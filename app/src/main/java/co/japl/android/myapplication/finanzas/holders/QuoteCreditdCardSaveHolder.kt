package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

class QuoteCreditdCardSaveHolder(var view:View): IHolder<QuoteCreditCard> {
    lateinit var etName: TextInputEditText
    lateinit var tvValueCredit: TextView
    lateinit var tvInterest: TextView
    lateinit var tvMonths: TextView
    lateinit var tvQuoteCredit: TextView
    lateinit var quoteCreditCard:QuoteCreditCard


    override fun setFields(actions: View.OnClickListener?) {
        etName = view.findViewById(R.id.etName)
        tvValueCredit = view.findViewById(R.id.tvValueCreditSave)
        tvInterest = view.findViewById(R.id.tvInterestSave)
        tvMonths = view.findViewById(R.id.tvMonthsSave)
        tvQuoteCredit = view.findViewById(R.id.tvQuoteValueSave)
        val btnSave: Button = view.findViewById(R.id.btnSave)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        btnSave.setOnClickListener(actions)
        btnCancel.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: QuoteCreditCard) {
        val format = DecimalFormat(" $ #,###.##")
        val quoteCredit: BigDecimal = values.value.orElse(BigDecimal.ZERO)
        tvQuoteCredit.text = format.format(quoteCredit)
        val interest:Double =values.tax.orElse(0.0)
        tvInterest.text = " ${interest.toString()} % ${values.kindOfTax.get()}"
        val period:Long = values.period.orElse(0)
        tvMonths.text = period.toString()
        val valueCredit: BigDecimal = values.response.orElse(BigDecimal.ZERO)
        tvValueCredit.text = format.format(valueCredit)
        quoteCreditCard = values
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun downLoadFields(): QuoteCreditCard {
        quoteCreditCard.name = Optional.ofNullable(etName.text.toString())
        return quoteCreditCard
    }

    override fun cleanField() {
        TODO("Not yet implemented")
    }

    override fun validate(): Boolean {
        if(etName.text?.isBlank() == true){
            etName.error = "Debe contener un nombre para ser guardado."
            return false
        }
        return true
    }
}