package co.japl.android.myapplication.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.holders.view.BoughtViewHolder
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

class ListBoughtAdapter(private val data:List<CreditCardBoughtDTO>,private val cutOff:LocalDateTime) : RecyclerView.Adapter<BoughtViewHolder>() {
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    lateinit var searchSvc: SearchSvc<CreditCardBoughtDTO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoughtViewHolder {
        dbConnect = ConnectDB(parent.context)
        saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
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
    private fun getInterest(dto:CreditCardBoughtDTO):BigDecimal{
        if(dto.month > 1){
            val months = DateUtils.getMonths(dto.boughtDate,cutOff)
            val quote = dto.valueItem.divide(dto.month.toBigDecimal(),8,RoundingMode.CEILING)
            val capitalBought = quote.multiply(months.toBigDecimal())
            val pendingCapital = dto.valueItem.minus(capitalBought)
            val interest = dto.interest.toBigDecimal().divide(BigDecimal(100),8,RoundingMode.CEILING)
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
        val capital = getCapital(data[position])
        val interest = getInterest(data[position])
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