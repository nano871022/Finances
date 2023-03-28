package co.japl.android.myapplication.finanzas.holders

import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal

class AmortizationTableHolder(val view:View):ITableHolder<CalcDTO> {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()
    private lateinit var table: TableLayout
    private lateinit var creditData: CalcDTO
    private lateinit var amortizationList: ArrayList<Amortization>
    private lateinit var interesToPay:BigDecimal
    private lateinit var creditValue: MaterialTextView
    private lateinit var quoteValue: MaterialTextView
    private lateinit var capitalValue: MaterialTextView
    private lateinit var quoteValueLayout:TextInputLayout
    private lateinit var capitalValueLayout:TextInputLayout
    private lateinit var tax: MaterialTextView
    private lateinit var periods: MaterialTextView
    private lateinit var interestToPay: MaterialTextView
    private lateinit var totalColumn: MaterialTextView
    private lateinit var nextValueColumn: MaterialTextView
    private lateinit var capitalValueColumn: MaterialTextView
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

        amortizationList = ArrayList<Amortization>()
        interesToPay = BigDecimal.ZERO
        totalColumn.visibility= View.GONE
        nextValueColumn.visibility = View.GONE
        capitalValueLayout.visibility = View.GONE
    }

    override fun setData(creditValue: CalcDTO) {
        creditData = creditValue
        this.creditValue.text = NumbersUtil.COPtoString(creditValue.valueCredit)
        this.quoteValue.text = NumbersUtil.COPtoString(creditValue.quoteCredit)
        this.tax.text = "${creditValue.interest} %"
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
        var currentCreditValue = creditData.valueCredit
        for ( period in 1 .. creditData.period){
            val interest = currentCreditValue * (creditData.interest / 100).toBigDecimal()
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
        var currentCreditValue = creditData.valueCredit
        for ( period in 1 .. creditData.period){
            val tax = kindOfTaxSvc.getNM(creditData.interest,KindOfTaxEnum.valueOf(creditData.kindOfTax)) / 100
            val interest = currentCreditValue * tax.toBigDecimal()
            val capital = creditData.quoteCredit - interest
            currentCreditValue -=  capital
            amortizationList.add(Amortization(period.toInt(),
                currentCreditValue + creditData.capitalValue,
                interest,capital,
                capital + interest,
                currentCreditValue))
            interesToPay += interest
        }
        capitalValue.text = NumbersUtil.COPtoString(creditData.capitalValue)
        capitalValueLayout.visibility = View.VISIBLE
        quoteValueLayout.visibility = View.GONE
        this.totalColumn.visibility = View.VISIBLE
        this.capitalValueColumn.visibility = View.GONE
    }

   override fun load(){
       var paid = false

        for( amortization in amortizationList){
            paid = false
            val row = TableRow(view.context)
            paid = amortization.order.toLong() == quotesPaid
            if(paid){
                row.setBackgroundColor(backgroun)
            }

            val period = createTextView(amortization.order.toString(),paid)
            row.addView(period)

            val currentCreditValue = createTextView(NumbersUtil.COPtoString(amortization.currentValueCredit),paid)
            row.addView(currentCreditValue)
            val capital = createTextView(NumbersUtil.COPtoString(amortization.capitalValue),paid)
            capital.visibility = capitalValueColumn.visibility
            row.addView(capital)

            val interest = createTextView(NumbersUtil.COPtoString(amortization.interestValue),paid)
            row.addView(interest)

            val total = createTextView( NumbersUtil.COPtoString(amortization.totalQuote),paid)
            total.visibility = totalColumn.visibility

            row.addView(total)

            val newCurrentValue = createTextView(NumbersUtil.COPtoString(amortization.newCurrentValueCredit),paid)
            newCurrentValue.visibility = this.nextValueColumn.visibility
            row.addView(newCurrentValue)


            table.addView(row)
        }
       this.interestToPay.text = NumbersUtil.COPtoString(interesToPay)
    }

    private fun createTextView(value:String,paid:Boolean):MaterialTextView{
        val textView = MaterialTextView(view.context)
        textView.setPadding(view.resources.getDimension(R.dimen.space_min).toInt())
        textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        textView.text = value
        if(paid){
            textView.setTextColor(colorPaid)
        }
        return textView
    }



}