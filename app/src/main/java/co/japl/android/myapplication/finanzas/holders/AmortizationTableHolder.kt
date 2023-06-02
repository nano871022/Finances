package co.japl.android.myapplication.finanzas.holders

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.holders.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal

class AmortizationTableHolder(val view:View): ITableHolder<CalcDTO> {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()
    private lateinit var table: TableLayout
    private lateinit var creditData: CalcDTO
    private lateinit var amortizationList: ArrayList<Amortization>
    private lateinit var interesToPay:BigDecimal
    private lateinit var creditValue: TextView
    private lateinit var quoteValue: TextView
    private lateinit var capitalValue: TextView
    private lateinit var quoteValueLayout:TextInputLayout
    private lateinit var capitalValueLayout:TextInputLayout
    private lateinit var tax: TextView
    private lateinit var periods: TextView
    private lateinit var interestToPay: TextView
    private lateinit var totalColumn: TextView
    private lateinit var nextValueColumn: TextView
    private lateinit var capitalValueColumn: TextView
    private lateinit var progressBar:ProgressBar
    private var quotesPaid:Long = 0
    private val colorPaid = view.resources.getColor(androidx.media.R.color.primary_text_default_material_dark)
    private val backgroun = view.resources.getColor(R.color.green_background)


    override fun setup(actions: View.OnClickListener?) {

        table = view.findViewById(R.id.tableAT)
        creditValue = view.findViewById(R.id.credit_value_at)
        quoteValue = view.findViewById(R.id.quote_value_at)
        totalColumn = view.findViewById(R.id.totalColumn)
        nextValueColumn = view.findViewById(R.id.NextValueColumn)
        capitalValueColumn = view.findViewById(R.id.capital_column_at)
        capitalValue = view.findViewById(R.id.capital_value_at)
        tax = view.findViewById(R.id.tax_at)
        periods = view.findViewById(R.id.periods_at)
        interestToPay = view.findViewById(R.id.interest_to_pay_at)
        capitalValueLayout = view.findViewById(R.id.capitalValueLayout)
        quoteValueLayout = view.findViewById(R.id.quoteValueLayout)
        progressBar = view.findViewById(R.id.pb_load_at)

        amortizationList = ArrayList<Amortization>()
        interesToPay = BigDecimal.ZERO
        totalColumn.visibility= View.GONE
        nextValueColumn.visibility = View.GONE
        capitalValueLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun setData(creditValue: CalcDTO) {
        creditData = creditValue
        this.creditValue.text = NumbersUtil.toString(creditValue.valueCredit)
        this.quoteValue.text = NumbersUtil.toString(creditValue.quoteCredit)
        this.tax.text = "${creditValue.interest} ${creditValue.kindOfTax}"
        this.periods.text = creditValue.period.toString()
    }

    override fun add(name:String,value:Any){
        if(name == "QuotesPaid"){
            quotesPaid = value as Long
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
        capitalValueLayout.visibility = View.GONE
        quoteValueLayout.visibility = View.VISIBLE
        this.totalColumn.visibility = View.GONE
        this.capitalValueColumn.visibility = View.VISIBLE
    }

    private fun quoteVariableCalc(){
        Log.d(javaClass.name,"Quote Variable")
        var currentCreditValue = creditData.valueCredit
        val periods = creditData.period
        val capital = currentCreditValue.toDouble() / periods
        Log.d(javaClass.name,"$capital = $currentCreditValue / $periods")
        val tax = getTax()
        for ( period in 1 .. creditData.period){
            val interest = currentCreditValue * tax.toBigDecimal()
            amortizationList.add(Amortization(period.toInt(),
                currentCreditValue ,
                interest,
                capital.toBigDecimal(),
                capital.toBigDecimal()   + interest,
                currentCreditValue - capital.toBigDecimal()))
            currentCreditValue -=  capital.toBigDecimal()
            interesToPay += interest
            Log.d(javaClass.name,"${amortizationList.size} $currentCreditValue $capital (${creditData.valueCredit} / ${creditData.period}) $interest - $tax - ${creditData.interest}")
        }
        capitalValue.text = NumbersUtil.toString(creditData.capitalValue)
        capitalValueLayout.visibility = View.VISIBLE
        quoteValueLayout.visibility = View.GONE
        this.totalColumn.visibility = View.VISIBLE
        this.capitalValueColumn.visibility = View.GONE
    }

    fun getTax():Double{
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
       this.interestToPay.text = NumbersUtil.toString(interesToPay)
       progressBar.visibility = View.GONE
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



}