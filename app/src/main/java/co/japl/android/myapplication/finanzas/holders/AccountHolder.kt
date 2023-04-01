package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class AccountHolder(val view:View):IHolder<AccountDTO> {
    lateinit var name:TextInputEditText
    lateinit var account:AccountDTO
    lateinit var list:MaterialButton
    lateinit var save:MaterialButton

    private val validations  by lazy{
        arrayOf(
            name set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    override fun setFields(actions: View.OnClickListener?) {
        name = view.findViewById(R.id.name_acc)
        list = view.findViewById(R.id.btn_input_list_acc)
        save = view.findViewById(R.id.btn_save_acc)
        list.setOnClickListener(actions)
        save.setOnClickListener(actions)
        list.visibility = View.GONE

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: AccountDTO) {
        account = values
        name.setText(values.name)
        if(values.id > 0){
            list.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): AccountDTO {
        val id = account.id ?: 0
        val date =  LocalDate.now()
        val name = name.text.toString()
        val active = true
        return AccountDTO(id,date!!,name,active)
    }

    override fun cleanField() {
        name.text?.clear()
    }

    override fun validate(): Boolean {
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid
    }
}