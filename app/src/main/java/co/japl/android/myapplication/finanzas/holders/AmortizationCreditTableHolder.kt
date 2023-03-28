package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.bussiness.DTO.AmortizationCreditFix
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.utils.AmortizationCreditFixEnum
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.time.LocalDate
import co.japl.android.myapplication.finanzas.holders.validations.COPtoBigDecimal
import java.time.temporal.ChronoUnit

class AmortizationCreditTableHolder(val view:View):ITableHolder<AmortizationCreditFix> {
    val kindTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()
    val calc = QuoteCredit()
    lateinit var table:TableLayout
    lateinit var date:MaterialTextView
    lateinit var periods:MaterialTextView
    lateinit var additionalMonthly:MaterialTextView
    lateinit var additional:MaterialTextView
    lateinit var interest:MaterialTextView
    lateinit var quote:MaterialTextView
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
        additional = view.findViewById(R.id.additional_acf)
        interest = view.findViewById(R.id.interest_acf)
        quote = view.findViewById(R.id.quote_acf)
    }

    override fun setData(creditValue: CalcDTO) {
        credit = creditValue
        val quoteValue = credit.quoteCredit + (additionalValue?.let { additionalValue }?:BigDecimal.ZERO)
        quote.text = NumbersUtil.COPtoString(quoteValue)
        periods.text = credit.period.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun create() {
        var currentCreditValue = credit.valueCredit
        val tax = kindTaxSvc.getNM(credit.interest,KindOfTaxEnum.valueOf(credit.kindOfTax)) / 100
        for ( period in 1 .. credit.period){
            val interest = (currentCreditValue.toDouble() * tax).toBigDecimal()
            //val interest = calc.getInterest(credit.valueCredit,credit.interest,credit.period.toInt(),credit.quoteCredit,period.toInt(),KindOfTaxEnum.valueOf(credit.kindOfTax))
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
        for(amortization in amortizationList){
            paid = false
            val row = TableRow(view.context)
            paid = amortization.order == quotesPaid
            Log.d(javaClass.name,"QuotesPaid $quotesPaid")
            if (paid){
                row.setBackgroundColor(background)
            }

            val period = createTextView(amortization.order.toString(),paid)
            row.addView(period)

            val currentCreditValue = createTextView(NumbersUtil.COPtoString(amortization.currentValueCredit),paid)
            row.addView(currentCreditValue)

            val capital = createTextView(NumbersUtil.COPtoString(amortization.capitalValue),paid)
            row.addView(capital)

            val interest = createTextView(NumbersUtil.COPtoString(amortization.interestValue),paid)
            row.addView(interest)

            table.addView(row)
        }
        interest.text = NumbersUtil.COPtoString(interestToPay)
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