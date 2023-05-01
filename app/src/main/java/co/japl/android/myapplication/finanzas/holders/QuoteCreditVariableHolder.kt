package co.japl.android.myapplication.finanzas.holders

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterestQuote
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.CalcInterest
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.stream.IntStream
import kotlin.streams.toList

class QuoteCreditVariableHolder(var container: View): IHolder<QuoteCreditCard>,OnClickListener{
    private val calc: Calc = QuoteCreditVariable()
    private val calcQuoteInt: CalcInterest = QuoteCreditVariableInterestQuote()
    lateinit var listMonths:MutableList<String>
    private lateinit var dialog:AlertDialog

    private lateinit var etValueCredit: TextInputEditText
    private lateinit var etTax: TextInputEditText
    private lateinit var etMonths: TextInputEditText
    private lateinit var kindOfTax: TextInputEditText
    private lateinit var tvCapitalValue: MaterialTextView
    private lateinit var tvInterestValue: MaterialTextView
    private lateinit var tvTotalValue: MaterialTextView
    private lateinit var llCalculation: LinearLayout
     lateinit var spMonth: MaterialAutoCompleteTextView
     private lateinit var kindOfTaxDialog:AlertDialog
    private lateinit var btnSave: Button
    private lateinit var btnAmortization: Button

    private lateinit var quote:QuoteCreditCard
    private var months:Array<Int> = arrayOf(1)

    override fun setFields(actions: View.OnClickListener?) {
        etValueCredit = container.findViewById(R.id.etNameItem)
        etTax = container.findViewById(R.id.etTax)!!
        etMonths = container.findViewById(R.id.etMonths)!!
        tvCapitalValue = container.findViewById(R.id.tvCapitalValue)!!
        tvInterestValue = container.findViewById(R.id.tvInterestValueList)!!
        llCalculation = container.findViewById(R.id.llCalculationQCCV)
        tvTotalValue = container.findViewById(R.id.tvTotalValue)!!
        kindOfTax = container.findViewById(R.id.et_kind_of_tax_qcv)
        spMonth = container.findViewById(R.id.months)
        btnAmortization = container.findViewById(R.id.btnAmortizationQCV)
        val btnClear:Button = container.findViewById(R.id.btnClear)
        btnClear.setOnClickListener(actions)
        val btnCalc:Button = container.findViewById(R.id.btnCalc)
        btnCalc.setOnClickListener(actions)
        btnSave = container.findViewById(R.id.btnSaveVariable)
        btnSave.setOnClickListener(actions)
        btnSave.visibility = View.INVISIBLE
        spMonth.isFocusable = false
        quote = QuoteCreditCard()
        llCalculation.visibility=View.INVISIBLE
        etValueCredit.setOnFocusChangeListener{ _,focus->
            if(!focus && etValueCredit.text?.isNotBlank() == true){
                etValueCredit.setText(NumbersUtil.toString(etValueCredit))
            }
        }
        btnAmortization.setOnClickListener(actions)
        spMonth.isFocusable = false
        onClick()
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
        values.kindOfTax.ifPresent{
            kindOfTax.setText(it)
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
        quote.capitalValue = Optional.ofNullable(capital)
        quote.interestValue = Optional.ofNullable(interest)
        quote.response = Optional.ofNullable(total)
        quote.kindOfTax = Optional.of(kindOfTax.text.toString())
        months = IntStream.range(1,quote.period.orElse(0).toInt() + 1).toList().toTypedArray()
        return quote
    }

    override fun cleanField() {
        etValueCredit.editableText.clear();
        etTax.editableText.clear()
        etMonths.editableText.clear()
        btnSave.visibility = View.INVISIBLE
        kindOfTax.editableText.clear()
        llCalculation.visibility = View.INVISIBLE
        btnAmortization.visibility = View.INVISIBLE
    }

    private val validations  by lazy{
        arrayOf(
            etValueCredit set R.string.error_empty `when` { text().isEmpty() },
            etTax set R.string.error_empty `when` { text().isEmpty() },
            etMonths set R.string.error_empty `when` { text().isEmpty() },
            kindOfTax set R.string.error_empty `when` { text().isEmpty() }
        )
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid
    }

     private fun onClick() {
         createDialog()
         createDialogTax()
        spMonth.setOnClickListener(this)
         kindOfTax.setOnClickListener(this)
    }

    private fun createDialog(){
        val builder = AlertDialog.Builder(container?.context)
        with(builder){
            setItems(months.map{"$it"}.toTypedArray()){ _,position->
                val month = months[position].toInt()
                spMonth.setText(month.toString())
                val value = NumbersUtil.toBigDecimal(etValueCredit      )
                val period = etMonths.text.toString().toLong()
                val tax = etTax.text.toString().toDouble()
                val response = calc.calc(value, period, tax, KindOfTaxEnum.EM)
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
        dialog = builder.create()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.months->dialog.show()
            R.id.et_kind_of_tax_qcv->kindOfTaxDialog.show()
        }
    }

    private fun createDialogTax(){
        val builderKindOf = AlertDialog.Builder(container.context)
        with(builderKindOf){
            val items = container.resources.getStringArray(R.array.kind_of_tax_list)
            setItems(items){ _,position ->
                val kindOf = items[position]
                kindOfTax.setText(kindOf)
            }
        }
        kindOfTaxDialog = builderKindOf.create()
    }

}