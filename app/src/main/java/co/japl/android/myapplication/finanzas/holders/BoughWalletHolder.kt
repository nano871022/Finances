package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.arch.core.util.Function
import androidx.core.util.Predicate
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBought
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.ICallerHolder
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class BoughWalletHolder(var view:View,val manager:FragmentManager, var caller: (value: BigDecimal, date:LocalDateTime) -> CreditCardBought) : IHolder<CreditCardBought>, View.OnClickListener, View.OnFocusChangeListener{
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
        creditCardName = view.findViewById(R.id.etCreditCardAssignBWCC)
        date = view.findViewById(R.id.dtBoughtBWCC)
        productName  = view.findViewById(R.id.etNameItemBWCC)
        productValue = view.findViewById(R.id.etProductValueBWCC)
        tax = view.findViewById(R.id.etTaxBoughtBWCC)
        month = view.findViewById(R.id.tvMontBWCC)
        quoteValue = view.findViewById(R.id.etQuoteValueBWCC)
        save = view.findViewById(R.id.btnSaveBoughtBWCC)
        clean = view.findViewById(R.id.btnClearBoughtBWCC)
        save.setOnClickListener(actions)
        clean.setOnClickListener(this)
        val datePicker = MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).setTitleText(view.resources.getString(R.string.bought_date_time)).build()
        date.setText(DateUtils.localDateTimeToString(LocalDateTime.now()))
        date.isFocusable = false
        date.isClickable = true
        date.setOnClickListener {
            datePicker.show(manager,"Bought_date")
        }

        datePicker.addOnPositiveButtonClickListener {
            val dateSelected = LocalDateTime.ofInstant(Instant.ofEpochMilli(it),ZoneId.systemDefault()).plusDays(1)
            date.setText(dateSelected.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        }

        productName.onFocusChangeListener = this
        productValue.onFocusChangeListener = this

        save.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: CreditCardBought) {
        values.nameCreditCard?.let { creditCardName.text = it}
        values.month?.let { month.text = it.toString()}
        values.quoteValue?.let { quoteValue.text = NumbersUtil.COPtoString(it)}
        values.interest?.let { tax.text = it.toString()}
        values.nameItem?.let { productName.setText(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardBought {
        val quote = CreditCardBought()
        quote.nameCreditCard = creditCardName.text.toString()
        quote.nameItem= productName.text.toString()
        quote.valueItem= NumbersUtil.toBigDecimal(productValue)
        tax.text.toString().takeIf { it.isNotEmpty() }.apply { quote.interest = this?.toDouble() }
        quoteValue.text.toString().takeIf {
            it.isNotEmpty() && NumbersUtil.stringCOPToBigDecimal(it) > BigDecimal.ZERO
        }?.let {
            Log.d(this.javaClass.name,"value to quoteValue $it")
            quote.quoteValue = NumbersUtil.stringCOPToBigDecimal(it!!)
        }
        month.text.toString().takeIf { it.isNotEmpty() }.apply { quote.month = this?.toInt() }
        quote.boughtDate = DateUtils.getLocalDateTimeByString(date)
        return quote
    }

    override fun cleanField() {
        date.editableText.clear()
        productName.editableText.clear()
        productValue.editableText.clear()
        tax.text = view.resources.getText(R.string.interest_value_hint)
        month.text = view.resources.getText(R.string.months_number_hint)
        quoteValue.text = view.resources.getText(R.string.money_hint)
    }

    override fun validate(): Boolean {
        var valid = true
        if(date.editableText.isEmpty()){
            date.error = "La fecha debe estar configurada"
            valid = false
        }
        if(productValue.editableText.isEmpty()){
            productValue.error = "El valor del producto  de estar configurado"
            valid = false
        }
        if(productName.editableText.isEmpty()){
            productName.error = "El nombre del producto debe estar codnifgurado"
            valid = false
        }
        return valid
    }

    override fun onClick(p0: View?) {
        cleanField()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFocusChange(view: View?, focus: Boolean) {
        if(!focus) {
            when (view?.id) {
                R.id.etProductValueBWCC -> productValue.setText(NumbersUtil.toString(productValue))
            }
        }

        if(validate()){
            caller.let{
                val values = downLoadFields()
                it.invoke(values.valueItem!!,values.boughtDate!!).apply {
                    loadFields(this).also {
                        save.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}