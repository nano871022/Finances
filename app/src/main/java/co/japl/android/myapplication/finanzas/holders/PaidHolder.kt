package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.setCOPtoField
import android.app.AlertDialog
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PaidHolder(val view:View,val supportManager: FragmentManager):IHolder<PaidDTO> {
    private val
            service:SaveSvc<AccountDTO> = AccountImpl(ConnectDB(view.context))
    private lateinit var accountList:List<AccountDTO>
    lateinit var date:TextInputEditText
    lateinit var account:TextInputEditText
    lateinit var name:TextInputEditText
    lateinit var value:TextInputEditText
    lateinit var recurrent:CheckBox
    lateinit var paid:PaidDTO
    lateinit var cancel:MaterialButton
    lateinit var save:MaterialButton
    lateinit var accountDialog:AlertDialog

    private val validations  by lazy{
        arrayOf(
            date set R.string.error_empty `when` { text().isEmpty() },
            account set R.string.error_empty `when` { text().isEmpty() },
            name set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        date = view.findViewById(R.id.date_paid_p)
        account = view.findViewById(R.id.account_p)
        name = view.findViewById(R.id.name_p)
        value = view.findViewById(R.id.value_p)
        recurrent = view.findViewById(R.id.recurrent_p)
        cancel = view.findViewById(R.id.btn_cancel_p)
        save = view.findViewById(R.id.btn_save_p)
        cancel.setOnClickListener(actions)
        save.setOnClickListener(actions)
        date()
        accounts()
        value.setOnFocusChangeListener{ _,focus ->
            if(!focus){
                value.setCOPtoField()
            }
        }
    }

    private fun accounts(){
        accountList = service.getAll()
        accountDialog(accountList.toMutableList())
    }

    private fun accountDialog(accountList:MutableList<AccountDTO>){
        val builderAccount = AlertDialog.Builder(view.context)
        val items = accountList.map { "${it.id}. ${it.name}" }.toTypedArray()
        with(builderAccount){
            setItems(items){ _,position ->
                val accountItem = items[position]
                account.setText(accountItem)
            }
        }

        accountDialog = builderAccount.create()

        account.setOnClickListener{accountDialog.show()}
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
            dataPicker.show(supportManager,"DT_BILL")
            dataPicker.addOnPositiveButtonClickListener {
                val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()).plusDays(1)
                this.date.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: PaidDTO) {
        paid = values
        date.setText(DateUtils.localDateToString(values.date))
        account.setText(values.account)
        name.setText(values.name)
        value.setText(NumbersUtil.COPtoString(values.value))
        recurrent.isSelected = values.recurrent == (1).toShort()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): PaidDTO {
        val id =  if(this::paid.isInitialized){
                paid.id
            }else{
                0
        }
        val date =  date.toLocalDate()
        val account = account.text.toString()
        val name = name.text.toString()
        val value = value.COPtoBigDecimal()
        val recurrent = if(recurrent.isChecked) 1 else 0
        return PaidDTO(id,date!!,account,name,value,recurrent.toShort())
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
        return valid
    }
}