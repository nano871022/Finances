package co.japl.android.myapplication.holders

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.math.max

class CreditCardHolder(var view:View) : IHolder<CreditCardDTO> {
    lateinit var name:TextInputEditText
    lateinit var maxQuotes:TextInputEditText
    lateinit var cutOffDay:TextInputEditText
    lateinit var warningQuote:TextInputEditText
    lateinit var status:SwitchMaterial
    lateinit var interest1Quote:SwitchMaterial
    lateinit var interest1NotQuote:SwitchMaterial
    lateinit var save:Button
    lateinit var clear:Button
    lateinit var setting:Button
    private lateinit var id:Optional<Int>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setFields(action: View.OnClickListener?) {
        name = view.findViewById(R.id.etNameCCC)
        maxQuotes = view.findViewById(R.id.et_max_quotes_ccc)
        cutOffDay = view.findViewById(R.id.edCutOffDayCCC)
        warningQuote = view.findViewById(R.id.edWarningQuoteCCC)
        save = view.findViewById(R.id.btnSaveCCC)
        clear = view.findViewById(R.id.btnCleanCCC)
        setting = view.findViewById(R.id.btnSettingsCCC)
        interest1NotQuote = view.findViewById(R.id.tb_interest_1not_quote_ccc)
        interest1Quote = view.findViewById(R.id.tb_interest_1quote_ccc)
        status = view.findViewById(R.id.tb_status_ccc)
        status.isChecked = true
        interest1Quote.isChecked = false
        interest1NotQuote.isChecked = false
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        setting.setOnClickListener(action)
        id = Optional.empty()
        setting.visibility = View.INVISIBLE
        warningQuote.setOnFocusChangeListener{_,focus->
            if(!focus && warningQuote.text?.isNotBlank() == true){
                warningQuote.setText(NumbersUtil.toString(warningQuote))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: CreditCardDTO) {
        name.setText(values.name)
        cutOffDay.setText(values.cutOffDay.toString())
        warningQuote.setText(NumbersUtil.toString(values.warningValue))
        status.isChecked = values.status
        interest1Quote.isChecked = values.interest1Quote
        interest1NotQuote.isChecked = values.interest1NotQuote
        maxQuotes.setText(values.maxQuotes.toString())
        id = Optional.ofNullable(values.id)
        if(values.id > 0){
            setting.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardDTO {
        val name:String = name.text.toString()
        var cutOffDay: Short = Short.MIN_VALUE
        if(this.cutOffDay.text?.isNotBlank() == true) {
            cutOffDay = this.cutOffDay.text.toString().toShort()
        }
        val warningQuote = NumbersUtil.toBigDecimal(this.warningQuote)
        val create:LocalDateTime = LocalDateTime.now()
        val maxQuotes = maxQuotes.text.toString().toShort()
        return CreditCardDTO(id.orElse(0),name,maxQuotes,cutOffDay,warningQuote,create,status.isChecked,interest1Quote.isChecked, interest1NotQuote.isChecked)
    }

    override fun cleanField() {
        name.editableText.clear()
        cutOffDay.editableText.clear()
        warningQuote.editableText.clear()
        status.isChecked = true
        interest1Quote.isChecked = false
        interest1NotQuote.isChecked = false
    }

    override fun validate(): Boolean {
        var valid = true
        if(name.text.toString().isBlank()){
            name.error = "Fill field with name of credit card"
            valid = valid and false
        }
        if(cutOffDay.text.toString().isBlank() || cutOffDay.text.toString().toShort() < 1 || cutOffDay.text.toString().toShort() > 31 ){
            cutOffDay.error = "Invalid value permit values 1-31"
            valid = valid and false
        }

        if(warningQuote?.text.toString().isNotBlank() && warningQuote?.text.toString().replace(",","").toBigDecimal() < BigDecimal(1)){
            warningQuote.error = "Invalid value, it should be higher of 0 (Zero)"
            valid = valid and false
        }
        return valid
    }


}