package co.japl.android.myapplication.finanzas.holders

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class QuoteCreditHolder(var container:View): IHolder<QuoteCreditCard> {
    lateinit var etValueCredit: EditText
    lateinit var etTax: EditText
    lateinit var etMonths: EditText
    lateinit var tvQuoteValue: TextView
    lateinit var lyQuoteCredit: LinearLayout
    lateinit var btnSave: Button
    lateinit var response:Optional<BigDecimal>

    override fun setFields(actions: View.OnClickListener?) {
        etValueCredit = container.findViewById(R.id.etNameItem)
        etTax = container.findViewById(R.id.etTax)!!
        etMonths = container.findViewById(R.id.etMonths)!!
        tvQuoteValue = container.findViewById(R.id.tvQuoteValue)!!
        lyQuoteCredit = container.findViewById(R.id.lyInterestValue)!!
        btnSave  = container.findViewById(R.id.btnSave)

        val btnClear:Button = container.findViewById(R.id.btnClear)
        btnClear.setOnClickListener(actions)
        val btnCalc:Button = container.findViewById(R.id.btnCalc)
        btnCalc.setOnClickListener(actions)
        btnSave.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        val format = DecimalFormat("#,###.00")
        tvQuoteValue.text = format.format(values.response.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN))
        lyQuoteCredit.visibility = View.VISIBLE
        btnSave.visibility = View.VISIBLE
        response = values.response
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        val pojo = QuoteCreditCard()
        pojo.value = Optional.ofNullable(etValueCredit.text.toString().toBigDecimal())
        pojo.period = Optional.ofNullable(etMonths.text.toString().toLong())
        pojo.tax = Optional.ofNullable(etTax.text.toString().toDouble())
        if(this::response.isInitialized){
            pojo.response = response
        }
        return pojo
    }

    override fun cleanField() {
        etValueCredit.editableText.clear();
        etTax.editableText.clear()
        etMonths.editableText.clear()
        //tvQuoteValue.editableText.clear()
        lyQuoteCredit.visibility = View.INVISIBLE
        etValueCredit.setBackgroundColor(Color.WHITE)
        etTax.setBackgroundColor(Color.WHITE)
        etMonths.setBackgroundColor(Color.WHITE)
        btnSave.visibility = View.INVISIBLE
    }

    override fun validate(): Boolean {
        var valid:Boolean = true
        if(etValueCredit.text.isBlank()){
            etValueCredit.error = "Debe contener un valor mayor a 1"
            valid = valid && false
        }
        if(etTax.text.isBlank()){
            etTax.error = "Debe contener un valo entre 0.001 y 99.9"
            valid = valid && false
        }
        if(etMonths.text.isBlank()){
            etMonths.error = "Debe contener un valor entre 1 a 200."
            valid = valid && false
        }
        return valid
    }
}