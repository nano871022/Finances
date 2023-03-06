package co.japl.android.myapplication.holders

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import java.time.LocalDateTime

class CreditCardSettingHolder (var view:View, var parentFragmentManager:FragmentManager, var navController: NavController): IHolder<CreditCardSettingDTO>,ISpinnerHolder<CreditCardSettingHolder>, View.OnClickListener,AdapterView.OnItemSelectedListener {
    lateinit var creditCard:Spinner
    lateinit var type:Spinner
    lateinit var name:EditText
    lateinit var value:EditText
    lateinit var add:Button
    lateinit var cancel:Button
    lateinit var active:CheckBox
    lateinit var setting:CreditCardSettingDTO
    private val creditCardSvc:SaveSvc<CreditCardSettingDTO> = CreditCardSettingImpl(ConnectDB(view.context))
    private var codeCreditCard: Int = 0
    private var nameCreditCard: String = ""
    private var codeType: Int = 0
    private var valueType: String = ""


    override fun setFields(actions: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spCreditCardCCSF)
        type = view.findViewById(R.id.spTypeCCS)
        name = view.findViewById(R.id.etNameCCS)
        value = view.findViewById(R.id.etValueCCS)
        add = view.findViewById(R.id.btnAddCCS)
        cancel = view.findViewById(R.id.btnCancelCCS)
        active = view.findViewById(R.id.cbActiveCCS)
        add.setOnClickListener(this)
        cancel.setOnClickListener(this)
    }

    override fun loadFields(values: CreditCardSettingDTO) {
        if (values != null) {
            setting = values
        }
        if(setting.id > 0){
            name.setText(setting.name)
            value.setText(setting.value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardSettingDTO {
        val id = if (setting.id > 0) setting.id else 0
        val codeCreditCard = creditCard.selectedItemId.toString().toInt()
        val create = LocalDateTime.now()
        val type = this.type.selectedItem.toString()
        val name = this.name.text.toString()
        val value = this.value.text.toString()
        val active:Short = if(this.active.isSelected) 1 else 0
        return CreditCardSettingDTO(id,codeCreditCard,name,value,type,create,active)
    }

    override fun cleanField() {
        type.setSelection(0)
        name.text.clear()
        value.text.clear()
    }

    override fun validate(): Boolean {
        var validate = true
        if (name.editableText.isBlank()) {
            name.error = view.resources.getString(R.string.validation_name_empty)
            validate = validate and false
        }
        if (value.editableText.isBlank()) {
            value.error = view.resources.getString(R.string.validation_value_empty)
            validate = validate and false
        }
        if(type.isSelected){
            type.setBackgroundColor(Color.RED)
            Toast.makeText(view.context,view.resources.getString(R.string.validation_type_not_selected),Toast.LENGTH_LONG).show()
            validate = validate and false
        }else{
            type.setBackgroundColor(Color.TRANSPARENT)
            validate = validate and true
        }
        if(creditCard.isSelected){
            creditCard.setBackgroundColor(Color.RED)
            Toast.makeText(view.context,view.resources.getString(R.string.validation_credit_card_not_selected),Toast.LENGTH_LONG).show()
            validate = validate and false
        }else{
            creditCard.setBackgroundColor(Color.TRANSPARENT)
            validate = validate and true
        }
        return validate
    }

    override fun lists(fn: ((CreditCardSettingHolder) -> Unit)?) {
        fn?.invoke(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddCCS-> {
                val dto = downLoadFields()
                if(validate() && creditCardSvc.save(dto)>0){
                    if(dto.id > 0) {
                        Toast.makeText(
                            view.context,
                            view.resources.getString(R.string.toast_successful_insert),
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            view.context,
                            view.resources.getString(R.string.toast_successful_update),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    CreditCardSettingParams.toBack(navController)
                }else{
                    Toast.makeText(view.context,view.resources.getString(R.string.toast_successful_insert),Toast.LENGTH_LONG).show()
                }
        }
            R.id.btnCancelCCS -> CreditCardSettingParams.toBack(navController)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = parent?.getItemAtPosition(position)
        when(view?.id){
            R.id.spCreditCardCCSF ->{
                nameCreditCard = selected.toString()
                codeCreditCard = position
            }
            R.id.spTypeCCS -> {
                codeType = position
                valueType = selected.toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}