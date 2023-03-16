package co.japl.android.myapplication.holders

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime

class CreditCardSettingHolder (var view:View,val creditCardList:List<CreditCardDTO>, var parentFragmentManager:FragmentManager, var navController: NavController): IHolder<CreditCardSettingDTO>,ISpinnerHolder<CreditCardSettingHolder>, View.OnClickListener{
    lateinit var creditCard:MaterialAutoCompleteTextView
    lateinit var type:MaterialAutoCompleteTextView
    lateinit var name:TextInputEditText
    lateinit var value:TextInputEditText
    lateinit var add:Button
    lateinit var cancel:Button
    lateinit var active:MaterialCheckBox
    lateinit var setting:CreditCardSettingDTO
    private val creditCardSvc:SaveSvc<CreditCardSettingDTO> = CreditCardSettingImpl(ConnectDB(view.context))
    private var codeCreditCard: Int = 0
    private var nameCreditCard: String = ""
    private var codeType: Int = -1
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
        Log.v(this.javaClass.name,"LoadFields $values")
            setting = values
        if(setting.id > 0){
            name.setText(setting.name)
            value.setText(setting.value)
            val list = view.resources.getStringArray(R.array.CreditCardSettingType)
            val option = list.first{it == setting.type}
            type.setText(option)
            val creditCardName = creditCardList.firstOrNull{
                it.id == values.codeCreditCard
            }?.name ?: ""
            creditCard.setText(creditCardName)
            Log.v(this.javaClass.name,"LoadFields List: $list Option: $option ")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardSettingDTO {
        val id = if (setting.id > 0) setting.id else 0
        val codeCreditCard = if(this.codeCreditCard > 0) this.codeCreditCard else creditCardList.first{ it.name == creditCard.text.toString() }.id
        val create = LocalDateTime.now()
        val type = if( valueType?.isNotBlank() == true)  valueType else  this.type.text.toString()
        val name = this.name.text.toString()
        val value = this.value.text.toString()
        val active:Short = if(this.active.isSelected) 1 else 0
        return CreditCardSettingDTO(id,codeCreditCard,name,value,type,create,active)
    }

    override fun cleanField() {
        type.setSelection(0)
        name.text?.clear()
        value.text?.clear()
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
        onClick()
        onItemSelected()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddCCS-> {
                val dto = downLoadFields()
                if(validate()){
                    val response = creditCardSvc.save(dto)
                    if(dto.id <= 0) {
                        dto.id = response.toInt()
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
                    CreditCardSettingParams.toBack(setting.codeCreditCard,navController)
                }
        }
            R.id.btnCancelCCS -> CreditCardSettingParams.toBack(setting.codeCreditCard,navController)
        }
    }

    private fun onClick(){
        creditCard.setOnClickListener { creditCard.showDropDown() }
        type.setOnClickListener { type.showDropDown() }
    }

    private fun onItemSelected() {
                creditCard.setOnItemClickListener { adapter, _, position, _ ->
                    val selected = adapter?.getItemAtPosition(position)
                    codeCreditCard = creditCardList.first {
                        it.name == selected.toString()
                    }.id
                    nameCreditCard = selected.toString()
            }
            type.setOnItemClickListener { adapter, _, position, _ ->
                val selected = adapter?.getItemAtPosition(position)
                codeType = position
                valueType = selected.toString()
            }
    }
}