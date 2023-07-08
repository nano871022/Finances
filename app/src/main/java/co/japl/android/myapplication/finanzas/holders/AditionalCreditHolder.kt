package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AdditionalCreditHolder(val view:View,val supportManager: FragmentManager):
    IHolder<AdditionalCreditDTO> {
    lateinit var name: TextInputEditText
    lateinit var value: TextInputEditText
    lateinit var startDate: TextInputEditText
    lateinit var endDate: TextInputEditText
    lateinit var cancel: MaterialButton
    lateinit var save: MaterialButton
    lateinit var additionalCredit:AdditionalCreditDTO

    private val validations by lazy {
        arrayOf(
            name set R.string.error_empty `when` { text().isEmpty()},
            value set R.string.error_empty `when` { text().isEmpty()},
            startDate set R.string.error_empty `when` { isNotLocalDate()},
            endDate set R.string.error_empty `when` { isNotLocalDate()}
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        name = view.findViewById(R.id.nameAC)
        value =  view.findViewById(R.id.valueAC)
        startDate =  view.findViewById(R.id.start_date_ac)
        endDate =  view.findViewById(R.id.end_date_ac)
        cancel =  view.findViewById(R.id.btn_cancel_ac)
        save =  view.findViewById(R.id.btn_save_ac)
        cancel.setOnClickListener(actions)
        save.setOnClickListener(actions)
        loadDates()
        loadMoney()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: AdditionalCreditDTO) {
        if(values.id > 0) {
            name.setText(values.name)
            value.setText(NumbersUtil.toString(values.value))
        }
        startDate.setText(DateUtils.localDateToString(values.startDate))
        endDate.setText(DateUtils.localDateToString(values.endDate))
        additionalCredit = values
    }

    private fun loadMoney(){
        val handler = Handler(Looper.getMainLooper())
        value.addTextChangedListener (object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    value.removeTextChangedListener(this)
                    value.setNumberToField()
                    value.addTextChangedListener (this)
                },1000)

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDates(){

        val dataPicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(R.string.date_bill)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val dataPicker2 = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(R.string.date_bill)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()


        startDate.setOnClickListener{
            dataPicker.show(supportManager,"DT_BILL")
            dataPicker.addOnPositiveButtonClickListener {
                var date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                this.startDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }

        endDate.setOnClickListener{
            dataPicker2.show(supportManager,"DT_BILL")
            dataPicker2.addOnPositiveButtonClickListener {
                var date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                this.endDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): AdditionalCreditDTO {
        val id = additionalCredit.id
        val codCredit = additionalCredit.creditCode
        val name = name.text.toString()
        val value = NumbersUtil.stringCOPToBigDecimal(value.text.toString())
        val start  = startDate.toLocalDate()!!
        val end = endDate.toLocalDate()!!
        return AdditionalCreditDTO(id,name,value,codCredit,start,end)
    }

    override fun cleanField() {
        name.text?.clear()
        value.text?.clear()
        startDate.text?.clear()
        endDate.text?.clear()
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{
            requestFocus()
        }.notNull{
            valid = true
        }
        return valid
    }
}