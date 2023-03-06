package co.japl.android.myapplication.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.holders.view.BoughtViewHolder
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
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
    lateinit var buyCreditCardSettingSvc: SaveSvc<BuyCreditCardSettingDTO>
    lateinit var creditCardSettingSvc: SaveSvc<CreditCardSettingDTO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoughtViewHolder {
        dbConnect = ConnectDB(parent.context)
        saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
        taxSvc = TaxImpl(dbConnect)
        creditCardSettingSvc = CreditCardSettingImpl(dbConnect)
        buyCreditCardSettingSvc = BuyCreditCardSettingImpl(dbConnect)
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
    private fun getTax(kind:TaxEnum):Optional<Double>{
        val tax = taxSvc.get(cutOff.month.value,cutOff.year,kind)
        if(tax.isPresent){
            return Optional.ofNullable(tax.get().value)
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSettings(codBought:Int):Optional<Double>{
        val buyCCSettingDto = buyCreditCardSettingSvc.get(codBought).also { Log.v(this.javaClass.name," response buy setting $it") }
        if(buyCCSettingDto.isPresent){
            val creditCardSettingDto = creditCardSettingSvc.get(buyCCSettingDto.get().codeCreditCardSetting).also { Log.v(this.javaClass.name," response setting $it") }
            if(creditCardSettingDto.isPresent){
                return Optional.of(creditCardSettingDto.get().value.toDouble()).also { Log.v(this.javaClass.name,"getSettings: $it") }
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInterestValue(creditCardBoughtDTO: CreditCardBoughtDTO,taxCCValue: Optional<Double>,taxADVValue: Optional<Double>):BigDecimal{
        val setting = getSettings(creditCardBoughtDTO.id)
        val interestTax = if(setting.isPresent){
            setting.get()
        }else {
            when (creditCardBoughtDTO.kind) {
                TaxEnum.CASH_ADVANCE.ordinal.toShort() -> taxADVValue.get()
                TaxEnum.CREDIT_CARD.ordinal.toShort() -> taxCCValue.get()
                else -> creditCardBoughtDTO.interest
            }
        }
        return interestTax.toBigDecimal().divide(BigDecimal(100),8,RoundingMode.CEILING)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInterest(dto:CreditCardBoughtDTO,taxCCValue:Optional<Double>,taxAdvValue:Optional<Double>):BigDecimal{
        if(dto.month > 1){
            val months = DateUtils.getMonths(dto.boughtDate,cutOff)
            val quote = dto.valueItem.divide(dto.month.toBigDecimal(),8,RoundingMode.CEILING)
            val capitalBought = quote.multiply(months.toBigDecimal())
            val pendingCapital = dto.valueItem.minus(capitalBought)
            val interest = getInterestValue(dto,taxCCValue,taxAdvValue)
            Log.v(this.javaClass.name,"Value ${dto.valueItem} - Bought $capitalBought X $months =  Pending $pendingCapital interest $interest")
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
        val taxAdv = getTax(TaxEnum.CASH_ADVANCE)
        val taxCC = getTax(TaxEnum.CREDIT_CARD)
        val capital = getCapital(data[position])
        val tax = Optional.ofNullable(getInterestValue(data[position],taxCC,taxAdv).multiply(BigDecimal(100)).toDouble())
        val interest = getInterest(data[position],taxCC,taxAdv)
        val quotesBought = getQuotesBought(data[position])
        val pendintToPay = getPendingToPay(data[position])

      holder.setFields(data[position],capital,interest,quotesBought,pendintToPay,tax){
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