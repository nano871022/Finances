package co.japl.android.myapplication.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.time.LocalDateTime
import java.util.*

class TaxesHolder(var view:View) : IHolder<TaxDTO>,AdapterView.OnItemSelectedListener,ISpinnerHolder<TaxesHolder> {
    lateinit var creditCard:Spinner
    lateinit var month:Spinner
    lateinit var tax:EditText
    lateinit var period:EditText
    lateinit var kind:Spinner
    lateinit var year:EditText
    lateinit var save:Button
    lateinit var clear:Button
    lateinit var creditCardStr:String
    lateinit var monthStr:String
    lateinit var creditCardCode:Optional<Int>
    lateinit var monthCode:Optional<Int>
    lateinit var layutPeriod:LinearLayout

    override fun setFields(action: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spCreditCardTaxes)
        month = view.findViewById(R.id.spMonthTaxes)
        tax = view.findViewById(R.id.edTaxesTaxes)
        year = view.findViewById(R.id.edYearTaxes)
        kind = view.findViewById(R.id.spKindTaxes)
        period = view.findViewById(R.id.etPeriodsTaxes)
        save = view.findViewById(R.id.btnSaveTaxes)
        clear = view.findViewById(R.id.btnClearTaxes)
        layutPeriod = view.findViewById(R.id.llPeriodTaxes)
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        month.onItemSelectedListener = this
        creditCard.onItemSelectedListener = this
        kind.onItemSelectedListener = this

        layutPeriod.visibility = View.INVISIBLE
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
        val kind = TaxEnum.valueOf(kind.selectedItem.toString()).ordinal.toShort()

        val period:Short = (this.period.takeIf { it.editableText.isNotEmpty()}?.let {
            it.text.toString().toShort()
        }?: run{ "0".toShort() }) as Short
        val dto = TaxDTO(0,monthCode.get().toShort(),years,status,creditCardCode.get(),create,taxs,kind,period)
        return dto
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun cleanField() {
        tax.editableText.clear()
        year.editableText.clear()
        creditCardCode = Optional.empty()
        creditCardStr = ""
        period.editableText.clear()
        monthCode = Optional.empty()
        monthStr = ""
        (month.selectedView as TextView).text = month.getItemAtPosition(0).toString()
        (creditCard.selectedView.findViewById(R.id.tvValueBigSp) as TextView).text = creditCard.getItemAtPosition(0).toString()
        (kind.selectedView.findViewById(R.id.tvValueBigSp) as TextView).text = TaxEnum.CREDIT_CARD.name
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
            (creditCard.selectedView.findViewById(R.id.tvValueBigSp) as TextView).error = "Invalid value"
            valid = valid && false
        }
        if(kind.selectedItem.toString() != TaxEnum.CREDIT_CARD.name && period.editableText.isEmpty()){
            period.error = "Debe ingresar la cantidad de periodos"
            valid = false
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
                R.id.spKindTaxes->{
                    if(TaxEnum.CREDIT_CARD.name  != selected.toString()){
                        layutPeriod.visibility = View.VISIBLE
                    }else {
                        layutPeriod.visibility = View.INVISIBLE
                    }
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