package co.japl.android.myapplication.finanzas.holders

import android.app.AlertDialog
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
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.enums.KindofProjectionEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ProjectionHolder(val view:View,val supportManager:FragmentManager): IHolder<ProjectionDTO> {
    private lateinit var date:TextInputEditText
    private lateinit var name:TextInputEditText
    private lateinit var type:TextInputEditText
    private lateinit var value:TextInputEditText
    private lateinit var quote:TextView
    private lateinit var active:SwitchMaterial
    private lateinit var cancel:MaterialButton
    private lateinit var add:Button
    private lateinit var dto:ProjectionDTO
    private lateinit var dataPicker:MaterialDatePicker<Long?>
    private lateinit var dialog:AlertDialog
    private lateinit var list:Array<String>
    private val validations  by lazy{
        arrayOf(
            date set R.string.error_empty `when` { text().isEmpty() },
            type set R.string.error_empty `when` { text().isEmpty() },
            name set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        date = view.findViewById(R.id.et_dt_date_prj)
        name = view.findViewById(R.id.et_name_prj)
        type = view.findViewById(R.id.et_type_prj)
        value = view.findViewById(R.id.et_value_prj)
        quote = view.findViewById(R.id.tv_quote_prj)
        active = view.findViewById(R.id.sw_active_prj)
        cancel = view.findViewById(R.id.btn_cancel_prj)
        add = view.findViewById(R.id.btn_save_prj)

        cancel.setOnClickListener(actions)
        add.setOnClickListener(actions)
        add.visibility = View.INVISIBLE
        settingDatePicket()
        settingType()
        settingCOPField()
    }

    private fun settingCOPField(){
        value.addTextChangedListener(object:TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    value.removeTextChangedListener(this)
                    value.toMoneyFormat()
                    calculateQuote()
                    value.addTextChangedListener (this)
                    validate()
                },1000)
            }
        })
        name.addTextChangedListener(object:TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {validate()}
        })
    }

    private fun calculateQuote(){
        if(validate()) {
            val value = value.COPtoBigDecimal()
            val type = type.text()
            val postType = list.indexOf(type)
            val list = KindofProjectionEnum.values()
            val quote = list?.takeIf { it.size > postType }?.let {
                val months = list[postType]
                value / months.months.toBigDecimal()
            } ?: BigDecimal.ZERO
            this.quote.setText(NumbersUtil.COPtoString(quote))
        }
    }

    private fun settingType(){
        list = view.resources.getStringArray(R.array.kind_of_projection_list)
        val builder = AlertDialog.Builder(view.context)
        with(builder){
            setItems(list){ _,position->
                val value = list[position]
                    type.setText(value)
                validate()
            }
        }
        dialog = builder.create()
        type.setOnClickListener { dialog.show() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun settingDatePicket(){
        dataPicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(view.resources.getString(R.string.bought_date_time))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        dataPicker.addOnPositiveButtonClickListener {
            var date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it!!), ZoneId.systemDefault())
            date = date.plusDays(1)
            this.date.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            validate()
        }
        date.setOnClickListener { dataPicker.show(supportManager,"dt_buy") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: ProjectionDTO) {
        dto = values
        date.setText(DateUtils.localDateToString(values.end))
        name.setText(values.name)
        type.setText(values.type)
        value.setText(NumbersUtil.COPtoString(values.value))
        quote.text = NumbersUtil.COPtoString(values.quote)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): ProjectionDTO {
        val id = if( this::dto.isInitialized) dto.id else 0
        val create = LocalDate.now()
        val end = DateUtils.toLocalDate(date.text.toString())
        val name = this.name.text.toString()
        val type = this.type.text.toString()
        val value = NumbersUtil.toBigDecimal(this.value)
        val quote = this.quote.COPToBigDecimal()
        val active:Short = if (this.active.isChecked)  1 else 0
        return ProjectionDTO(id,create,end,name,type,value,quote,active)
    }

    override fun cleanField() {
        date.text?.clear()
        name.text?.clear()
        type.text?.clear()
        value.text?.clear()
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid.also { if(it) add.visibility = View.VISIBLE }
    }

}