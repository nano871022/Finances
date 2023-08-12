package co.japl.android.myapplication.holders
import co.japl.android.myapplication.finanzas.holders.validations.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
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
    private val validations by lazy{ arrayOf(
        name set  R.string.name_credit_card_is_empty  `when` { text().isBlank() },
        cutOffDay set  R.string.cutoffday_value_invalid  `when` { text().isBlank() || text().isNotBlank() && text().toShort() < 1 || text().toShort() > 31 },
        warningQuote set  R.string.name_credit_card_is_empty  `when` { text().isBlank() || text().isNotBlank() && NumbersUtil.toBigDecimal(text()) <= BigDecimal.ONE }
    )}

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
        onClick(action)
        id = Optional.empty()
        setting.visibility = View.INVISIBLE
        onFocus()
        save.visibility = View.INVISIBLE
    }

    private fun onClick(action: View.OnClickListener?){
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        setting.setOnClickListener(action)
    }

    private fun onFocus(){
        warningQuote.addTextChangedListener(object: TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    if(warningQuote.text?.isNotBlank() == true){
                        warningQuote.removeTextChangedListener(this)
                        warningQuote.setText(NumbersUtil.toString(warningQuote))
                        warningQuote.addTextChangedListener (this)
                    }
                    validate()
                },1000)
            }
        })

        cutOffDay.addTextChangedListener(object: TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {validate()}
        })

        maxQuotes.addTextChangedListener(object: TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {validate()}
        })

        name.addTextChangedListener(object: TextWatcher{
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {validate()}
        })
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
        var valid = false
        validations.firstInvalid{requestFocus()}.notNull { valid = true }
        return valid.also { if(it) save.visibility = View.VISIBLE }
    }


}