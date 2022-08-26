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
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBought
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.ICallerHolder
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDateTime

class CashAdvanceHolder(var view:View, var caller: (value: BigDecimal,date:LocalDateTime) -> CreditCardBought) : IHolder<CreditCardBought>, View.OnClickListener, View.OnFocusChangeListener{
    lateinit var creditCardName:TextView
    lateinit var date:EditText
    lateinit var productName: EditText
    lateinit var productValue: EditText
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

        date.setText(DateUtils.localDateTimeToString(LocalDateTime.now()))

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
        quote.valueItem=productValue.text.toString().toBigDecimal()
        tax.text.toString().takeIf { it.isNotEmpty() }.apply { quote.interest = this?.toDouble() }
        quoteValue.text.toString().takeIf {
            it.isNotEmpty() && NumbersUtil.stringCOPToBigDecimal(it) > BigDecimal.ZERO
        }?.let {
            Log.d("cashAdvanceSave","value to quoteValue $it")
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
    override fun onFocusChange(p0: View?, p1: Boolean) {
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