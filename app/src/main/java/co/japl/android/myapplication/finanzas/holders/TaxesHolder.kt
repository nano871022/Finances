package co.japl.android.myapplication.holders

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.size
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.holders.validations.*
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.*

class TaxesHolder(var view:View,val creditCardList:List<CreditCardDTO>) : IHolder<TaxDTO>, OnClickListener{
    lateinit var creditCard:MaterialAutoCompleteTextView
    lateinit var month:MaterialAutoCompleteTextView
    lateinit var tax:TextInputEditText
    lateinit var kindOfTax:TextInputEditText
    lateinit var period:TextInputEditText
    lateinit var kind:MaterialAutoCompleteTextView
    lateinit var year:TextInputEditText
    lateinit var save:Button
    lateinit var clear:Button
    private lateinit var kindOfTaxDialog:AlertDialog
    private lateinit var kindTaxesDialog:AlertDialog
    private lateinit var monthDialog:AlertDialog
    private lateinit var creditCardDialog:AlertDialog
    var creditCardStr:String = ""
    var monthStr:String = ""
    @RequiresApi(Build.VERSION_CODES.N)
    var creditCardCode:Optional<Int> = Optional.empty()
    @RequiresApi(Build.VERSION_CODES.N)
    var monthCode:Optional<Int> = Optional.empty()
    lateinit var llPeriodsTaxes: TextInputLayout
    var taxInitial: TaxDTO? = null


    override fun setFields(action: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spCreditCardTaxes)
        month = view.findViewById(R.id.spMonthTaxes)
        tax = view.findViewById(R.id.edTaxesTaxes)
        year = view.findViewById(R.id.edYearTaxes)
        kind = view.findViewById(R.id.spKindTaxes)
        period = view.findViewById(R.id.etPeriodsTaxes)
        kindOfTax = view.findViewById(R.id.et_kind_of_tax_txs)
        save = view.findViewById(R.id.btnSaveTaxes)
        clear = view.findViewById(R.id.btnClearTaxes)
        llPeriodsTaxes = view.findViewById(R.id.llPeriodsTaxes)
        save.setOnClickListener(action)
        clear.setOnClickListener(action)
        llPeriodsTaxes.visibility = View.INVISIBLE
        creditCard.isFocusable = false
        kind.isFocusable = false
        month.isFocusable = false
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: TaxDTO) {
        taxInitial = values
        tax.setText(values.value.toString())
        year.setText(values.year.toString())
        kindOfTax.setText(values.kindOfTax)
        onItemClick()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): TaxDTO {
        val years:Int = year.editableText.toString().toInt()
        val taxs:Double = tax.text.toString().toDouble()
        val create:LocalDateTime = LocalDateTime.now()
        val status:Short = 1
        val kind = TaxEnum.valueOf(kind.text.toString()).ordinal.toShort()
        val kindOfTax = kindOfTax.text.toString()

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

        val dto = TaxDTO(0,month.toShort(),years,status,creditCard,create,taxs,kind,period,kindOfTax).also { Log.d(this.javaClass.name,"Download $it") }
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
        if(creditCardList.size == 1){
            creditCard.setText(creditCardList.first().name)
        }
        taxInitial?.let{
            year.setText(it.year.toString())
            tax.setText(it.value.toString())
        }
    }

    private val validations  by lazy{
        arrayOf(
            year set R.string.error_empty `when` { text().isEmpty() },
            tax set R.string.error_empty `when` { text().isEmpty() },
            kindOfTax set R.string.error_empty `when` { text().isEmpty() }
        )
    }

    private val validations2  by lazy{
        arrayOf(
            month set R.string.error_empty `when` { text().isEmpty() },
            creditCard set R.string.error_empty `when` { text().isEmpty()} ,
            kind set R.string.error_empty `when` { text().isEmpty() },
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun validate(): Boolean {
        var valid = true
        validations.firstInvalid{ requestFocus() }.notNull {
            validations2.firstInvalid { requestFocus() }.notNull { valid = true }
        }
        return valid
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onItemClick() {
        createDialogCreditCard()
        createDialogKindTaxes()
        createDialogMonth()
        createDialogKindOfTax()
        creditCard.setOnClickListener (this)
        month.setOnClickListener (this)
        kind.setOnClickListener (this)
        kindOfTax.setOnClickListener(this)
    }

    private fun createDialogKindOfTax(){
        val builderKindOf = AlertDialog.Builder(view.context)
        with(builderKindOf){
            val items = view.resources.getStringArray(R.array.kind_of_tax_list)
            setItems(items){ _,position ->
                val kindOf = items[position]
                kindOfTax.setText(kindOf)
            }
        }
        kindOfTaxDialog = builderKindOf.create()
    }

    private fun createDialogKindTaxes() {
        val builder = AlertDialog.Builder(view.context)
        with(builder) {
            val items = TaxEnum.values().map { it.toString() }.toTypedArray()
            setItems(items) { _, position ->
                val selected = items[position]
                kind.setText(selected)
                if (selected != TaxEnum.CREDIT_CARD.name) {
                    llPeriodsTaxes.visibility = View.VISIBLE
                } else {
                    llPeriodsTaxes.visibility = View.INVISIBLE
                }
            }
            kindTaxesDialog = builder.create()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createDialogMonth(){
        val builder = AlertDialog.Builder(view.context)
        with(builder) {
            val items = view.resources.getStringArray(R.array.Months)
            builder.setItems(items) { _, position ->
                val selected = items[position]
                monthStr = selected.toString()
                monthCode = Optional.ofNullable(position)
                month.setText(monthStr)
            }
        }
        monthDialog = builder.create()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createDialogCreditCard(){
        val builder = AlertDialog.Builder(view.context)
        with(builder) {
            setItems(creditCardList.map { "${it.id}. ${it.name}" }
                .toTypedArray()) { _, position ->
                val creditCardFound = creditCardList[position]
                creditCardStr = creditCardFound.name
                creditCardCode = Optional.ofNullable(creditCardFound.id)
                creditCard.setText(creditCardStr)
            }
        }
        creditCardDialog = builder.create()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.spKindTaxes->kindTaxesDialog.show()
            R.id.spMonthTaxes->monthDialog.show()
            R.id.spCreditCardTaxes->creditCardDialog.show()
            R.id.et_kind_of_tax_txs->kindOfTaxDialog.show()
        }
    }


}