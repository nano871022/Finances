package co.japl.android.myapplication.finanzas.holders
import android.app.AlertDialog
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class InputHolder(val view:View,val supportManager: FragmentManager): IHolder<InputDTO> {
    private lateinit var dialog:AlertDialog
    lateinit var date:TextInputEditText
    lateinit var kindOf:TextInputEditText
    lateinit var name:TextInputEditText
    lateinit var value:TextInputEditText
    lateinit var input:InputDTO
    lateinit var cancel:MaterialButton
    lateinit var save:MaterialButton

    private val validations  by lazy{
        arrayOf(
            date set R.string.error_empty `when` { text().isEmpty() },
            kindOf set R.string.error_empty `when` { text().isEmpty() },
            name set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        date = view.findViewById(R.id.date_in)
        kindOf = view.findViewById(R.id.kind_of_pay_in)
        name = view.findViewById(R.id.name_in)
        value = view.findViewById(R.id.value_in)
        cancel = view.findViewById(R.id.btn_cancel_in)
        save = view.findViewById(R.id.btn_save_in)
        cancel.setOnClickListener(actions)
        save.setOnClickListener(actions)
        date()
        dialog()
        valueFormat()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: InputDTO) {
        input = values
        date.setText(DateUtils.localDateToString(values.date))
        kindOf.setText(input.kindOf)
        name.setText(values.name)
        value.setText(NumbersUtil.COPtoString(values.value))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): InputDTO {
        val id = input.id ?: 0
        val date =  date.toLocalDate()
        val name = name.text.toString()
        val accountCode = input.accountCode
        val kindof = kindOf.text.toString()
        val value = value.COPtoBigDecimal()
        val start = LocalDate.now()
        val end = LocalDate.of(9999,12,31)
        return InputDTO(id,date!!,accountCode,kindof,name,value,start,end)
    }

    override fun cleanField() {
        date.text?.clear()
        name.text?.clear()
        value.text?.clear()
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid
    }

    private fun valueFormat(){
        value.setOnFocusChangeListener { _,focus ->
            if(!focus) {
                value.setCOPtoField()
            }
        }
    }

    private fun dialog(){
        val builder = AlertDialog.Builder(view.context)
        val items = view.resources.getStringArray(R.array.kind_of_pay_list)
        with(builder){
            setItems(items){ _,position ->
                val accountItem = items[position]
                kindOf.setText(accountItem)
            }
        }

        dialog = builder.create()

        kindOf.setOnClickListener{dialog.show()}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun date(){
        date.setText(DateUtils.localDateToString(LocalDate.now()))
        val dataPicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(R.string.date_paid)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()


        date.setOnClickListener{
            dataPicker.show(supportManager,"DT_INPUT")
            dataPicker.addOnPositiveButtonClickListener {
                val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                            .plusDays(1)
                this.date.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }
    }
}