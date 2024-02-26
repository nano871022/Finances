package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AdditionalCreditHolder(val view:View,val supportManager: FragmentManager,val isView:Boolean):
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
        save.visibility = View.INVISIBLE
        name.focusable = if(isView)  View.NOT_FOCUSABLE else View.FOCUSABLE
        value.focusable = if(isView)  View.NOT_FOCUSABLE else View.FOCUSABLE
        loadMoney()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: AdditionalCreditDTO) {
        if(values.id > 0) {
            name.setText(values.name)
            value.setText(NumbersUtil.toString(values.value))
            startDate.setText(DateUtils.localDateToString(values.startDate))
            endDate.setText(DateUtils.localDateToString(values.endDate))
        }else{
            startDate.visibility = View.GONE
            endDate.visibility = View.GONE
            startDate.setText(DateUtils.localDateToString(values.startDate))
            endDate.setText(DateUtils.localDateToString(values.endDate))
        }
        additionalCredit = values
    }

    private fun loadMoney(){
        val handler = Handler(Looper.getMainLooper())
        value.addTextChangedListener (object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    value.removeTextChangedListener(this)
                    value.setNumberToField()
                    value.addTextChangedListener (this)
                    validate()
                },1000)
            }
        })
        name.addTextChangedListener (object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {validate()}
        })
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
        return valid.also { if(it && !isView) save.visibility = View.VISIBLE }
    }
}