package co.japl.android.myapplication.finanzas.holders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.bussiness.DTO.DifferInstallmentDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AddAmortizationImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAddAmortizationSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.controller.AddValueAmortizationDialog
import co.japl.android.myapplication.finanzas.controller.AmortizationGeneralDialog
import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import co.japl.android.myapplication.finanzas.putParams.ExtraValueListParam
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal

class AmortizationTableHolder(val view:View, val kindOf:AmortizationKindOfEnum, val inflater:LayoutInflater,val navController: NavController): ITableHolder<CalcDTO>, OnClickListener {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()
    private lateinit var addValueAmortizationSvc:IAddAmortizationSvc
    private lateinit var table: TableLayout
    private lateinit var creditData: CalcDTO
    private lateinit var amortizationList: ArrayList<Amortization>
    private lateinit var interesToPay:BigDecimal
    private lateinit var creditValue: TextView
    private lateinit var quoteValue: TextView
    private lateinit var quoteValueLayout:TextInputLayout
    private lateinit var totalColumn: TextView
    private lateinit var nextValueColumn: TextView
    private lateinit var capitalValueColumn: TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var btnList:Button
    private lateinit var btnSeeMore:Button
    private var quotesPaid:Long = 0
    private var quote1NotPaid:Boolean = false
    private var differInstallment:DifferInstallmentDTO? = null
    private val colorPaid = view.resources.getColor(androidx.media.R.color.primary_text_default_material_dark)
    private val backgroun = view.resources.getColor(R.color.green_background)
    private var monthsCalc:Long? = null
    private lateinit var list : List<AddAmortizationDTO>

    override fun setup(actions: View.OnClickListener?) {
        addValueAmortizationSvc = AddAmortizationImpl(ConnectDB(view.context))
        table = view.findViewById(R.id.tableAT)
        creditValue = view.findViewById(R.id.credit_value_at)
        quoteValue = view.findViewById(R.id.quote_value_at)
        totalColumn = view.findViewById(R.id.totalColumn)
        nextValueColumn = view.findViewById(R.id.NextValueColumn)
        capitalValueColumn = view.findViewById(R.id.capital_column_at)
        quoteValueLayout = view.findViewById(R.id.quoteValueLayout)
        progressBar = view.findViewById(R.id.pb_load_at)
        btnList = view.findViewById(R.id.btn_extra_values_list_at)
        btnSeeMore = view.findViewById(R.id.btn_see_more_at)
        amortizationList = ArrayList<Amortization>()
        interesToPay = BigDecimal.ZERO
        totalColumn.visibility= View.GONE
        nextValueColumn.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        btnList.setOnClickListener(this)
        btnSeeMore.setOnClickListener(this)
    }

    override fun setData(creditValue: CalcDTO) {
        creditData = creditValue
        this.creditValue.text = NumbersUtil.toString(creditValue.valueCredit)
        this.quoteValue.text = NumbersUtil.toString(creditValue.quoteCredit)

        list = addValueAmortizationSvc.getAll(creditValue.id)
    }

    override fun add(name:String,value:Any){
        if(name == "QuotesPaid"){
            quotesPaid = value as Long
        }else
        if(name == "quote1NotPaid"){
            quote1NotPaid = value as Boolean
        }
        if(name == "differInstallment"){
            differInstallment = value as DifferInstallmentDTO
        }
        if(name == "monthsCalc"){
            monthsCalc = value as Long
        }
    }

    override fun create() {
        creditData?.let {
            when(CalcEnum.valueOf(it.type)){
                CalcEnum.FIX-> quoteFixCalc()
                CalcEnum.VARIABLE-> quoteVariableCalc()
            }
        }
    }

    private fun quoteFixCalc(){
        Log.d(javaClass.name,"Quote Fix")
        var currentCreditValue = creditData.valueCredit
        val tax = getTax().toBigDecimal()
        for ( period in 1 .. creditData.period){
            val interest = currentCreditValue * tax
            val capital = creditData.quoteCredit -  interest
            currentCreditValue -= capital
            amortizationList.add(Amortization(period.toInt(),
                currentCreditValue + capital,
                interest,
                capital,
                creditData.quoteCredit,
                currentCreditValue))
            interesToPay += interest
        }
        quoteValueLayout.visibility = View.VISIBLE
        this.totalColumn.visibility = View.GONE
        this.capitalValueColumn.visibility = View.VISIBLE
    }

