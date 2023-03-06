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
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.util.*

class QuoteCreditSaveVariableHolder(var view:View):IHolder<QuoteCreditCard> {
     lateinit var etName: EditText
     lateinit var tvValueCredit: TextView
     lateinit var tvInterest: TextView
     lateinit var tvMonths: TextView
     lateinit var tvQuoteCredit: TextView
     lateinit var tvInterestValue: TextView
     lateinit var tvCapitalValue: TextView

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
        tvInterest.text = values.tax.orElse(0.0).toString()
        tvMonths.text = values.period.orElse(0).toString()
        tvValueCredit.text = NumbersUtil.COPtoString(values.value.orElse(BigDecimal.ZERO))
        tvInterestValue.text = NumbersUtil.COPtoString(values.interestValue.orElse(BigDecimal.ZERO))
        tvCapitalValue.text = NumbersUtil.COPtoString(values.capitalValue.orElse(BigDecimal.ZERO))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        val quote = QuoteCreditCard()
        quote.name = Optional.ofNullable(etName.text.toString())
        quote.value = Optional.ofNullable(tvValueCredit.text.toString().replace("$","").replace(",","").trim().toBigDecimal())
        quote.tax = Optional.ofNullable(tvInterest.text.toString().replace(",","").trim().toDouble())
        quote.period = Optional.ofNullable(tvMonths.text.toString().toLong())
        quote.response = Optional.ofNullable(tvQuoteCredit.text.toString().replace("$","").replace(",","").trim().toBigDecimal())
        quote.type = CalcEnum.VARIABLE
        quote.interestValue = Optional.ofNullable(tvInterestValue.text.toString().replace("$","").replace(",","").trim().toBigDecimal())
        quote.capitalValue = Optional.ofNullable(tvCapitalValue.text.toString().replace("$","").replace(",","").trim().toBigDecimal())
        return quote
    }

    override fun cleanField() {
        etName.text.clear()
    }

    override fun validate(): Boolean {
        var valid = true
        if(etName.text.isBlank()){
            etName.error = "Fill out the field"
            valid = false
        }
        return valid
    }
}