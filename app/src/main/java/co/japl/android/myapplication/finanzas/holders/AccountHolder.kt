package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class AccountHolder(val view:View): IHolder<AccountDTO> {
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
        save.visibility =  View.GONE

        name.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {validate()}
        })
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
        val id = if(this::account.isInitialized) account.id ?: 0 else 0
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
        return valid.also{ if(it) save.visibility = View.VISIBLE }
    }
}