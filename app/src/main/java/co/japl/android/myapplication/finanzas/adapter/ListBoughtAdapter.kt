package co.japl.android.myapplication.adapter

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.bussiness.mapping.CalcMap
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCard
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.holders.view.BoughtViewHolder
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ListBoughtAdapter(private val data:MutableList<CreditCardBoughtDTO>,private val cutOff:LocalDateTime,private val inflater: LayoutInflater,val navController: NavController) : RecyclerView.Adapter<BoughtViewHolder>() {
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    lateinit var searchSvc: SearchSvc<CreditCardBoughtDTO>
    private val  kindOfTaxSvc = KindOfTaxImpl()
    private lateinit var taxSvc:SaveSvc<TaxDTO>
    lateinit var buyCreditCardSettingSvc: SaveSvc<BuyCreditCardSettingDTO>
    lateinit var creditCardSettingSvc: SaveSvc<CreditCardSettingDTO>
    lateinit var creditCardSvc: CreditCardImpl
    lateinit var view:View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoughtViewHolder {
        dbConnect = ConnectDB(parent.context)
        saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
        creditCardSvc = CreditCardImpl(dbConnect)
        taxSvc = TaxImpl(dbConnect)
        creditCardSettingSvc = CreditCardSettingImpl(dbConnect)
        buyCreditCardSettingSvc = BuyCreditCardSettingImpl(dbConnect)
        view = parent
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
    private fun getTax(codCreditCard:Long,kind: TaxEnum):Optional<Pair<Double,String>>{
        val tax = (taxSvc as TaxImpl).get(codCreditCard,cutOff.month.value,cutOff.year,kind)
        if(tax.isPresent){
            return Optional.ofNullable(Pair(tax.get().value,tax.get().kindOfTax ?: KindOfTaxEnum.EM.name))
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSettings(codBought:Int,taxSettup:Double):Optional<Double>{
        val buyCCSettingDto = buyCreditCardSettingSvc.get(codBought).also { Log.v(this.javaClass.name," response buy setting $it") }
        if(buyCCSettingDto.isPresent){
            val creditCardSettingDto = creditCardSettingSvc.get(buyCCSettingDto.get().codeCreditCardSetting).also { Log.v(this.javaClass.name," response setting $it") }
            if(creditCardSettingDto.isPresent){
                return Optional.of(creditCardSettingDto.get().value.toDouble()).also { Log.v(this.javaClass.name,"getSettings: $it") }
            }else if(taxSettup == 0.0){
                val creditCardSettingDto = creditCardSettingSvc.get(0).also { Log.v(this.javaClass.name," response setting $it") }
                if(creditCardSettingDto.isPresent){
                    return Optional.of(creditCardSettingDto.get().value.toDouble()).also { Log.v(this.javaClass.name,"getSettings: $it") }
                }
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInterestValue(creditCardBoughtDTO: CreditCardBoughtDTO,taxCCValue: Optional<Pair<Double,String>>,taxADVValue: Optional<Pair<Double,String>>):Pair<Double,KindOfTaxEnum>{
        val setting = getSettings(creditCardBoughtDTO.id,creditCardBoughtDTO.interest)
        return if(setting.isPresent){
            if(setting.get() > 0) {
                Pair(kindOfTaxSvc.getNM(setting.get(), KindOfTaxEnum.EM),KindOfTaxEnum.EM)
            }else{
                Pair(setting.get(),KindOfTaxEnum.EM)
            }
        }else {
            when (creditCardBoughtDTO.kind) {
                TaxEnum.CASH_ADVANCE.ordinal.toShort() -> {
                    if(taxADVValue.isPresent){
                        Pair(kindOfTaxSvc.getNM(taxADVValue.get().first, KindOfTaxEnum.valueOf(taxADVValue.get().second)), KindOfTaxEnum.EM)
                    }else{
                        Pair(0.0,KindOfTaxEnum.EM)
                    }
                }
                TaxEnum.CREDIT_CARD.ordinal.toShort() -> {
                    if(taxCCValue.isPresent){
                        Pair(kindOfTaxSvc.getNM(taxCCValue.get().first, KindOfTaxEnum.valueOf(taxCCValue.get().second)), KindOfTaxEnum.EM)
                    }else{
                        Pair(0.0,KindOfTaxEnum.EM)
                    }
                }
                else -> Pair(kindOfTaxSvc.getNM(creditCardBoughtDTO.interest, KindOfTaxEnum.valueOf(creditCardBoughtDTO.kindOfTax)), KindOfTaxEnum.EM)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInterest(dto:CreditCardBoughtDTO,taxCCValue:Optional<Pair<Double,String>>,taxAdvValue:Optional<Pair<Double,String>>,creditCard:Optional<CreditCardDTO>):BigDecimal{
        if( dto.month > 1){
            val months = DateUtils.getMonths(dto.boughtDate,cutOff)
            if(creditCard.isPresent && creditCard.get().interest1NotQuote && months == 0L && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD) {
                return BigDecimal.ZERO
            }
                val quote = dto.valueItem.divide(dto.month.toBigDecimal(), 8, RoundingMode.CEILING)
                val capitalBought = quote.multiply(months.toBigDecimal())
                val pendingCapital = dto.valueItem.minus(capitalBought)
                val interest = getInterestValue(dto, taxCCValue, taxAdvValue)
                if(creditCard.isPresent && creditCard.get().interest1NotQuote && months == 1L && dto.month > 1 && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD){
                    return pendingCapital.multiply(interest.first.toBigDecimal()) + dto.valueItem.multiply(interest.first.toBigDecimal()).also {
                        Log.v(javaClass.name,"$pendingCapital X $interest + ${dto.valueItem} X $interest = ${pendingCapital.multiply(interest.first.toBigDecimal())} + ${dto.valueItem.multiply(interest.first.toBigDecimal())} = $it")
                    }
                }else if (  pendingCapital > BigDecimal.ZERO) {
                    return pendingCapital.multiply(interest.first.toBigDecimal()).also{                    Log.v(
                        this.javaClass.name,
                        "Value ${dto.valueItem} - Bought $capitalBought X $months =  Pending $pendingCapital interest ${interest.first.toBigDecimal()} ${interest.second} = $it"
                    )
                    }
                }
            }
        return BigDecimal.ZERO
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
        val creditCard = creditCardSvc.get(data[position].codeCreditCard)
        Log.d(javaClass.name,"$creditCard")
        val taxAdv = getTax(data[position].codeCreditCard.toLong(), TaxEnum.CASH_ADVANCE)
        val taxCC = getTax(data[position].codeCreditCard.toLong(), TaxEnum.CREDIT_CARD)
        val tax = Optional.ofNullable(getInterestValue(data[position],taxCC,taxAdv))
        val interest = getInterest(data[position],taxCC,taxAdv,creditCard)
        val capital = getCapital(data[position])
        val quotesBought = getQuotesBought(data[position])
        val pendingToPay = getPendingToPay(data[position])
        val interestQuote = if(((creditCard.isPresent && creditCard.get().interest1Quote && quotesBought == 0L && data[position].month == 1) ||
            (creditCard.isPresent && creditCard.get().interest1NotQuote && quotesBought == 0L && data[position].month > 1)) && TaxEnum.findByOrdinal(data[position].kind) == TaxEnum.CREDIT_CARD){
            Pair(0.0,KindOfTaxEnum.EM)
        } else{
            tax.orElse(Pair(data[position].interest,KindOfTaxEnum.valueOf(data[position].kindOfTax)))
        }
      holder.setFields(data[position],capital,interest,quotesBought,pendingToPay,interestQuote) {
          when (it) {
              MoreOptionsItemsCreditCard.DELETE -> delete(  holder,position)
              MoreOptionsItemsCreditCard.AMORTIZATION -> amortization( quotesBought,capital,interest,KindOfTaxEnum.valueOf(data[position].kindOfTax),position,creditCard.get().interest1NotQuote && TaxEnum.findByOrdinal(data[position].kind) == TaxEnum.CREDIT_CARD)
              MoreOptionsItemsCreditCard.EDIT -> edit(position)
              MoreOptionsItemsCreditCard.ENDING->ending(holder,position)
              MoreOptionsItemsCreditCard.UPDATE_VALUE->updateRecurrentValue(holder,position)
          }
      }
    }

    fun edit(position:Int){
        CreditCardQuotesParams.Companion.ListBought.newInstance(
            data[position].id,
            data[position].codeCreditCard,
            view.findNavController()
        )
    }

    fun amortization(quotesBought:Long,capital:BigDecimal,interest:BigDecimal,kindOfTax:KindOfTaxEnum,position:Int,quote1NotPaid:Boolean){
        AmortizationTableParams.newInstanceQuotes(
            CalcMap().mapping(
                data[position],
                interest + capital,
                interest,
                capital,
                kindOfTax
            ), quotesBought,quote1NotPaid, view.findNavController()
        )
    }

    fun delete(holder: BoughtViewHolder,position:Int){
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.do_you_want_to_delete_this_record)
            .setPositiveButton(R.string.delete, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (saveSvc.delete(data[position].id)) {
                dialog.dismiss()
                Snackbar.make(
                    view,
                    R.string.delete_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also{
                        data.removeAt(position)
                        this.notifyDataSetChanged()
                        this.notifyItemRemoved(position)
                    }
            } else {
                dialog.dismiss()
                Snackbar.make(
                    holder.itemView,
                    R.string.dont_deleted,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun ending(holder: BoughtViewHolder, position:Int){
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.do_you_want_to_ending_recurrent_payment)
            .setPositiveButton(R.string.ending, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val dto = data[position]
            dto.endDate = cutOff.minusMonths(1).plusDays(1)
            if (saveSvc.save(dto)>0) {
                dialog.dismiss()
                Snackbar.make(
                    view,
                    R.string.ending_recurrent_payment_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show()
            } else {
                dialog.dismiss()
                Snackbar.make(
                    holder.itemView,
                    R.string.dont_ending_recurrent_payment,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateRecurrentValue(holder: BoughtViewHolder, position:Int){
        val inflaterr = inflater.inflate(R.layout.dialog_update_value_cop,null)
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.update_value_recurrent_paymner)
            .setView(inflaterr)
            .setPositiveButton(R.string.ending, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val value = inflaterr.findViewById<EditText>(R.id.ed_value_dupv)
            val dtoNew = data[position].copy()
            dtoNew.endDate = LocalDateTime.of(9999, 12, 31, 0, 0)
            dtoNew.createDate = LocalDateTime.now()
            dtoNew.id = 0
            dtoNew.valueItem = NumbersUtil.toBigDecimal(value)
            dtoNew.boughtDate = dtoNew.boughtDate.withYear(dtoNew.createDate.year).withMonth(dtoNew.createDate.monthValue)
            val dto = data[position]
            dto.endDate = cutOff.minusMonths(1).plusDays(1)
            dto.createDate = LocalDateTime.now()
            if(saveSvc.save(dtoNew) >0){
                if (saveSvc.save(dto)>0) {
                    dialog.dismiss()
                    Snackbar.make(
                        view,
                        R.string.ending_recurrent_payment_successfull,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.close) {}
                        .show().also {
                            CreditCardQuotesParams.Companion.CreateQuote.toBack(navController)
                        }
                }else {
                    dialog.dismiss()
                    Snackbar.make(
                        holder.itemView,
                        R.string.dont_ending_recurrent_payment,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.close, null).show()
                }
            } else {
                dialog.dismiss()
                Snackbar.make(
                    holder.itemView,
                    R.string.dont_ending_recurrent_payment,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
        }
    }
}