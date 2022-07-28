package co.japl.android.myapplication.holders

import android.graphics.drawable.Drawable
import android.os.Build
import android.service.autofill.OnClickAction
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import com.google.android.material.chip.Chip
import org.w3c.dom.Text
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month
import java.util.*

class TaxesHolder(var view:View) : IHolder<TaxDTO>,AdapterView.OnItemSelectedListener,ISpinnerHolder<TaxesHolder> {
    lateinit var creditCard:Spinner
    lateinit var month:Spinner
    lateinit var tax:EditText
    lateinit var year:EditText
    lateinit var save:Button
    lateinit var clear:Button
    lateinit var creditCardStr:String
    lateinit var monthStr:String
    lateinit var creditCardCode:Optional<Int>
    lateinit var monthCode:Optional<Int>

    override fun setFields(action: View.OnClickListener) {
        creditCard = view.findViewById(R.id.spCreditCardTaxes)
        month = view.findViewById(R.id.spMonthTaxes)
        tax = view.findViewById(R.id.edTaxesTaxes)
        year = view.findViewById(R.id.edYearTaxes)
        save = view.findViewById(R.id.btnSaveTaxes)
        clear = view.findViewById(R.id.btnClearTaxes)
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        month.onItemSelectedListener = this
        creditCard.onItemSelectedListener = this
    }

    override fun loadFields(values: TaxDTO) {
        tax.setText(values.value.toString())
        year.setText(values.year.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): TaxDTO {
        val years:Int = year.editableText.toString().toInt()
        val taxs:Double = tax.text.toString().toDouble()
        val create:LocalDateTime = LocalDateTime.now()
        val status:Short = 1
        val dto = TaxDTO(0,monthCode.get().toShort(),years,status,creditCardCode.get(),create,taxs)
        return dto
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun cleanField() {
        tax.editableText.clear()
        year.editableText.clear()
        creditCardCode = Optional.empty()
        creditCardStr = ""
        monthCode = Optional.empty()
        monthStr = ""
        (month.selectedView as TextView).text = month.getItemAtPosition(0).toString()
        (creditCard.selectedView.findViewById(R.id.tvValueSp) as TextView).text = creditCard.getItemAtPosition(0).toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun validate(): Boolean {
        var valid = true
        if(year.text.toString().isBlank() || !(year.text.toString().toInt() in 2015..2023)){
            year.error = "Invalid value permit values 2015-2023"
            valid = valid && false
        }
        if(tax.text.toString().isBlank() || !(tax.text.toString().toDouble() in 0.0..100.0)){
            tax.error = "Invalid value permit values 0.0 - 100.0 "
            valid = valid && false
        }
        if(monthCode == null || !monthCode.isPresent || !(monthCode.get() in 1..12)){
            (month.selectedView as TextView).error = "Invalid value"
            valid = valid && false
        }
        if(creditCardCode == null || !creditCardCode.isPresent || creditCardCode.get() < 1 ){
            (creditCard.selectedView.findViewById(R.id.tvValueSp) as TextView).error = "Invalid value"
            valid = valid && false
        }
        return valid
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = parent?.getItemAtPosition(position)
        if(selected != null){
            when(parent.id){
                R.id.spMonthTaxes->{
                    monthStr = selected.toString()
                    monthCode = Optional.ofNullable(position)
                }
                R.id.spCreditCardTaxes->{
                    creditCardStr = selected.toString()
                    creditCardCode = Optional.ofNullable(position)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this.view.context,"Invalid Option",Toast.LENGTH_LONG).show()
    }

    override fun lists(fn: ((TaxesHolder) -> Unit)?) {
        fn?.invoke(this)
    }


}