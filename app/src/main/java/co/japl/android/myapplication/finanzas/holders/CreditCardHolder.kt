package co.japl.android.myapplication.holders

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class CreditCardHolder(var view:View) : IHolder<CreditCardDTO> {
    lateinit var name:EditText
    lateinit var cutOffDay:EditText
    lateinit var warningQuote:EditText
    lateinit var save:Button
    lateinit var clear:Button
    private lateinit var id:Optional<Int>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setFields(action: View.OnClickListener?) {
        name = view.findViewById(R.id.etNameCCC)
        cutOffDay = view.findViewById(R.id.edCutOffDayCCC)
        warningQuote = view.findViewById(R.id.edWarningQuoteCCC)
        save = view.findViewById(R.id.btnSaveCCC)
        clear = view.findViewById(R.id.btnCleanCCC)
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        id = Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: CreditCardDTO) {
        name.setText(values.name)
        cutOffDay.setText(values.cutOffDay.toString())
        warningQuote.setText(values.warningValue.toString())
        id = Optional.ofNullable(values.id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardDTO {
        val name:String = name.text.toString()
        val cutOffDay:Short = cutOffDay.text.toString().toShort()
        val warningQuote:BigDecimal = warningQuote.text.toString().toBigDecimal()
        val create:LocalDateTime = LocalDateTime.now()
        val status:Short = 1
        val dto = CreditCardDTO(id.orElse(0),name,cutOffDay,warningQuote,create,status)
        return dto
    }

    override fun cleanField() {
        name.editableText.clear()
        cutOffDay.editableText.clear()
        warningQuote.editableText.clear()
    }

    override fun validate(): Boolean {
        var valid = true
        if(name.text.toString().isBlank()){
            name.error = "Fill field with name of credit card"
            valid = valid && false
        }
        if(cutOffDay.text.toString().isBlank() || cutOffDay.text.toString().toShort() < 1 || cutOffDay.text.toString().toShort() > 31 ){
            cutOffDay.error = "Invalid value permit values 1-31"
            valid = valid && false
        }
        if(warningQuote.text.toString().isNotBlank() && warningQuote.text.toString().toBigDecimal() < BigDecimal(1)){
            warningQuote.error = "Invalid value, it should be higher of 0 (Zero)"
            valid = valid && false
        }
        return valid
    }


}