    private fun quoteVariableCalc(){
        Log.d(javaClass.name,"Quote Variable")
        var currentCreditValue = creditData.valueCredit
        val periods:Long = differInstallment?.let{
                it.oldInstallment.toLong()
            }?:creditData.period

        var capital = differInstallment?.let{
                (it.originValue / it.oldInstallment)
            }?:(currentCreditValue.toDouble() / periods)

        val tax = getTax()
        for ( period in 1 .. creditData.period){

            differInstallment?.takeIf { ((creditData.period - it.newInstallment.toLong()) + 1) == period }
                ?.let {
                   capital = it.pendingValuePayable / it.newInstallment
                }

            if(capital.toBigDecimal() > currentCreditValue){
                capital = currentCreditValue.toDouble()
            }

            val extraCapitalValue =  getExtraValue(period)

            val interest = getInterest(period,currentCreditValue,tax)
            amortizationList.add(Amortization(period.toInt(),
                currentCreditValue ,
                interest,
                (capital + extraCapitalValue).toBigDecimal()  ,
                (capital + extraCapitalValue).toBigDecimal()   + interest,
                currentCreditValue - (capital + extraCapitalValue).toBigDecimal()))
            currentCreditValue -=  (capital + extraCapitalValue).toBigDecimal()
            interesToPay += interest
            if(periods > monthsCalc!! && monthsCalc == period && differInstallment == null){
                break
            }
            if(currentCreditValue <= BigDecimal.ZERO){
                break
            }
        }
        quoteValueLayout.visibility = View.GONE
        this.totalColumn.visibility = View.VISIBLE
        this.capitalValueColumn.visibility = View.GONE
    }

    private fun getInterest(period:Long,currentCreditValue:BigDecimal,tax:Double):BigDecimal{
        return if(quote1NotPaid && period == 1L){
            BigDecimal.ZERO
        }else if(quote1NotPaid && period == 2L){
            currentCreditValue * tax.toBigDecimal() + creditData.valueCredit *  tax.toBigDecimal()
        }else {
            currentCreditValue * tax.toBigDecimal()
        }
    }

    private fun getTax():Double{
        return if(creditData.interest > 0){
            Log.d(javaClass.name,"${creditData.interest} ${creditData.kindOfTax}")
            kindOfTaxSvc.getNM(creditData.interest, KindOfTaxEnum.valueOf(creditData.kindOfTax))
        } else {
            creditData.interest
        }
    }

   override fun load(){
       var paid = false
       val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)
       val layoutParams1 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1f)
       val layoutParams2 = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,2f)
       val layoutParams3 = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,3f)
       layoutParams1.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
       layoutParams2.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
       layoutParams3.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
       table.setColumnStretchable(1,false)
       table.setColumnStretchable(2,true)
       table.setColumnStretchable(3,true)
       table.setColumnStretchable(4,true)
       table.setColumnStretchable(5,true)
       table.setColumnStretchable(6,true)
        for( amortization in amortizationList){
            paid = false
            val row = TableRow(view.context)
            paid = amortization.order.toLong() == quotesPaid

            val period = createTextView(amortization.order.toString(),paid)
            period.layoutParams = layoutParams
            row.addView(period)

            val currentCreditValue = createTextView(NumbersUtil.COPtoString(amortization.currentValueCredit),paid)
            currentCreditValue.layoutParams = layoutParams1
            row.addView(currentCreditValue)

            val capital = createTextView(NumbersUtil.COPtoString(amortization.capitalValue),paid)
            capital.visibility = capitalValueColumn.visibility
            capital.layoutParams = layoutParams3
            row.addView(capital)

            val interest = createTextView(NumbersUtil.COPtoString(amortization.interestValue),paid)
            interest.layoutParams = layoutParams3
            row.addView(interest)

            val total = createTextView( NumbersUtil.COPtoString(amortization.totalQuote),paid)
            total.layoutParams = layoutParams2
            total.visibility = totalColumn.visibility
            row.addView(total)

            val newCurrentValue = createTextView(NumbersUtil.COPtoString(amortization.newCurrentValueCredit),paid)
            newCurrentValue.layoutParams = layoutParams2
            newCurrentValue.visibility = this.nextValueColumn.visibility
            row.addView(newCurrentValue)

            table.addView(row,TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT)
        }
       creditData.interestValue = interesToPay
       progressBar.visibility = View.GONE
    }

    private fun getExtraValue(quote:Long):Double{
        return list.filter { it.nbrQuote == quote }.sumOf { it.value }
    }

    private fun createTextView(value:String,paid:Boolean):MaterialTextView{
        val textView = MaterialTextView(view.context)
        textView.setPadding(view.resources.getDimension(R.dimen.space_min).toInt())
        textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        textView.text = value
        if(paid){
            textView.setTextColor(colorPaid)
            textView.setBackgroundColor(backgroun)
        }
        return textView
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_extra_values_list_at -> ExtraValueListParam.newInstance(creditData.id,kindOf, navController )
            R.id.btn_see_more_at -> AmortizationGeneralDialog(view.context,inflater,creditData).show()
        }
    }


}