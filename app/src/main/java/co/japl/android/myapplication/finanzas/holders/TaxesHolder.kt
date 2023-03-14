package co.japl.android.myapplication.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.size
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.*

class TaxesHolder(var view:View,val creditCardList:List<CreditCardDTO>) : IHolder<TaxDTO>,ISpinnerHolder<TaxesHolder> {
    lateinit var creditCard:MaterialAutoCompleteTextView
    lateinit var month:MaterialAutoCompleteTextView
    lateinit var tax:TextInputEditText
    lateinit var period:TextInputEditText
    lateinit var kind:MaterialAutoCompleteTextView
    lateinit var year:TextInputEditText
    lateinit var save:Button
    lateinit var clear:Button
    var creditCardStr:String = ""
    var monthStr:String = ""
    @RequiresApi(Build.VERSION_CODES.N)
    var creditCardCode:Optional<Int> = Optional.empty()
    @RequiresApi(Build.VERSION_CODES.N)
    var monthCode:Optional<Int> = Optional.empty()
    lateinit var llPeriodsTaxes: TextInputLayout


    override fun setFields(action: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spCreditCardTaxes)
        month = view.findViewById(R.id.spMonthTaxes)
        tax = view.findViewById(R.id.edTaxesTaxes)
        year = view.findViewById(R.id.edYearTaxes)
        kind = view.findViewById(R.id.spKindTaxes)
        period = view.findViewById(R.id.etPeriodsTaxes)
        save = view.findViewById(R.id.btnSaveTaxes)
        clear = view.findViewById(R.id.btnClearTaxes)
        llPeriodsTaxes = view.findViewById(R.id.llPeriodsTaxes)
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        llPeriodsTaxes.visibility = View.INVISIBLE
    }
    @RequiresApi(Build.VERSION_CODES.O)
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
        val kind = TaxEnum.valueOf(kind.text.toString()).ordinal.toShort()

        val period:Short = (this.period.takeIf { it.editableText.isNotEmpty()}?.let {
            it.text.toString().toShort()
        }?: run{ "0".toShort() }) as Short


        val creditCard =  creditCardCode.orElseGet { ->
            if(creditCard.text.isNotBlank()){
                creditCardList.first {
                    it.name == creditCard.text.toString()
                }.id ?: 0
            }else{
                throw Exception("Is not possible get credit card")
            }
        }
        val month = monthCode.orElseGet{
            if(month.text.isNotBlank()){
                view.resources.getStringArray(R.array.Months).indexOfFirst {
                    it == month.text.toString()
                }
            }else{
                throw Exception("Is not possible get month")
            }
        }

        val dto = TaxDTO(0,month.toShort(),years,status,creditCard,create,taxs,kind,period).also { Log.d(this.javaClass.name,"Download $it") }
        return dto
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun cleanField() {
        tax.editableText.clear()
        year.editableText.clear()
        creditCardCode = Optional.empty()
        creditCardStr = ""
        period.editableText.clear()
        monthCode = Optional.empty()
        monthStr = ""
        month.setText(view.resources.getStringArray(R.array.Months)[LocalDate.now().monthValue])
        kind.setText(TaxEnum.CREDIT_CARD.name)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun validate(): Boolean {
        var valid = true
        if(year.text.toString().isBlank() || !(year.text.toString().toInt() in 2015..2023)){
            year.error = "Invalid value permit values 2015-2023"
            valid = false
        }
        Log.d(this.javaClass.name,"Year validation $valid")
        if(tax.text.toString().isBlank() || !(tax.text.toString().toDouble() in 0.0..100.0)){
            tax.error = "Invalid value permit values 0.0 - 100.0 "
            valid = false
        }
        Log.d(this.javaClass.name,"Tax validation $valid")
        if((!monthCode.isPresent || !(monthCode.get() in 1..12)) && month.text.isBlank()){
            month.error = "Invalid value"
            valid = false
        }
        Log.d(this.javaClass.name,"Month validation $valid")
        if( (!creditCardCode.isPresent || creditCardCode.get() < 1 ) && creditCard.text.isBlank()){
            creditCard.error = "Invalid value"
            valid = false
        }
        Log.d(this.javaClass.name,"CreditCard validation $valid")
        if(kind.text.toString().isBlank()){
            kind.error = "Invalid value"
            valid = false
        }
        Log.d(this.javaClass.name,"Kind validation $valid")
        if(kind.text.toString() != TaxEnum.CREDIT_CARD.name && period.text?.isBlank()==true){
            period.error = "Debe ingresar la cantidad de periodos"
            valid = false
        }
        Log.d(this.javaClass.name,"Period validation $valid")
        return valid
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun lists(fn: ((TaxesHolder) -> Unit)?) {
        fn?.invoke(this)
        month.setOnClickListener{
            month.showDropDown()
        }
        creditCard.setOnClickListener{
            creditCard.showDropDown()
        }
        kind.setOnClickListener{
            kind.showDropDown()
        }
        if(creditCardList.size == 1){
            creditCard.adapter.getItem(1)
        }
        onItemClick()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onItemClick() {
        creditCard.setOnItemClickListener{ adapter,_,position,_ ->
            val selected = adapter?.getItemAtPosition(position)
            Log.d(this.javaClass.name,"CreditCard:: Select Option $adapter $position $selected ")
            creditCardStr = selected.toString()
            creditCardCode = Optional.ofNullable(position)
        }

        month.setOnItemClickListener { adapter , _, position, _ ->
            val selected = adapter?.getItemAtPosition(position)
            Log.d(this.javaClass.name,"Month:: Select Option $adapter $position $selected ")
            monthStr = selected.toString()
            monthCode = Optional.ofNullable(position)
        }
        kind.setOnItemClickListener { adapter, _, position, _ ->
            val selected = adapter?.getItemAtPosition(position)
            Log.d(this.javaClass.name,"Kind:: Select Option $adapter $position $selected ${selected.toString() == TaxEnum.CASH_ADVANCE.name}")
            if(selected.toString() != TaxEnum.CREDIT_CARD.name){
                llPeriodsTaxes.visibility = View.VISIBLE
            }else{
                llPeriodsTaxes.visibility = View.INVISIBLE
            }
        }
    }


}