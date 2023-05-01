package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.RequiresApi
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.finanzas.bussiness.DTO.AmortizationCreditFix
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.holders.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.enums.AmortizationCreditFixEnum
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.time.LocalDate
import com.google.android.material.button.MaterialButton

class AmortizationCreditTableHolder(val view:View): ITableHolder<AmortizationCreditFix> {
    val kindTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()
    val calc = QuoteCredit()
    lateinit var table:TableLayout
    lateinit var date:MaterialTextView
    lateinit var periods:MaterialTextView
    lateinit var additionalMonthly:MaterialTextView
    lateinit var additional:MaterialTextView
    lateinit var interest:MaterialTextView
    lateinit var tax:MaterialTextView
    lateinit var quote:MaterialTextView
    lateinit var btnAdditional:MaterialButton
    lateinit var credit:CalcDTO
    private val amortizationList:MutableList<AmortizationCreditFix> = ArrayList()
    private var interestToPay:BigDecimal = BigDecimal.ZERO
    private var additionalValue:BigDecimal = BigDecimal.ZERO
    private var sumAdditionalValue:BigDecimal = BigDecimal.ZERO
    private var quotesPaid:Int = 0
    private val colorPaid = view.resources.getColor(androidx.media.R.color.primary_text_default_material_dark)
    private val background = view.resources.getColor(R.color.green_background)
    @RequiresApi(Build.VERSION_CODES.O)
    private var dateBill:LocalDate = LocalDate.MIN

    override fun setup(actions: View.OnClickListener?) {
        table = view.findViewById(R.id.amortization_acf)
        date = view.findViewById(R.id.date_acf)
        periods = view.findViewById(R.id.period_acf)
        additionalMonthly = view.findViewById(R.id.additional_monthly__acf)
        btnAdditional = view.findViewById(R.id.btn_additional_acf)
        additional = view.findViewById(R.id.additional_acf)
        interest = view.findViewById(R.id.interest_acf)
        tax = view.findViewById(R.id.tv_interest_acf)
        quote = view.findViewById(R.id.quote_acf)
        btnAdditional.setOnClickListener(actions)
    }

    override fun setData(creditValue: CalcDTO) {
        credit = creditValue
        val quoteValue = credit.quoteCredit + (additionalValue?.let { additionalValue }?:BigDecimal.ZERO)
        quote.text = NumbersUtil.COPtoString(quoteValue)
        periods.text = credit.period.toString()
        Log.d(javaClass.name,"Tax:. ${creditValue.interest}")
        tax.text = "${creditValue.interest} % ${creditValue.kindOfTax}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun create() {
        var currentCreditValue = credit.valueCredit
        val tax = kindTaxSvc.getNM(credit.interest, KindOfTaxEnum.valueOf(credit.kindOfTax)) / 100
        for ( period in 1 .. credit.period){
            val interest = (currentCreditValue.toDouble() * tax).toBigDecimal()
            val capital = credit.quoteCredit -  interest
            currentCreditValue -= capital
            amortizationList.add(
                AmortizationCreditFix(period.toInt(),
                currentCreditValue + capital,
                interest,
                capital,
                credit.quoteCredit,
                currentCreditValue,
                additionalValue)
            )
            sumAdditionalValue += additionalValue
            interestToPay += interest
        }
        additional.text = NumbersUtil.COPtoString(sumAdditionalValue)
    }

    override fun load() {
        var paid = false
        val layoutParams = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f)
        val layoutParams1 = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f)
        val layoutParams2 = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,2f)
        val layoutParams3 = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,3f)
        layoutParams.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
        layoutParams1.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
        layoutParams2.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
        layoutParams3.setMargins(view.resources.getDimension(R.dimen.column_space_min).toInt())
        for(amortization in amortizationList){
            paid = false
            val row = TableRow(view.context)
            TableRow.LayoutParams.WRAP_CONTENT
            paid = amortization.order == quotesPaid


            val period = createTextView(amortization.order.toString(),paid)
            period.textAlignment = View.TEXT_ALIGNMENT_CENTER
            period.layoutParams = layoutParams1
            row.addView(period)

            val currentCreditValue = createTextView(NumbersUtil.COPtoString(amortization.currentValueCredit),paid)
            currentCreditValue.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            currentCreditValue.layoutParams = layoutParams2
            row.addView(currentCreditValue)

            val capital = createTextView(NumbersUtil.COPtoString(amortization.capitalValue),paid)
            capital.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            capital.layoutParams = layoutParams3
            row.addView(capital)

            val interest = createTextView(NumbersUtil.COPtoString(amortization.interestValue),paid)
            interest.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            interest.layoutParams = layoutParams3
            row.addView(interest)

            table.addView(row,TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT)
        }
        interest.text = NumbersUtil.COPtoString(interestToPay)
    }

    private fun createTextView(value:String,paid:Boolean):MaterialTextView{
        val textView = MaterialTextView(view.context)
        textView.setPadding(view.resources.getDimension(R.dimen.space_min).toInt())
        textView.text = value
        if(paid){
            textView.setTextColor(colorPaid)
            textView.setBackgroundColor(background)
        }
        return textView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun add(name: String, value: Any) {
        when(name){
            AmortizationCreditFixEnum.ADDITIONAL.name-> {
                additionalValue = value as BigDecimal
                additionalMonthly.text = NumbersUtil.COPtoString(additionalValue)
            }
            AmortizationCreditFixEnum.QUOTES_PAID.name-> quotesPaid = (value as Long).toInt()
            AmortizationCreditFixEnum.DATE_BILL.name-> {
                dateBill = value as LocalDate
                date.text = DateUtils.localDateToString(dateBill)
            }
        }
    }


}