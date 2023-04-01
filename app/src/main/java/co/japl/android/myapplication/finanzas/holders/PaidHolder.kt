package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class PaidHolder(val view:View):IHolder<PaidDTO> {
    lateinit var date:TextInputEditText
    lateinit var account:TextInputEditText
    lateinit var name:TextInputEditText
    lateinit var value:TextInputEditText
    lateinit var recurrent:MaterialCheckBox
    lateinit var paid:PaidDTO
    lateinit var cancel:MaterialButton
    lateinit var save:MaterialButton

    private val validations  by lazy{
        arrayOf(
            date set R.string.error_empty `when` { text().isEmpty() },
            account set R.string.error_empty `when` { text().isEmpty() },
            name set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    override fun setFields(actions: View.OnClickListener?) {
        date = view.findViewById(R.id.date_paid_p)
        account = view.findViewById(R.id.account_p)
        name = view.findViewById(R.id.name_p)
        value = view.findViewById(R.id.value_p)
        recurrent = view.findViewById(R.id.recurrent_p)
        cancel = view.findViewById(R.id.btn_cancel_p)
        save = view.findViewById(R.id.bnt_save_p)
        cancel.setOnClickListener(actions)
        save.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: PaidDTO) {
        paid = values
        date.setText(DateUtils.localDateToString(values.date))
        account.setText(values.account)
        name.setText(values.name)
        value.setText(NumbersUtil.COPtoString(values.value))
        recurrent.isSelected = values.recurrent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): PaidDTO {
        val id = paid.id ?: 0
        val date =  date.toLocalDate()
        val account = account.text.toString()
        val name = name.text.toString()
        val value = value.COPtoBigDecimal()
        val recurrent = recurrent.isSelected
        return PaidDTO(id,date!!,account,name,value,recurrent)
    }

    override fun cleanField() {
        date.text?.clear()
        account.text?.clear()
        name.text?.clear()
        value.text?.clear()
        recurrent.isSelected = false
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        if(recurrent.isSelected) {
            recurrent.error = view.resources.getString(R.string.error_empty)
            valid = false
        }
        return valid
    }
}