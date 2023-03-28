package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.util.*

class QuoteCreditSaveVariableHolder(var view:View):IHolder<QuoteCreditCard> {
     lateinit var etName: TextInputEditText
     lateinit var tvValueCredit: MaterialTextView
     lateinit var tvInterest: MaterialTextView
     lateinit var tvMonths: MaterialTextView
     lateinit var tvQuoteCredit: MaterialTextView
     lateinit var tvInterestValue: MaterialTextView
     lateinit var tvCapitalValue: MaterialTextView

    override fun setFields(actions: View.OnClickListener?) {
        etName = view.findViewById(R.id.etName)
        tvValueCredit = view.findViewById(R.id.tvValueCreditSave)
        tvInterest = view.findViewById(R.id.tvInterestSave)
        tvMonths = view.findViewById(R.id.tvMonthsSave)
        tvQuoteCredit = view.findViewById(R.id.tvQuoteValueSave)
        tvInterestValue = view.findViewById(R.id.tvInterestValueSaveVariable)
        tvCapitalValue = view.findViewById(R.id.tvCapitalValueSave)
        val btnSave: Button = view.findViewById(R.id.btnSave)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        btnSave.setOnClickListener(actions)
        btnCancel.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        tvQuoteCredit.text = NumbersUtil.COPtoString(values.response.orElse(BigDecimal.ZERO))
        tvInterest.text = "${values.tax.orElse(0.0).toString()} %"
        tvMonths.text = values.period.orElse(0).toString()
        tvValueCredit.text = NumbersUtil.COPtoString(values.value.orElse(BigDecimal.ZERO))
        tvInterestValue.text = NumbersUtil.COPtoString(values.interestValue.orElse(BigDecimal.ZERO))
        tvCapitalValue.text = NumbersUtil.COPtoString(values.capitalValue.orElse(BigDecimal.ZERO))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        val quote = QuoteCreditCard()
        quote.name = Optional.ofNullable(etName.text.toString())
        quote.value = Optional.ofNullable(NumbersUtil.toBigDecimal(tvValueCredit))
        quote.tax = Optional.ofNullable(NumbersUtil.toDouble(tvInterest))
        quote.period = Optional.ofNullable(tvMonths.text.toString().toLong())
        quote.response = Optional.ofNullable(NumbersUtil.toBigDecimal(tvQuoteCredit))
        quote.type = CalcEnum.VARIABLE
        quote.interestValue = Optional.ofNullable(NumbersUtil.toBigDecimal(tvInterestValue))
        quote.capitalValue = Optional.ofNullable(NumbersUtil.toBigDecimal(tvCapitalValue))
        quote.kindOfTax = Optional.of(KindOfTaxEnum.EM.name)
        return quote
    }

    override fun cleanField() {
        etName.text?.clear()
    }

    override fun validate(): Boolean {
        var valid = true
        if(etName.text?.isBlank() == true){
            etName.error = "Fill out the field"
            valid = false
        }
        return valid
    }
}