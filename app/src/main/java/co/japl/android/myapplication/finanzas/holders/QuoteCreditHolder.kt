package co.japl.android.myapplication.finanzas.holders

import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class QuoteCreditHolder(var container:View): IHolder<QuoteCreditCard> {
    lateinit var etValueCredit: TextInputEditText
    lateinit var etTax: TextInputEditText
    lateinit var etMonths: TextInputEditText
    lateinit var tvQuoteValue: MaterialTextView
    lateinit var lyQuoteCredit: LinearLayout
    lateinit var btnSave: Button
    lateinit var btnAmortization: Button
    lateinit var response:Optional<BigDecimal>

    override fun setFields(actions: View.OnClickListener?) {
        etValueCredit = container.findViewById<TextInputEditText>(R.id.etNameItem)
        etTax = container.findViewById(R.id.etTax)!!
        etMonths = container.findViewById(R.id.etMonths)!!
        tvQuoteValue = container.findViewById(R.id.tvQuoteValue)!!
        lyQuoteCredit = container.findViewById(R.id.lyInterestValue)!!
        btnAmortization = container.findViewById(R.id.btnAmortizationQCF)
        btnSave  = container.findViewById(R.id.btnSave)
        etValueCredit.setOnFocusChangeListener{ _,focus->
            if(!focus && etValueCredit.text?.isNotBlank() == true){
                etValueCredit.setText(NumbersUtil.toString(etValueCredit))
            }
        }
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2

        val btnClear:Button = container.findViewById(R.id.btnClear)
        btnClear.setOnClickListener(actions)
        val btnCalc:Button = container.findViewById(R.id.btnCalc)
        btnCalc.setOnClickListener(actions)
        btnSave.setOnClickListener(actions)
        btnAmortization.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        tvQuoteValue.text = NumbersUtil.COPtoString(values.response.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN))
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
        if(this::response.isInitialized){
            pojo.response = response
            pojo.interestValue = Optional.ofNullable(pojo.value.orElse(BigDecimal.ZERO) * (pojo.tax.orElse(0.0) / 100).toBigDecimal())
            pojo.capitalValue = Optional.of(pojo.response.orElse(BigDecimal.ZERO) - pojo.interestValue.orElse(BigDecimal.ZERO))
        }
2000
        return pojo
    }

    override fun cleanField() {
        etValueCredit.editableText.clear();
        etTax.editableText.clear()
        etMonths.editableText.clear()
        //tvQuoteValue.editableText.clear()
        lyQuoteCredit.visibility = View.INVISIBLE
        btnSave.visibility = View.INVISIBLE
        btnAmortization.visibility = View.INVISIBLE
    }

    override fun validate(): Boolean {
        var valid:Boolean = true
        if(etValueCredit.text?.isBlank() == true){
            etValueCredit.error = "Debe contener un valor mayor a 1"
            valid = false
        }
        if(etTax.text?.isBlank() == true){
            etTax.error = "Debe contener un valo entre 0.001 y 99.9"
            valid = false
        }
        if(etMonths.text?.isBlank() == true){
            etMonths.error = "Debe contener un valor entre 1 a 200."
            valid = false
        }
        return valid
    }
}