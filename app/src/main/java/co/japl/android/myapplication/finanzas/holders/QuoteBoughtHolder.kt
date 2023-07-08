package co.japl.android.myapplication.finanzas.holders
import co.japl.android.myapplication.finanzas.holders.validations.*

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterest
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.ISpinnerHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSettingSvc
import co.japl.android.myapplication.finanzas.enums.KindBoughtEnum
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class QuoteBoughtHolder(var root:View, val supportManager:FragmentManager) : IHolder<CreditCardBoughtDTO>,
    ISpinnerHolder<QuoteBoughtHolder> {
    lateinit var etProductName: TextInputEditText
    lateinit var etProductValue: TextInputEditText
    lateinit var etTax: TextView
    lateinit var etMonths: TextInputEditText
    lateinit var etQuotesValue: TextView
    lateinit var tvCardAssing: TextView
    lateinit var dtBought: TextInputEditText
    lateinit var btnClear: Button
    lateinit var llTax : LinearLayout
    private lateinit var btnSave: MaterialButton
    lateinit var chRecurrent: CheckBox
    private lateinit var creditCardBought: CreditCardBoughtDTO
    lateinit var spTypeSetting: MaterialAutoCompleteTextView
    lateinit var spNameSetting: MaterialAutoCompleteTextView
    lateinit var llTypeSetting: TextInputLayout
    lateinit var llNameSetting: TextInputLayout
    private lateinit var  nameSettingDialog:AlertDialog
    private lateinit var  typeSettingDialog:AlertDialog
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
    private val itemDefaultSelected = root.resources?.getString(R.string.item_select)
    private val delayed = 600L
    private val delayedEdit = 1000L


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
            btnClear  = it.findViewById(R.id.btnClearBought)
            btnSave = it.findViewById(R.id.btnSaveBought)
            onFocus()
            onClick(actions)
            visibility()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onFocus(){
        dtBought.isClickable = true
        dtBought.isFocusable = false

        spTypeSetting.isFocusable = false
        spNameSetting.isFocusable = false
        val handlerProduct = Handler(Looper.getMainLooper())
        etProductName.addTextChangedListener  {
            handlerProduct.removeCallbacksAndMessages(null)
            handlerProduct.postDelayed({
                validate() && calc()
            },delayed)
        }
        val handlerValue = Handler(Looper.getMainLooper())
        val handlerValueFormatter = Handler(Looper.getMainLooper())
        etProductValue.addTextChangedListener  (object:TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handlerValue.removeCallbacksAndMessages(null)
                handlerValue.postDelayed({
                    validate() && calc()
                }, delayed)
            }

            override fun afterTextChanged(s: Editable?) {
                handlerValueFormatter.removeCallbacksAndMessages(null)
                handlerValueFormatter.postDelayed({
                if (s?.isNotBlank() == true) {
                    etProductValue.removeTextChangedListener(this)
                    etProductValue.setText(NumbersUtil.toString(etProductValue))
                    etProductValue.addTextChangedListener (this)
                }
                },delayedEdit)
            }
        })
        val handlerMonths = Handler(Looper.getMainLooper())
        etMonths.addTextChangedListener {
            handlerMonths.removeCallbacksAndMessages(null)
            handlerMonths.postDelayed({
                validate() && calc()
            },delayed)
        }
    }

    private fun onClick(actions: View.OnClickListener?){
        spTypeSetting.setOnClickListener { spTypeSetting.showDropDown() }
        spNameSetting.setOnClickListener { spNameSetting.showDropDown() }
        btnSave.setOnClickListener(actions)
        btnClear.setOnClickListener(actions)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun visibility(){
        val dataPicker = MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).setTitleText(root.resources.getString(R.string.bought_date_time)).setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
        llTypeSetting.visibility = View.INVISIBLE
        llNameSetting.visibility = View.INVISIBLE
        dtBought.setOnClickListener{ clicked->
            dataPicker.show(supportManager,"DT_BOUGHT")
            dataPicker.addOnPositiveButtonClickListener {
                var date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                date = date.plusDays(1)
                dtBought.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: CreditCardBoughtDTO) {
        creditCardBought = values
        Log.d(javaClass.name,"$creditCardBought")
        taxMonthly = values.interest
        creditCardCode = Optional.ofNullable(values.codeCreditCard)
        creditCardName = values.nameCreditCard
        cutOffDate = values.cutOutDate
        dtBought.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        etTax.text = "${taxMonthly.toString()} % ${values.kindOfTax}"
        tvCardAssing.text = values.nameCreditCard
        buyCCSDTO = buyCCSsvc.get(values.id)
        cCSettingList = cCSettingsvc.getAll(values.codeCreditCard)

        if(cCSettingList.isNotEmpty()){
            llTypeSetting.visibility = View.VISIBLE
            createTypeSettingDialog(values.kindOfTax)
            spTypeSetting.setOnClickListener { typeSettingDialog.show() }
        }
        if(values.id > 0){
            dtBought.setText(DateUtils.localDateToString(values.boughtDate.toLocalDate()))
            etProductName.setText(values.nameItem)
            etProductValue.setText(NumbersUtil.toString(values.valueItem))
            etMonths.setText(values.month.toString())
            calc()
            chRecurrent.isChecked = values.recurrent.toInt() == 1
        }

    }

    private fun createTypeSettingDialog(kindOfTax:String){
        val builder = AlertDialog.Builder(root.context)
        with(builder){
            val items = root.resources.getStringArray(R.array.CreditCardSettingType)
            setItems(items){ _,position ->
                val value = items[position]
                spTypeSetting.setText(value)
                if(value != itemDefaultSelected) {
                    val list = cCSettingList.filter { it.type == spTypeSetting.text.toString() }
                        .map { "${it.id}. ${it.name}" }.toMutableList()
                    if(list.size > 0) {
                        llNameSetting.visibility = View.VISIBLE
                        createDialog(list)
                        spNameSetting.setOnClickListener { nameSettingDialog.show() }

                    }else if(root.context != null && root.resources != null && root.resources?.getString(R.string.type_setting_does_not_record) != null){
                        Toast.makeText(root.context,root.resources?.getString(R.string.type_setting_does_not_record),Toast.LENGTH_LONG).show()
                    }
                }else{
                    llNameSetting.visibility = View.INVISIBLE
                    etTax.text = "$taxMonthly % $kindOfTax"
                }
            }
        }
        typeSettingDialog = builder.create()
    }

    private fun createDialog(list:MutableList<String>){
        val builder = AlertDialog.Builder(root.context)
        with(builder){
            setItems(list.toTypedArray()){ _,position->
                val value = list[position]
                if(value != itemDefaultSelected) {
                    spNameSetting.setText(value)
                    calc()
                }
            }
        }
       nameSettingDialog = builder.create()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): CreditCardBoughtDTO {
        return map()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downLoadBuyCreditCardSetting():BuyCreditCardSettingDTO{
        val defaultItemSelected = root.resources.getString(R.string.item_select)
        val codeCreditCardSetting:Int = if(spTypeSetting.text.isNotBlank() &&    spTypeSetting.text.toString() != defaultItemSelected && spNameSetting.text.toString() != defaultItemSelected){
            spNameSetting.text.toString().split(".")[0].toInt()
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
        etTax.text = "$taxMonthly % ${creditCardBought.kindOfTax}"
        llTypeSetting.visibility = View.INVISIBLE
        if(::cCSettingList.isInitialized && cCSettingList.isNotEmpty()){
            llTypeSetting.visibility = View.VISIBLE
        }
        if(::buyCCSDTO.isInitialized && buyCCSDTO.isPresent){
            llNameSetting.visibility = View.VISIBLE
            spTypeSetting.setSelection(0)
        }
    }

    private val validations by lazy{
        arrayOf(
            etProductName set R.string.name_or_product_is_empty `when` { text().isBlank()}
            , etProductValue set R.string.value_is_empty `when` { text().isBlank()}
            , etMonths set R.string.months_is_empty `when` { text().isBlank()}
            , etMonths set R.string.months_invalids `when` { text().isNotBlank() && (text().toLong() <= 0L || text().toLong() > 72L)}
            , dtBought set R.string.bought_date_is_empty `when` { text().isBlank()}
            , dtBought set R.string.bought_year_invalid `when` { text().isNotBlank() && (text().split("/")[2].toInt() < 2010 || text().split("/")[2].toInt() > LocalDateTime.now().year)}
            , dtBought set R.string.bought_month_invalid `when` { text().isNotBlank() && (text().split("/")[1].toInt() < 1 || text().split("/")[1].toInt() > 12)}
            , dtBought set R.string.bought_day_invalid `when` { text().isNotBlank() && (text().split("/")[0].toInt() < 1 || text().split("/")[0].toInt() > 31)}
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun validate(): Boolean {
        var valid: Boolean = true
        validations.firstInvalid{requestFocus()}.notNull { valid = true }

        if (etTax.text.isBlank()) {
            valid = false
            etTax.error = ("El tasa es vacia")
        }

        val defaultSelectItem = root.resources.getString(R.string.item_select)
        if(spTypeSetting.text.toString() != defaultSelectItem && spNameSetting.text.toString() == defaultSelectItem){
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

        val value = NumbersUtil.toBigDecimal(etProductValue)

        val period = if(etMonths.text?.isNotBlank() == true) {
            etMonths.text.toString().toLong()
        }else{
            0
        }
        Log.d(this.javaClass.name,"Calc $optionSpecialTax ${spTypeSetting.text.toString()} ${spNameSetting.isSelected} ${spNameSetting.text.toString() != defaultItemSelected} ${spTypeSetting.text.toString() == optionSpecialTax}")
        val tax:Double = if(spNameSetting.text.toString() != defaultItemSelected
                && spTypeSetting.text.toString() == optionSpecialTax){
            val settingId = spNameSetting.text.toString().split(".")[0].toInt()
            val taxValue:Double = cCSettingList.first{ it.id == settingId}.value.toDouble()
            Log.d(this.javaClass.name,"Get values $taxValue")
            etTax.text = "${taxValue.toString()} % ${creditCardBought.kindOfTax}"
            taxValue
        }else {
           getTax()
        }
        if(period == 1L){
            llTax.visibility = View.INVISIBLE
        }else{
            llTax.visibility = View.VISIBLE
        }

        val responseQuote = calc.calc(value, period, tax, KindOfTaxEnum.valueOf(creditCardBought.kindOfTax))
        val responseiNteres = calcTax.calc(value, period, tax, KindOfTaxEnum.valueOf(creditCardBought.kindOfTax))

        responseQuote.let { quote ->
            responseiNteres.let { interes ->
                var total = quote.add(interes)
                if(period == 1L){
                    total = quote;
                }
                etQuotesValue.text = NumbersUtil.toString(total.setScale(2, RoundingMode.HALF_EVEN))
                etQuotesValue.visibility = View.VISIBLE
                btnSave.visibility = View.VISIBLE
            }
        }
        return true
    }

    private fun getTax():Double{
        val value = etTax?.text?.toString()?.replace("%","")
            ?.replace(Regex("[^\\d.]"),"")?.trim()
        if(value?.isNotBlank() == true){
            return value?.toDouble()!!
        }
            return 0.0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun map():CreditCardBoughtDTO{
        val endDate = LocalDateTime.of(9999,12,31,23,59,59)
        return CreditCardBoughtDTO(
            creditCardCode.get(),
            creditCardName,
            etProductName.text.toString(),
            NumbersUtil.toBigDecimal(etProductValue),
            getTax(),
            etMonths.text.toString().toInt(),
            DateUtils.getLocalDateTimeByString(dtBought),
            cutOffDate,
            LocalDateTime.now(),
            endDate,
            creditCardBought?.id ?: 0,
            if (chRecurrent.isChecked) 1 else 0,
            KindBoughtEnum.BOUGHT.kind,
            creditCardBought.kindOfTax)
    }

    override fun lists(fn: ((QuoteBoughtHolder) -> Unit)?) {
        fn?.invoke(this)
    }

}