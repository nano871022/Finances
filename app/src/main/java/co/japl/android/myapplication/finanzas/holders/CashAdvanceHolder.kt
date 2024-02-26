package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBought
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CashAdvanceHolder(var view:View,val manager:FragmentManager, var caller: (value: BigDecimal,date:LocalDateTime) -> CreditCardBought) : IHolder<CreditCardBought>, View.OnClickListener{
    lateinit var creditCardName:TextView
    lateinit var date:TextInputEditText
    lateinit var productName: TextInputEditText
    lateinit var productValue: TextInputEditText
    lateinit var tax:TextView
    lateinit var month:TextView
    lateinit var quoteValue: TextView
    lateinit var save:Button
    lateinit var clean:Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        creditCardName = view.findViewById(R.id.etCreditCardAssignCACC)
        date = view.findViewById(R.id.dtBoughtCACC)
        productName  = view.findViewById(R.id.etNameItemCACC)
        productValue = view.findViewById(R.id.etProductValueCACC)
        tax = view.findViewById(R.id.etTaxBoughtCACC)
        month = view.findViewById(R.id.tvMontCACC)
        quoteValue = view.findViewById(R.id.etQuoteValueCACC)
        save = view.findViewById(R.id.btnSaveBoughtCACC)
        clean = view.findViewById(R.id.btnClearBoughtCACC)
        save.setOnClickListener(actions)
        clean.setOnClickListener(this)
        val datePicker = MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).setTitleText(view.resources.getString(R.string.bought_date_time)).build()
        date.setText(DateUtils.localDateTimeToString(LocalDateTime.now()))
        date.isFocusable = false
        date.setOnClickListener {
            datePicker.show(manager,"DT_BOUGHT")
        }
        datePicker.addOnPositiveButtonClickListener {
            val dateSelected = LocalDateTime.ofInstant(Instant.ofEpochMilli(it),ZoneId.systemDefault()).plusDays(1)
            date.setText(dateSelected.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        }

        productValue.addTextChangedListener (object :TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    productValue.removeTextChangedListener(this)
                    productValue.setText(NumbersUtil.toString(productValue))
                    if (validate()) {
                        caller.let {
                            val values = downLoadFields()
                            it.invoke(values.valueItem!!, values.boughtDate!!).apply {
                                loadFields(this).also {
                                    save.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    productValue.addTextChangedListener (this)
                }, 1000)
            }
        })

        save.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: CreditCardBought) {
        values.nameCreditCard?.let { creditCardName.text = it}
        values.month?.let { month.text = it.toString()}
        values.quoteValue?.let { quoteValue.text = NumbersUtil.COPtoString(it)}
        values.interest?.let { tax.text = "$it % ${values.kindOfTax}"}
        values.nameItem?.let { productName.setText(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardBought {
        val quote = CreditCardBought()
        quote.nameCreditCard = creditCardName.text.toString()
        quote.nameItem= productName.text.toString()
        quote.valueItem= NumbersUtil.toBigDecimal(productValue)
        quote.interest = getTax()
        quoteValue.text.toString().takeIf {
            it.isNotEmpty() && NumbersUtil.stringCOPToBigDecimal(it) > BigDecimal.ZERO
        }?.let {
            quote.quoteValue = NumbersUtil.stringCOPToBigDecimal(it!!)
        }
        month.text.toString().takeIf { it.isNotEmpty() }.apply { quote.month = this?.toInt() }
        quote.boughtDate = DateUtils.getLocalDateTimeByString(date)
        return quote
    }

    private fun getTax():Double{
        val value  = tax.text.toString()?.replace("%","")?.replace(Regex("[^\\d.]"),"")?.trim()
        if(value?.isNotBlank() == true){
            return value?.toDouble() ?: 0.0
        }
        return 0.0
    }

    override fun cleanField() {
        date.editableText.clear()
        productName.editableText.clear()
        productValue.editableText.clear()
        tax.text = view.resources.getText(R.string.interest_value_hint)
        month.text = view.resources.getText(R.string.months_number_hint)
        quoteValue.text = view.resources.getText(R.string.money_hint)
    }

    private val validations by lazy{
        arrayOf(
            date set R.string.date_is_not_setting `when` { text().isEmpty()},
            productName set R.string.name_product_is_not_setting `when` { text().isEmpty() },
            productValue set R.string.value_product_is_not_setting `when` { text().isEmpty() },
        )
    }
    override fun validate(): Boolean {
        var valid = true
        validations.firstInvalid{requestFocus()}.notNull { valid = true }
        return valid
    }

    override fun onClick(p0: View?) {
        cleanField()
    }
}