package co.japl.android.myapplication.finanzas.holders

import android.app.AlertDialog
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.holders.validations.COPtoBigDecimal
import co.japl.android.myapplication.finanzas.holders.validations.toLocalDate
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CreditFixHolder(val view:View,val supportManager: FragmentManager): IHolder<CreditDTO> {
    lateinit var quoteCreditSvc:Calc
    lateinit var name: TextInputEditText
    lateinit var date: TextInputEditText
    lateinit var value: TextInputEditText
    lateinit var tax: TextInputEditText
    lateinit var kindOfTax: TextInputEditText
    lateinit var period: TextInputEditText
    lateinit var quote: TextView
    lateinit var kindOf: TextInputEditText
    lateinit var amortization: MaterialButton
    lateinit var additional: MaterialButton
    lateinit var cancel: MaterialButton
    lateinit var save: MaterialButton
    lateinit var dialog:AlertDialog
    lateinit var kindOfTaxDialog:AlertDialog
    private val delay = 600L
    private val delayUpdate = 1000L

    private val validations  by lazy{
        arrayOf(
            name set R.string.error_empty `when` { text().isEmpty() },
            date set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
            tax set R.string.error_empty `when` { text().isEmpty() },
            period set R.string.error_empty `when` { text().isEmpty() },
            kindOf set R.string.error_empty `when` { text().isEmpty() },
            kindOfTax set R.string.error_empty `when` { text().isEmpty() }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        quoteCreditSvc = QuoteCredit()
        name = view.findViewById(R.id.NameCFF)
        value = view.findViewById(R.id.valueCFF)
        tax = view.findViewById(R.id.taxCFF)
        date = view.findViewById(R.id.dateCFF)
        period = view.findViewById(R.id.periodsCFF)
        quote = view.findViewById(R.id.quoteValueCFF)
        kindOf = view.findViewById(R.id.kindOfCFF)
        amortization = view.findViewById(R.id.btnAmortizationCFF)
        additional = view.findViewById(R.id.btnAdditionalCFF)
        cancel = view.findViewById(R.id.btnCancelCFF)
        save = view.findViewById(R.id.btn_save_cff)
        kindOfTax = view.findViewById(R.id.kind_of_tax_CFF)

        amortization.setOnClickListener(actions)
        additional.setOnClickListener(actions)
        cancel.setOnClickListener(actions)
        save.setOnClickListener(actions)

        save.visibility = View.GONE

        additional.visibility = View.GONE
        amortization.visibility = View.GONE
        moneyFormat()
        kindOfDialog()
        date()
    }

    private fun moneyFormat(){
        value.addTextChangedListener(object:TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    value.removeTextChangedListener(this)
                    if(value.text?.isNotBlank() == true){
                        value.setText(NumbersUtil.toString(value))
                    }
                    calc()
                    value.addTextChangedListener (this)
                },delayUpdate)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun date(){
        date.setText(DateUtils.localDateToString(LocalDate.now()))
        val dataPicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(R.string.date_bill)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()


        date.setOnClickListener{
            dataPicker.show(supportManager,"DT_BILL")
            dataPicker.addOnPositiveButtonClickListener {
                var date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it),ZoneId.systemDefault())
                this.date.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }
    }



    private fun kindOfDialog(){
       val builder = AlertDialog.Builder(view.context)
        with(builder){
            val items = view.resources.getStringArray(R.array.kind_of_pay_list)
            setItems(items){ _, position ->
                val value = items[position]
                kindOf.setText(value)
            }
        }
        dialog = builder.create()

        val builderKindOf = AlertDialog.Builder(view.context)
        with(builderKindOf){
            val items = view.resources.getStringArray(R.array.kind_of_tax_list)
            setItems(items){ _,position ->
                val kindOf = items[position]
                kindOfTax.setText(kindOf)
            }
        }

        kindOfTaxDialog = builderKindOf.create()

        onClick()
        onFocus()
    }
    private fun onClick(){
        kindOfTax.setOnClickListener{kindOfTaxDialog.show()}
        kindOf.setOnClickListener{dialog.show()}
    }

    private fun onFocus(){
        val handlerTax = Handler(Looper.getMainLooper())
        tax.addTextChangedListener {
            handlerTax.removeCallbacksAndMessages(null)
            handlerTax.postDelayed({
                calc()
            },delay)
        }
        val handlerPeriod = Handler(Looper.getMainLooper())
        period.addTextChangedListener {
            handlerPeriod.removeCallbacksAndMessages(null)
            handlerPeriod.postDelayed({
                calc()
            },delay)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: CreditDTO) {
        if(values.id > 0) {
            name.setText(values.name)
            value.setText(NumbersUtil.COPtoString(values.value))
            tax.setText(values.tax.toString())
            period.setText(values.periods)
            quote.text = NumbersUtil.COPtoString(values.quoteValue)
            kindOf.setText(values.kindOf)
            date.setText(DateUtils.localDateToString(values.date))
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditDTO {
        val id = 0
        val name = name.text.toString()
        val value = value.COPtoBigDecimal()
        val tax = tax.text.toString().toDouble()
        val period = period.text.toString().toInt()
        val quote = NumbersUtil.stringCOPToBigDecimal(quote.text.toString())
        val kindOf = kindOf.text.toString()
        val date = date.toLocalDate()
        Log.d(javaClass.name,"Date $date")
        val kindOfTax = kindOfTax.text.toString()
        return CreditDTO(id,name,date!!,tax,period,value,quote,kindOf,kindOfTax)
    }

    override fun cleanField() {
        name.text?.clear()
        value.text?.clear()
        tax.text?.clear()
        period.text?.clear()
        quote.text = ""
        date.text?.clear()
        kindOf.text?.clear()
    }


    fun calc(){
        if(validate()){
            val tax = tax.text.toString().toDouble()
            val period = period.text.toString().toLong()
            val value = NumbersUtil.toBigDecimal(value)
            val kindOf = kindOfTax.text.toString()
            val quote = quoteCreditSvc.calc(value,period,tax, KindOfTaxEnum.valueOf(kindOf))
            this.quote.text = NumbersUtil.COPtoString(quote)
            amortization.visibility = View.VISIBLE
            save.visibility = View.VISIBLE
        }else{
            amortization.visibility = View.GONE
            save.visibility = View.GONE
        }
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid
    }
}