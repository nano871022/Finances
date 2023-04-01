package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class InputHolder(val view:View):IHolder<InputDTO> {
    lateinit var date:TextInputEditText
    lateinit var name:TextInputEditText
    lateinit var value:TextInputEditText
    lateinit var input:InputDTO
    lateinit var cancel:MaterialButton
    lateinit var save:MaterialButton

    private val validations  by lazy{
        arrayOf(
            date set R.string.error_empty `when` { text().isEmpty() },
            name set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    override fun setFields(actions: View.OnClickListener?) {
        date = view.findViewById(R.id.date_in)
        name = view.findViewById(R.id.name_in)
        value = view.findViewById(R.id.value_in)
        cancel = view.findViewById(R.id.btn_cancel_in)
        save = view.findViewById(R.id.btn_save_in)
        cancel.setOnClickListener(actions)
        save.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: InputDTO) {
        input = values
        date.setText(DateUtils.localDateToString(values.date))
        name.setText(values.name)
        value.setText(NumbersUtil.COPtoString(values.value))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): InputDTO {
        val id = input.id ?: 0
        val date =  date.toLocalDate()
        val name = name.text.toString()
        val value = value.COPtoBigDecimal()
        return InputDTO(id,date!!,name,value)
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
}