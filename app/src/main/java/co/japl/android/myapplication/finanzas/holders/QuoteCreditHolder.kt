package co.japl.android.myapplication.finanzas.holders

import android.app.AlertDialog
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import co.japl.android.myapplication.finanzas.enums.KindInitialQuoteEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.gms.common.util.NumberUtils
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

class QuoteCreditHolder(var container:View): IHolder<QuoteCreditCard> {
    lateinit var etValueCredit: TextInputEditText
    lateinit var etTax: TextInputEditText
    lateinit var etMonths: TextInputEditText
    lateinit var tvQuoteValue: TextView
    lateinit var lyQuoteCredit: LinearLayout
    lateinit var kindOfTax:TextInputEditText
    lateinit var btnSave: Button
    lateinit var btnAmortization: Button
    lateinit var response:Optional<BigDecimal>
    lateinit var kindOfTaxDialog:AlertDialog
    lateinit var kindInitialQuoteDialog:AlertDialog
    var initialQuote:TextInputEditText? = null
    var kindInitialQuote:TextInputEditText? = null
    var valueInitialQuote:TextView? = null
    var totalCreditValue:TextView? = null

    override fun setFields(actions: View.OnClickListener?) {
        etValueCredit = container.findViewById<TextInputEditText>(R.id.etNameItem)
        etTax = container.findViewById(R.id.etTax)!!
        etMonths = container.findViewById(R.id.etMonths)!!
        tvQuoteValue = container.findViewById(R.id.tvQuoteValue)!!
        lyQuoteCredit = container.findViewById(R.id.lyInterestValue)!!
        btnAmortization = container.findViewById(R.id.btnAmortizationQCF)
        kindOfTax = container.findViewById(R.id.etKindOfTax)
        btnSave  = container.findViewById(R.id.btnSave)
        initialQuote = container.findViewById(R.id.et_initial_quote_sqc)
        kindInitialQuote = container.findViewById(R.id.et_kind_initial_quote_sqc)
        valueInitialQuote = container.findViewById(R.id.tv_value_quote_initial_sqc)
        totalCreditValue = container.findViewById(R.id.tv_total_credit_sqc)
        changeListener()
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2

        val btnClear:Button = container.findViewById(R.id.btnClear)
        btnClear.setOnClickListener(actions)
        val btnCalc:Button = container.findViewById(R.id.btnCalc)
        btnCalc.setOnClickListener(actions)
        btnSave.setOnClickListener(actions)
        btnAmortization.setOnClickListener(actions)
        createDialogTax()
        createDialogKindInitial()
        kindOfTax.setOnClickListener { kindOfTaxDialog.show() }
        kindInitialQuote?.let{
            it.setText(KindInitialQuoteEnum.AMOUNT.symbole)
            it.setOnClickListener{kindInitialQuoteDialog.show()}
        }
        initialQuote?.let {
            it.setText(NumbersUtil.toString(BigDecimal.ZERO))
        }
        btnSave.visibility = View.INVISIBLE
    }

