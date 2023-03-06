package co.japl.android.myapplication.finanzas.holders

import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterest
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSettingSvc
import co.japl.android.myapplication.finanzas.utils.KindBoughtEnum
import co.japl.android.myapplication.utils.DateUtils
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class QuoteBoughtHolder(var root:View) : IHolder<CreditCardBoughtDTO>, AdapterView.OnItemSelectedListener {
    lateinit var etProductName: TextInputEditText
    lateinit var etProductValue: TextInputEditText
    lateinit var etTax: TextView
    lateinit var etMonths: TextInputEditText
    lateinit var etQuotesValue: TextView
    lateinit var tvCardAssing: TextView
    lateinit var dtBought: TextInputEditText
    lateinit var llTax : LinearLayout
    private lateinit var btnSave: Button
    lateinit var chRecurrent: CheckBox
    lateinit var spTypeSetting: Spinner
    lateinit var spNameSetting: Spinner
    lateinit var llTypeSetting: View
    lateinit var llNameSetting: View
    //services
    private var calcTax: Calc = QuoteCreditVariableInterest()
    private var calc: Calc = QuoteCreditVariable()
    //Parameters
    private var taxMonthly: Double = 0.0
    private lateinit var creditCardName:String
    private lateinit var creditCardCode:Optional<Int>
    private lateinit var cutOffDate:LocalDateTime
    private val buyCCSsvc:SaveSvc<BuyCreditCardSettingDTO> = BuyCreditCardSettingImpl(ConnectDB(root.context))
    private val cCSettingsvc:ICreditCardSettingSvc = CreditCardSettingImpl(ConnectDB(root.context))
    private lateinit var buyCCSDTO:Optional<BuyCreditCardSettingDTO>
    private lateinit var cCSettingList:List<CreditCardSettingDTO>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(actions: View.OnClickListener?) {
        root.let{
            etProductName = it.findViewById(R.id.etNameItem)
            etTax = it.findViewById(R.id.etTaxBought)!!
            etMonths = it.findViewById(R.id.etMonths)!!
            etQuotesValue = it.findViewById(R.id.etQuoteValue)!!
            etProductValue = it.findViewById(R.id.etProductValue)!!
            tvCardAssing = it.findViewById(R.id.etCreditCardAssign)!!
            dtBought = it.findViewById(R.id.dtBought)!!
            llTax = it.findViewById(R.id.linearLayoutTax)!!
            chRecurrent = it.findViewById(R.id.cbRecurrent)
            llNameSetting = it.findViewById(R.id.llSettingNameBCC)
            llTypeSetting = it.findViewById(R.id.llSetingTypeBCC)
            spNameSetting = it.findViewById(R.id.spNameSettingBCC)
            spTypeSetting = it.findViewById(R.id.spTypeSettingBCC)

            etProductName.setOnFocusChangeListener { _, b -> !b and validate() && calc() }
            etProductValue.setOnFocusChangeListener { _, b -> !b and validate() && calc() }
            etMonths.setOnFocusChangeListener { _, b -> !b and validate() && calc() }

            llTypeSetting.visibility = View.INVISIBLE
            llNameSetting.visibility = View.INVISIBLE

            val btnClear: Button = it.findViewById(R.id.btnClearBought)
            btnClear.setOnClickListener(actions)
            btnSave = it.findViewById(R.id.btnSaveBought)
            btnSave.setOnClickListener(actions)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: CreditCardBoughtDTO) {
        taxMonthly = values.interest
        creditCardCode = Optional.ofNullable(values.codeCreditCard)
        creditCardName = values.nameCreditCard
        cutOffDate = values.cutOutDate
        dtBought.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        etTax.text = taxMonthly.toString()
        tvCardAssing.text = values.nameCreditCard
        buyCCSDTO = buyCCSsvc.get(values.id)
        cCSettingList = cCSettingsvc.getAll(values.codeCreditCard)

        if(cCSettingList.isNotEmpty()){
                llTypeSetting.visibility = View.VISIBLE
            spTypeSetting.adapter = ArrayAdapter(
                root.context,
                R.layout.spinner_simple,
                R.id.tvValueBigSp,
                root.resources.getStringArray(R.array.CreditCardSettingType)
            )
            spTypeSetting.onItemSelectedListener = this
            spNameSetting.onItemSelectedListener = this
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardBoughtDTO {
        return map()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downLoadBuyCreditCardSetting():BuyCreditCardSettingDTO{
        val defaultItemSelected = root.resources.getString(R.string.item_select)
        val codeCreditCardSetting:Int = if(spTypeSetting.selectedItem != defaultItemSelected && spNameSetting.selectedItem.toString() != defaultItemSelected){
            spNameSetting.selectedItem.toString().split(".")[0].toInt()
        }else{
            0
        }
      return BuyCreditCardSettingDTO(0,0,codeCreditCardSetting, LocalDateTime.now(),1)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun cleanField() {
        etQuotesValue.text= "0.0"
        etProductName.editableText.clear();
        etProductValue.editableText.clear();
        etMonths.editableText.clear()
        btnSave.visibility = View.INVISIBLE
        dtBought.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        etTax.text = taxMonthly.toString()
        llTypeSetting.visibility = View.INVISIBLE
        if(cCSettingList.isNotEmpty()){
            llTypeSetting.visibility = View.VISIBLE
        }
        if(buyCCSDTO.isPresent){
            llNameSetting.visibility = View.VISIBLE
            spTypeSetting.setSelection(0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun validate(): Boolean {
        var valid: Boolean = true
        if (etProductName.text?.isBlank() == true) {
            valid = false
            etProductName.error = ("El nombre del producto o tienda esta vacia")
        }
        if (etProductValue.text?.isBlank() == true) {
            valid = false
            etProductValue.error = ("El  valor esta vacia")
        }
        if (etTax.text.isBlank()) {
            valid = false
            etTax.error = ("El tasa es vacia")
        }
        if (etMonths.text?.isBlank() == true) {
            valid = false
            etMonths.error = "El mes es vacio"
        }

        if (etMonths.text?.isNotBlank() == true && (etMonths.text.toString().toLong() <= 0L || etMonths.text.toString().toLong() > 72L)) {
            valid = false
            etMonths.error = ("La cantidad de meses es invalido, debe ser entre 1 y 72")
        }
        if(dtBought.text?.isBlank() == true){
            valid = false
            dtBought.error = ("La fecha se encuentra vacia")
        }
        val bought = dtBought.text.toString()
        val date = bought.split("/")
        if(date[2].toInt() < 2010 || date[2].toInt() > LocalDateTime.now().year){
            valid =  false
            dtBought.error = ("El año es invalido entre 2010 hasta el actual año en curso")
        }
        if(date[1].toInt()  < 1 || date[1].toInt() > 12){
            valid =  false
            dtBought.error = ("Mes ingresado es invalido entre 01-12")
        }
        if(date[0].toInt() < 1 || date[0].toInt() > 31){
            valid = false
            dtBought.error = ("El dia ingresado no es valido entre 01-31")
        }
        val defaultSelectItem = root.resources.getString(R.string.item_select)
        if(spTypeSetting.selectedItem != defaultSelectItem && spNameSetting.selectedItem == defaultSelectItem){
            valid =  false
            spNameSetting.setBackgroundColor(Color.RED)
        }else{
            spNameSetting.setBackgroundColor(Color.TRANSPARENT)
        }
        return valid
    }

    private fun calc(): Boolean {
        val defaultItemSelected = root.resources.getString(R.string.item_select)
        val optionSpecialTax = root.resources.getStringArray(R.array.CreditCardSettingType)[2]

        val value = if(etProductValue.text?.isNotBlank() == true){
            etProductValue.text.toString().toBigDecimal()
        }else{
            BigDecimal.ZERO
        }

        val period = if(etMonths.text?.isNotBlank() == true) {
            etMonths.text.toString().toLong()
        }else{
            0
        }
        Log.d(this.javaClass.name,"Calc $optionSpecialTax ${spTypeSetting.selectedItem} ${spNameSetting.isSelected} ${spNameSetting.selectedItem != defaultItemSelected} ${spTypeSetting.selectedItem == optionSpecialTax}")
        val tax:Double = if(spNameSetting.selectedItem != defaultItemSelected
                && spTypeSetting.selectedItem == optionSpecialTax){
            val settingId = spNameSetting.selectedItem.toString().split(".")[0].toInt()
            val taxValue:Double = cCSettingList.first{ it.id == settingId}.value.toDouble()
            Log.d(this.javaClass.name,"Get values $taxValue")
            etTax.text = taxValue.toString()
            taxValue
        }else {
            etTax.text.toString().toDouble()
        }
        if(period == 1L){
            llTax.visibility = View.INVISIBLE
        }else{
            llTax.visibility = View.VISIBLE
        }

        val responseQuote = calc.calc(value, period, tax)
        val responseiNteres = calcTax.calc(value, period, tax)

        responseQuote.let { quote ->
            responseiNteres.let { interes ->
                val format = DecimalFormat("#,###.00")
                var total = quote.add(interes)
                if(period == 1L){
                    total = quote;
                }
                etQuotesValue.text = format.format(total.setScale(2, RoundingMode.HALF_EVEN))
                etQuotesValue.visibility = View.VISIBLE
                btnSave.visibility = View.VISIBLE
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun map():CreditCardBoughtDTO{
        return CreditCardBoughtDTO(
            creditCardCode.get(),
            creditCardName,
            etProductName.text.toString(),
            etProductValue.text.toString().toBigDecimal(),
            etTax.text.toString().toDouble(),
            etMonths.text.toString().toInt(),
            DateUtils.getLocalDateTimeByString(dtBought),
            cutOffDate,
            LocalDateTime.now(),
            0,
            if (chRecurrent.isChecked) 1 else 0,
            KindBoughtEnum.BOUGHT.kind)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemDefaultSelected = view?.resources?.getString(R.string.item_select)
        when(parent?.id){
            R.id.spNameSettingBCC -> {
                if(spNameSetting.selectedItem != itemDefaultSelected) {
                    calc()
                }
            }
            R.id.spTypeSettingBCC -> {
                if(spTypeSetting.selectedItem != itemDefaultSelected) {
                    val list = cCSettingList.filter { it.type == spTypeSetting.selectedItem }
                        .map { "${it.id}. ${it.name}" }.toMutableList()
                    if(list.size > 0) {
                        list.add(0, root.resources?.getString(R.string.item_select)!!)
                        llNameSetting.visibility = View.VISIBLE
                        spNameSetting.adapter = ArrayAdapter(
                            root.context,
                            R.layout.spinner_simple,
                            R.id.tvValueBigSp,
                            list.toTypedArray()
                        )
                    }else if(view?.context != null && view?.resources != null && view?.resources?.getString(R.string.type_setting_does_not_record) != null){
                        Toast.makeText(view?.context,view?.resources?.getString(R.string.type_setting_does_not_record),Toast.LENGTH_LONG).show()
                    }
                }else{
                    llNameSetting.visibility = View.INVISIBLE
                }
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}