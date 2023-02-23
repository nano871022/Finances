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
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class QuoteCreditVariableHolder(var container: View):IHolder<QuoteCreditCard>,AdapterView.OnItemSelectedListener, ISpinnerHolder<QuoteCreditVariableHolder> {
    private val calc: Calc = QuoteCreditVariable()
    private val calcQuoteInt: CalcInterest = QuoteCreditVariableInterestQuote()
    lateinit var listMonths:MutableList<String>

    private lateinit var etValueCredit: EditText
    private lateinit var etTax: EditText
    private lateinit var etMonths: EditText
    private lateinit var tvCapitalValue: TextView
    private lateinit var tvInterestValue: TextView
    private lateinit var tvTotalValue: TextView
    private lateinit var lyCapitalCredit: LinearLayout
    private lateinit var lyInterestCredit: LinearLayout
    private lateinit var lyMontlyPay: LinearLayout
    private lateinit var lyTotalValue: LinearLayout
     lateinit var spMonth: Spinner
    private lateinit var btnSave: Button

    private lateinit var quote:QuoteCreditCard

    override fun setFields(actions: View.OnClickListener?) {
        etValueCredit = container.findViewById(R.id.etNameItem)
        etTax = container.findViewById(R.id.etTax)!!
        etMonths = container.findViewById(R.id.etMonths)!!
        tvCapitalValue = container.findViewById(R.id.tvCapitalValue)!!
        lyCapitalCredit = container.findViewById(R.id.lyCapitalValue)!!
        tvInterestValue = container.findViewById(R.id.tvInterestValueList)!!
        tvTotalValue = container.findViewById(R.id.tvTotalValue)!!
        spMonth = container.findViewById(R.id.months)
        val btnClear:Button = container.findViewById(R.id.btnClear)
        btnClear.setOnClickListener(actions)
        val btnCalc:Button = container.findViewById(R.id.btnCalc)
        btnCalc.setOnClickListener(actions)
        btnSave = container.findViewById(R.id.btnSaveVariable)
        btnSave.setOnClickListener(actions)
        btnSave.visibility = View.INVISIBLE
        spMonth.onItemSelectedListener = this
        try{
            lyInterestCredit = container.findViewById(R.id.lyInterestValue)!!
            lyTotalValue = container.findViewById(R.id.lyTotalValue)!!
            lyMontlyPay = container.findViewById(R.id.lyMonthPay)

        }catch(e:Exception){

        }
        quote = QuoteCreditCard()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        values.capitalValue.ifPresent {
            tvCapitalValue.text = NumbersUtil.COPtoString(it)
            try {
                lyCapitalCredit.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
        }
        values.interestValue.ifPresent{
            tvInterestValue.text = NumbersUtil.COPtoString(it)
            try {
                lyInterestCredit.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
        }
        values.value.ifPresent{
            try {
                lyTotalValue.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            tvTotalValue.text = NumbersUtil.COPtoString(it)
        }
        btnSave.visibility = View.VISIBLE
        quote = values
        Log.d(this.javaClass.name,"Assign to quote ${quote.response} ${quote.capitalValue} ${quote.interestValue}")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        val quote = QuoteCreditCard()
        quote.tax = Optional.ofNullable(etTax.text.toString().toDouble())
        quote.value= Optional.ofNullable(etValueCredit.text.toString().toBigDecimal())
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
        etValueCredit.setBackgroundColor(Color.WHITE)
        etTax.setBackgroundColor(Color.WHITE)
        etMonths.setBackgroundColor(Color.WHITE)
        btnSave.visibility = View.INVISIBLE
        try{
            lyCapitalCredit.visibility = View.INVISIBLE
            lyInterestCredit.visibility = View.INVISIBLE
            lyTotalValue.visibility = View.INVISIBLE
            lyMontlyPay.visibility = View.INVISIBLE
        }catch (e:Exception){}
    }

    override fun validate(): Boolean {
        var valid:Boolean = true
        if(etValueCredit.text.isBlank()){
            etValueCredit.error = "Fill out this field"
            valid = valid && false
        }
        if(etTax.text.isBlank()){
            etTax.error = "Fill out this filed"
            valid = valid && false
        }
        if(etMonths.text.isBlank()){
            etTax.error = "Fill out this filed"
            valid = valid && false
        }
        return valid
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        print("In item selected")
        val format = DecimalFormat("#,###.00")
        val value = etValueCredit.text.toString().toBigDecimal()
        val period = etMonths.text.toString().toLong()
        val tax = etTax.text.toString().toDouble()
        val response = calc.calc(value, period, tax)
        var totalValue = BigDecimal(0)

        val option = listMonths.get(pos)
        val interest = calcQuoteInt.calc(value,period,tax,option.toLong())
        response.let {

            tvCapitalValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
            try {
                lyCapitalCredit.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            totalValue = totalValue.add(it)
        }
        interest.let {

            tvInterestValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
            try {
                lyInterestCredit.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            totalValue = totalValue.add(it)
        }
        if(totalValue.compareTo(BigDecimal(0))>0){
            try {
                lyTotalValue.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            tvTotalValue.text = format.format(totalValue.setScale(2, RoundingMode.HALF_EVEN))
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun lists(fn: ((QuoteCreditVariableHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}