    private fun changeListener(){
        etValueCredit.addTextChangedListener(object:TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    etValueCredit.removeTextChangedListener(this)
                    if(etValueCredit.text?.isNotBlank() == true){
                        etValueCredit.setText(NumbersUtil.toString(etValueCredit))
                    }
                    etValueCredit.addTextChangedListener(this)
                },1000)
            }
        })
        initialQuote?.let{it.addTextChangedListener(object:TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                kindInitialQuote?.let{ kind->
                    if(kind.text?.isNotBlank() == true && kind.text.toString() == KindInitialQuoteEnum.AMOUNT.symbole){
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    it.removeTextChangedListener(this)
                    if(it.text?.isNotBlank() == true){
                        it.setText(NumbersUtil.toString(it))
                    }
                    it.addTextChangedListener(this)
                },1000)
                }
            }}
        })}
    }

    private fun createDialogTax(){
        val builderKindOf = AlertDialog.Builder(container.context)
        with(builderKindOf){
            setTitle(R.string.kind_of_tax)
            val items = container.resources.getStringArray(R.array.kind_of_tax_list)
            setItems(items){ _,position ->
                val kindOf = items[position]
                kindOfTax.setText(kindOf)
            }
        }
        kindOfTaxDialog = builderKindOf.create()
    }

    private fun createDialogKindInitial(){
        val builderKindOf = AlertDialog.Builder(container.context)
        with(builderKindOf){
            setTitle(R.string.kind_initial_quote)
            val items = KindInitialQuoteEnum.values().map { it.symbole }.toTypedArray()
            setItems(items){ _,position ->
                val kindOf = items[position]
                kindInitialQuote?.setText(kindOf)
            }
        }
        kindInitialQuoteDialog = builderKindOf.create()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        tvQuoteValue.text = NumbersUtil.COPtoString(values.response.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN))
        valueInitialQuote?.text = NumbersUtil.toString(values.initialValue?:BigDecimal.ZERO)
        totalCreditValue?.text = NumbersUtil.toString(values.value.orElse(BigDecimal.ZERO))
        kindInitialQuote?.setText(values.kindOfTax.orElse(KindInitialQuoteEnum.AMOUNT.symbole))
        lyQuoteCredit.visibility = View.VISIBLE
        btnSave.visibility = View.VISIBLE
        btnAmortization.visibility = View.VISIBLE
        response = values.response
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        val pojo = QuoteCreditCard()
        pojo.name = Optional.of("unknown")
        pojo.value = Optional.ofNullable(NumbersUtil.toBigDecimal(etValueCredit))
        pojo.period = Optional.ofNullable(etMonths.text.toString().toLong())
        pojo.tax = Optional.ofNullable(etTax.text.toString().toDouble())
        pojo.type = CalcEnum.FIX
        pojo.kindOfTax = Optional.of(kindOfTax.text.toString())
        val kind = kindInitialQuote?.let{
            it.text.toString()
        }?: KindInitialQuoteEnum.AMOUNT.symbole
        initialQuote?.let {
            if(kind == KindInitialQuoteEnum.AMOUNT.symbole){
                pojo.initialValue = NumbersUtil.toBigDecimal(it)
            }else{
                pojo.initialValue = pojo.value.get() * (it.text.toString().toDouble() / 100).toBigDecimal()
            }
        }
        initialQuote?.takeIf { it.text.toString().toBigDecimal() > BigDecimal.ZERO }?.let{
            pojo.value = Optional.ofNullable(pojo.value.orElse(BigDecimal.ZERO) - (pojo?.initialValue?:BigDecimal.ZERO))
        }

        if(this::response.isInitialized){
            pojo.response = response
            pojo.interestValue = Optional.ofNullable(pojo.value.orElse(BigDecimal.ZERO) * (pojo.tax.orElse(0.0) / 100).toBigDecimal())
            pojo.capitalValue = Optional.of(pojo.response.orElse(BigDecimal.ZERO) - pojo.interestValue.orElse(BigDecimal.ZERO))
        }
        return pojo
    }

    override fun cleanField() {
        etValueCredit.editableText.clear();
        etTax.editableText.clear()
        etMonths.editableText.clear()
        lyQuoteCredit.visibility = View.INVISIBLE
        kindOfTax.editableText.clear()
        btnSave.visibility = View.INVISIBLE
        btnAmortization.visibility = View.INVISIBLE
        kindInitialQuote?.setText(KindInitialQuoteEnum.AMOUNT.symbole)
        initialQuote?.setText(NumbersUtil.toString(BigDecimal.ZERO))
    }

    private val validations  by lazy{
        arrayOf(
            etValueCredit set R.string.error_empty `when` { text().isEmpty() },
            etTax set R.string.error_empty `when` { text().isEmpty() },
            etMonths set R.string.error_empty `when` { text().isEmpty() },
            kindOfTax set R.string.error_empty `when` { text().isEmpty() }
            ,kindInitialQuote!! set R.string.error_empty `when` { text().isEmpty() }
            , initialQuote!! set R.string.error_empty `when` { text().isEmpty() }
        )
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid.also { if(it) btnSave.visibility = View.VISIBLE }
    }
}