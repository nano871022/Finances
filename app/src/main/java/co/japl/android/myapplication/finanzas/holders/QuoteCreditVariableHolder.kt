package co.japl.android.myapplication.finanzas.holders

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterestQuote
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.CalcInterest
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class QuoteCreditVariableHolder(var container: View):IHolder<QuoteCreditCard>, ISpinnerHolder<QuoteCreditVariableHolder> {
    private val calc: Calc = QuoteCreditVariable()
    private val calcQuoteInt: CalcInterest = QuoteCreditVariableInterestQuote()
    lateinit var listMonths:MutableList<String>

    private lateinit var etValueCredit: TextInputEditText
    private lateinit var etTax: TextInputEditText
    private lateinit var etMonths: TextInputEditText
    private lateinit var tvCapitalValue: MaterialTextView
    private lateinit var tvInterestValue: MaterialTextView
    private lateinit var tvTotalValue: MaterialTextView
    private lateinit var llCalculation: LinearLayout
     lateinit var spMonth: MaterialAutoCompleteTextView
    private lateinit var btnSave: Button
    private lateinit var btnAmortization: Button

    private lateinit var quote:QuoteCreditCard

    override fun setFields(actions: View.OnClickListener?) {
        etValueCredit = container.findViewById(R.id.etNameItem)
        etTax = container.findViewById(R.id.etTax)!!
        etMonths = container.findViewById(R.id.etMonths)!!
        tvCapitalValue = container.findViewById(R.id.tvCapitalValue)!!
        tvInterestValue = container.findViewById(R.id.tvInterestValueList)!!
        llCalculation = container.findViewById(R.id.llCalculationQCCV)
        tvTotalValue = container.findViewById(R.id.tvTotalValue)!!
        spMonth = container.findViewById(R.id.months)
        btnAmortization = container.findViewById(R.id.btnAmortizationQCV)
        val btnClear:Button = container.findViewById(R.id.btnClear)
        btnClear.setOnClickListener(actions)
        val btnCalc:Button = container.findViewById(R.id.btnCalc)
        btnCalc.setOnClickListener(actions)
        btnSave = container.findViewById(R.id.btnSaveVariable)
        btnSave.setOnClickListener(actions)
        btnSave.visibility = View.INVISIBLE
        spMonth.setOnClickListener { spMonth.showDropDown() }
        spMonth.isFocusable = false
        quote = QuoteCreditCard()
        llCalculation.visibility=View.INVISIBLE
        etValueCredit.setOnFocusChangeListener{ _,focus->
            if(!focus && etValueCredit.text?.isNotBlank() == true){
                etValueCredit.setText(NumbersUtil.toString(etValueCredit))
            }
        }
        btnAmortization.setOnClickListener(actions)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        values.capitalValue.ifPresent {
            tvCapitalValue.text = NumbersUtil.COPtoString(it)
        }
        values.interestValue.ifPresent{
            tvInterestValue.text = NumbersUtil.toString(it)
        }
        values.value.ifPresent{
            tvTotalValue.text = NumbersUtil.COPtoString(it)
        }
        btnSave.visibility = View.VISIBLE
        btnAmortization.visibility = View.VISIBLE
        quote = values
        Log.d(this.javaClass.name,"Assign to quote ${quote.response} ${quote.capitalValue} ${quote.interestValue}")
        llCalculation.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        val quote = QuoteCreditCard()
        quote.name = Optional.of("unknown")
        quote.tax = Optional.ofNullable(etTax.text.toString().toDouble())
        quote.value= Optional.ofNullable(NumbersUtil.toBigDecimal(etValueCredit     ))
        quote.period= Optional.ofNullable(etMonths.text.toString().toLong())
        quote.type = CalcEnum.VARIABLE


        val capital = this.quote.getCapitalValue().orElse(BigDecimal.ZERO)
        val interest = this.quote.getInterestValue().orElse(BigDecimal.ZERO)
        val total = this.quote.getResponse().orElse(BigDecimal.ZERO)
        Log.d(this.javaClass.name,"Capital $capital")
        Log.d(this.javaClass.name,"Interest $interest")
        Log.d(this.javaClass.name,"Total $total")
        quote.capitalValue = Optional.ofNullable(capital)
        quote.interestValue = Optional.ofNullable(interest)
        quote.response = Optional.ofNullable(total)
        return quote
    }

    override fun cleanField() {
        etValueCredit.editableText.clear();
        etTax.editableText.clear()
        etMonths.editableText.clear()
        //tvQuoteValue.editableText.clear()
        btnSave.visibility = View.INVISIBLE
        llCalculation.visibility = View.INVISIBLE
        btnAmortization.visibility = View.INVISIBLE
    }

    override fun validate(): Boolean {
        var valid:Boolean = true
        if(etValueCredit.text?.isBlank() == true){
            etValueCredit.error = "Fill out this field"
            valid = false
        }
        if(etTax.text?.isBlank() == true){
            etTax.error = "Fill out this filed"
            valid = false
        }
        if(etMonths.text?.isBlank() == true){
            etTax.error = "Fill out this filed"
            valid = false
        }
        return valid
    }

     private fun onItemSelected() {
        spMonth.setOnItemClickListener{adapter,_,pos,_ ->
            val month = adapter.getItemAtPosition(pos)
            val value = NumbersUtil.toBigDecimal(etValueCredit      )
            val period = etMonths.text.toString().toLong()
            val tax = etTax.text.toString().toDouble()
            val response = calc.calc(value, period, tax)
            var totalValue = BigDecimal(0)

            val interest = calcQuoteInt.calc(value,period,tax,month.toString().toLong())
            response.let {

                tvCapitalValue.text = NumbersUtil.COPtoString(it.setScale(2, RoundingMode.HALF_EVEN))
                totalValue = totalValue.add(it)
            }
            interest.let {

                tvInterestValue.text = NumbersUtil.COPtoString(it.setScale(2, RoundingMode.HALF_EVEN))
                totalValue = totalValue.add(it)
            }
            if(totalValue> BigDecimal(0)){
                tvTotalValue.text = NumbersUtil.COPtoString(totalValue.setScale(2, RoundingMode.HALF_EVEN))
            }
        }
    }

    override fun lists(fn: ((QuoteCreditVariableHolder) -> Unit)?) {
        fn?.invoke(this)
        onItemSelected()
    }
}