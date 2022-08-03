package co.japl.android.finanzas.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.finanzas.R
import co.japl.android.finanzas.bussiness.DB.connections.ConnectDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.finanzas.bussiness.interfaces.SaveSvc
import co.japl.android.finanzas.bussiness.interfaces.SearchSvc
import co.japl.android.finanzas.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.finanzas.bussiness.impl.TaxImpl
import co.japl.android.finanzas.bussiness.interfaces.ITaxSvc
import co.japl.android.finanzas.holders.view.BoughtViewHolder
import co.japl.android.finanzas.utils.DateUtils
import co.japl.android.finanzas.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

class ListBoughtAdapter(private val data:List<CreditCardBoughtDTO>,private val cutOff:LocalDateTime) : RecyclerView.Adapter<BoughtViewHolder>() {
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    lateinit var searchSvc: SearchSvc<CreditCardBoughtDTO>
    lateinit var taxSvc:ITaxSvc

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoughtViewHolder {
        dbConnect = ConnectDB(parent.context)
        saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
        taxSvc = TaxImpl(dbConnect)
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.bought_item_list, parent, false)
        val viewHolder =  BoughtViewHolder(view)
        viewHolder.loadFields(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getCapital(dto:CreditCardBoughtDTO):BigDecimal{
        if(dto.month > 1){
            return dto.valueItem.divide(dto.month.toBigDecimal(),8,RoundingMode.CEILING)
        }
        return dto.valueItem
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTax():Optional<Double>{
        val tax = taxSvc.get(cutOff.month.value,cutOff.year)
        if(tax.isPresent){
            return Optional.ofNullable(tax.get().value)
        }
        return Optional.empty()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInterest(dto:CreditCardBoughtDTO,tax:Optional<Double>):BigDecimal{
        if(dto.month > 1){
            val months = DateUtils.getMonths(dto.boughtDate,cutOff)
            val quote = dto.valueItem.divide(dto.month.toBigDecimal(),8,RoundingMode.CEILING)
            val capitalBought = quote.multiply(months.toBigDecimal())
            val pendingCapital = dto.valueItem.minus(capitalBought)
            var interestTax = if (dto.kind == "0".toShort()) tax.get() else dto.interest
            val interest = interestTax.toBigDecimal().divide(BigDecimal(100),8,RoundingMode.CEILING)
            Log.d(this.javaClass.name,"Value ${dto.valueItem} - Bought $capitalBought X $months =  Pending $pendingCapital interest $interest")
            if(pendingCapital > BigDecimal(0)){
                return pendingCapital.multiply(interest)
            }
        }
        return BigDecimal(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPendingToPay(dto:CreditCardBoughtDTO):BigDecimal{
        if(dto.month > 1){
            val months = DateUtils.getMonths(dto.boughtDate,cutOff)
            val quote = dto.valueItem.divide(dto.month.toBigDecimal(),8,RoundingMode.CEILING)
            val capitalBought = quote.multiply(months.toBigDecimal())
            val pendingCapital = dto.valueItem.minus(capitalBought)
            if(pendingCapital > BigDecimal(0)){
                return pendingCapital
            }
        }
        return BigDecimal(0)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuotesBought(dto:CreditCardBoughtDTO):Long{
        if(dto.month > 1) {
            val months = DateUtils.getMonths(dto.boughtDate,cutOff)
            return months.toLong()
        }
        return 0
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BoughtViewHolder, position: Int) {
        val tax = getTax()
        val capital = getCapital(data[position])
        val interest = getInterest(data[position],tax)
        val quotesBought = getQuotesBought(data[position])
        val pendintToPay = getPendingToPay(data[position])

        holder.tvBoughtName.text = data[position].nameItem
        holder.tvCapitalBought.text = NumbersUtil.COPtoString(capital)
        holder.tvMonthBought.text = data[position].month.toString()
        holder.tvQuotesBought.text = quotesBought.toString()
        holder.tvCreditValueBought.text = NumbersUtil.COPtoString(data[position].valueItem)
        holder.tvTotalQuoteBought.text = NumbersUtil.COPtoString(capital.plus(interest))
        holder.tvBoughtDate.text = DateUtils.localDateTimeToString(data[position].boughtDate)
        holder.tvInterestBought.text = NumbersUtil.COPtoString(interest)
        holder.tvPendingToPay.text = NumbersUtil.COPtoString(pendintToPay)
        holder.tvTaxBoughtICC.text = tax.orElse(data[position].interest).toString()

        holder.btnBoughtDelete.setOnClickListener {
                if (saveSvc.delete(data[position].id)) {
                    Snackbar.make(holder.itemView, R.string.delete_successfull, Snackbar.LENGTH_LONG)
                        .setAction(R.string.close) {
                            this.notifyItemRemoved(position)
                            this.notifyDataSetChanged()
                        }
                        .show()
                } else {
                    Snackbar.make(holder.itemView, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.close,null).show()
                }
            }
    }
}