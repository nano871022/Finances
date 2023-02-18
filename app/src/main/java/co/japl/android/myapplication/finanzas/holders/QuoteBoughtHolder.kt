package co.japl.android.myapplication.finanzas.holders

import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class QuoteBoughtHolder(var root:View) : IHolder<CreditCardBoughtDTO>{
    lateinit var etProductName: EditText
    lateinit var etProductValue: EditText
    lateinit var etTax: TextView
    lateinit var etMonths: EditText
    lateinit var etQuotesValue: TextView
    lateinit var tvCardAssing: TextView
    lateinit var dtBought: EditText
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardBoughtDTO {
        return map()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downLoadBuyCreditCardSetting():BuyCreditCardSettingDTO{
        val codeCreditCardSetting = cCSettingList.first{ it.name == spNameSetting.selectedItem.toString()}.id
      return BuyCreditCardSettingDTO(0,0,codeCreditCardSetting, LocalDateTime.now(),1)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun cleanField() {
        etQuotesValue.editableText.clear();
        etProductName.editableText.clear();
        etProductValue.editableText.clear();
        etMonths.editableText.clear()
        etTax.setBackgroundColor(Color.WHITE)
        etMonths.setBackgroundColor(Color.WHITE)
        btnSave.visibility = View.INVISIBLE
        etQuotesValue.setBackgroundColor(Color.WHITE)
        etProductName.setBackgroundColor(Color.WHITE)
        dtBought.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        dtBought.setBackgroundColor(Color.WHITE)
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
        if (etProductName.text.isBlank()) {
            valid = valid and false
            etProductName.error = ("El nombre del producto o tienda esta vacia")
        }
        if (etProductValue.text.isBlank()) {
            valid = valid and false
            etProductValue.error = ("El  valor esta vacia")
        }
        if (etTax.text.isBlank()) {
            valid = valid and false
            etTax.error = ("El tasa es vacia")
        }
        if (etMonths.text.isBlank()) {
            valid = valid and false
            etMonths.error = "El mes es vacio"
        }

        if (etMonths.text.isNotBlank() && (etMonths.text.toString().toLong() <= 0L || etMonths.text.toString().toLong() > 72L)) {
            valid = valid and false
            etMonths.error = ("La cantidad de meses es invalido, debe ser entre 1 y 72")
        }
        if(dtBought.text.isBlank()){
            valid = valid and false
            dtBought.error = ("La fecha se encuentra vacia")
        }
        val bought = dtBought.text.toString()
        val date = bought.split("/")
        if(date[2].toInt() < 2010 || date[2].toInt() > LocalDateTime.now().year){
            valid = valid and false
            dtBought.error = ("El año es invalido entre 2010 hasta el actual año en curso")
        }
        if(date[1].toInt()  < 1 || date[1].toInt() > 12){
            valid = valid and false
            dtBought.error = ("Mes ingresado es invalido entre 01-12")
        }
        if(date[0].toInt() < 1 || date[0].toInt() > 31){
            valid = valid and false
            dtBought.error = ("El dia ingresado no es valido entre 01-31")
        }
        if(spTypeSetting.isSelected && !spNameSetting.isSelected){
            valid = valid and false
            spNameSetting.setBackgroundColor(Color.RED)
        }else{
            spNameSetting.setBackgroundColor(Color.TRANSPARENT)
        }
        return valid
    }

    private fun calc(): Boolean {
        val value = etProductValue.text.toString().toBigDecimal()
        val period = etMonths.text.toString().toLong()
        val tax:Double = if(spNameSetting.isSelected){
            cCSettingList.first {  it.name == spNameSetting.selectedItem.toString() }.value.toDouble()
        }else {
            etTax.text.toString().toDouble()
        }
        if(period == 1L){
            println("Invisible")
            llTax.visibility = View.INVISIBLE
        }else{
            llTax.visibility = View.VISIBLE
            println("Visible")
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


}