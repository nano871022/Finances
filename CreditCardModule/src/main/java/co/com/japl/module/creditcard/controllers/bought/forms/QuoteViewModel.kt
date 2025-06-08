package co.com.japl.module.creditcard.controllers.bought.forms

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.FormUIState
import co.com.japl.ui.utils.initialFieldState
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class QuoteViewModel constructor(private val codeCreditCard:Int,
                                 private val codeBought:Int,
                                 private val period:LocalDateTime,
                                 private val boughtSvc:IBoughtPort?,
                                 private val creditRateSvc:ITaxPort?,
                                 private val creditCardSvc:ICreditCardPort?,
                                 private val tagSvc:ITagPort?,
                                 private val creditCardSettingSvc:ICreditCardSettingPort?,
                                 private val buyCreditCardSettingSvc:IBuyCreditCardSettingPort?,
                                 private val navController: NavController?,
                                 private val prefs:Prefs) : ViewModel(){



    private var bought:CreditCardBoughtDTO? = null
    private var buySetting:BuyCreditCardSettingDTO? = null
    private var cutOffDate:LocalDateTime? = null

    private var validate = false
    private var taxDto:TaxDTO? = null
    private val settingList = mutableListOf<CreditCardSettingDTO>()

    val loading = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val isNew = mutableStateOf(true)

    val creditCardName = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { bought?.nameCreditCard = it }
    )

    val nameProduct = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { bought?.nameItem = it; validate() }
    )

    val valueProduct = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { bought?.valueItem = NumbersUtil.toBigDecimal(it); validate() }
    )
    val monthProduct = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it.trim()) && it.toInt() > 0},
        onValueChangeCallBack = { bought?.month = it.trim().toInt(); validate() }
    )

    val creditRate = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { bought?.interest = NumbersUtil.toBigDecimal(it).toDouble() }
    )
    val capitalValue = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )
    val dateBought = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && DateUtils.isDateValid(it)},
        onValueChangeCallBack = { bought?.boughtDate = DateUtils.toLocalDateTime2(it) }
    )
    val quoteValue = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )

    val interestValue = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )
    val creditRateKind = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { }
    )

    val tagSelected = initialFieldState<TagDTO?>(
        initialValue = null,
        list = arrayListOf(),
        validator = {it != null},
        onValueChangeCallBack = {}
    )

    val settingKind = initialFieldState<Pair<Int,String>?>(
        initialValue = null,
        list = arrayListOf(),
        validator = {it != null},
        onValueChangeCallBack = {}
    )
    val settingName = initialFieldState<Pair<Int,String>?>(
        initialValue = null,
        list = arrayListOf(),
        validator = {it != null},
        onValueChangeCallBack = {}
    )

    val recurrent = initialFieldState(
        initialValue = false,
        validator = {true},
        onValueChangeCallBack = {bought?.recurrent = if(it) 1 else 0}
    )

    private val _uiState = MutableStateFlow<FormUIState>(FormUIState.Current)
    val uiState : StateFlow<FormUIState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            main()
        }

    }

    fun clear(){

        nameProduct.reset("")
        valueProduct.reset("")
        monthProduct.reset("")
        creditRate.reset("")
        capitalValue.reset("")
        quoteValue.reset("")
        interestValue.reset("")
        isNew.value = true

        loading.value = true

        validate = false

        bought = null

    }

    private fun getBuyCreditCardSetting(codeBought:Int,codeCreditCardSetting:Int):BuyCreditCardSettingDTO{
        val dto = buySetting?.copy(
            codeBuyCreditCard = codeBought,
            codeCreditCardSetting = codeCreditCardSetting
        ) ?: BuyCreditCardSettingDTO(
             id=0,
            codeBuyCreditCard=codeBought,
            codeCreditCardSetting=codeCreditCardSetting,
            create= LocalDateTime.now(),
            active=1
        )

        return dto

    }

    fun create(){
        if(validate) {
            boughtSvc?.let {
                val id = it.create(bought!!,prefs.simulator)
                if(id > 0) {
                    buyCreditCardSettingSvc?.let{svc->
                        settingName.value?.let{svc.createOrUpdate(getBuyCreditCardSetting(id,it.first))
                        }?:buySetting?.let {svc.delete(it.id)}
                    }
                    tagSvc?.let { svc ->
                        tagSelected.value?.let{svc.createOrUpdate(it.id,id)}
                    }
                    navController?.let { navController ->
                        Toast.makeText(
                            navController.context,
                            R.string.toast_successful_insert,
                            Toast.LENGTH_SHORT
                        ).show().also {
                            navController.popBackStack()
                        }
                    }
                }else {
                    Toast.makeText(
                        navController?.context,
                        R.string.toast_unsuccessful_insert,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun createAndBack(){
        validate()
        if(validate) {
            boughtSvc?.let {
                val id = it.create(bought!!,prefs.simulator)
                if(id > 0) {
                    buyCreditCardSettingSvc?.let{svc->
                        settingName.value?.let{svc.createOrUpdate(getBuyCreditCardSetting(id,it.first))
                        }?:buySetting?.let {svc.delete(it.id)}
                    }
                    tagSvc?.let { svc ->
                        tagSelected.value?.let{svc.createOrUpdate(it.id,id)}
                    }
                    navController?.let { navController ->
                        Toast.makeText(
                            navController.context,
                            R.string.toast_successful_insert,
                            Toast.LENGTH_SHORT
                        ).show().also {
                            clear()
                        }
                    }
                }else {
                    Toast.makeText(
                        navController?.context,
                        R.string.toast_unsuccessful_insert,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun update(){
        validate()
        if(validate) {
            boughtSvc?.let {
                if(it.update(bought!!,prefs.simulator)) {
                    buyCreditCardSettingSvc?.let{svc->
                        settingName.value?.let{bought?.let{dto->svc.createOrUpdate(getBuyCreditCardSetting(dto.id,it.first))}
                        }?:buySetting?.let {svc.delete(it.id)}
                    }
                    tagSvc?.let { svc ->
                        tagSelected.value?.let{svc.createOrUpdate(it.id,codeBought)}
                    }
                    navController?.let { navController ->
                        Toast.makeText(
                            navController.context,
                            R.string.toast_successful_update,
                            Toast.LENGTH_SHORT
                        ).show().also {
                            navController.popBackStack()
                        }
                    }
                }else {
                    Toast.makeText(
                        navController?.context,
                        R.string.toast_dont_successful_update,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun createTag(tagName:String):TagDTO?=
         tagSvc?.let {
            val tag = TagDTO(
                id = 0,
                name = tagName,
                active = true,
                create = LocalDate.now()
            )
            val id =  it.create(tag)
            return tag.copy(id = id)
        }

        fun settingName(codeSettingKind:Int){
            settingName.list.clear()
            settingList.filter { it.type == settingKind.value?.second }?.map { Pair(it.id,it.name) }?.forEach(settingName.list::add)
        }


    fun creditRateEmpty(){
        if(taxDto == null){
            navController?.let {navController->
                Toast.makeText(navController.context,R.string.toast_credit_rate_is_not_setting,Toast.LENGTH_SHORT).show().also {
                    navController.popBackStack()
                }
            }
        }
    }

    fun deleteTag(codeTag:Int){
        if(tagSvc?.delete(codeTag) == true){
            navController?.let {navController->
                Toast.makeText(navController.context,R.string.toast_successful_deleted,Toast.LENGTH_SHORT).show().also {
                    loadTags()
                }
            }
        }
    }

    fun validate(){
        var value = true
        var month = true

        validate = true
        nameProduct.validate().not().or(nameProduct.error.value).takeIf { it }?.let{validate = false}

        valueProduct.validate().not().or(valueProduct.error.value).takeIf { it }?.let { validate = false; value = false }

        monthProduct.validate().not().or(monthProduct.error.value).takeIf { it }?.let { validate = false; month = false}

        dateBought.validate().not().or(dateBought.error.value).takeIf { it }?.let { validate = false }

        calculateValues(month,value)

        createNewDTOs()

    }

    private fun createNewDTOs(){
        if(validate && taxDto != null){
            bought = CreditCardBoughtDTO(
                codeCreditCard = codeCreditCard,
                id = codeBought,
                nameItem = nameProduct.value,
                valueItem = NumbersUtil.toBigDecimal(valueProduct.value),
                month = NumbersUtil.toLong(monthProduct.value).toInt(),
                boughtDate = DateUtils.toLocalDateTime2(dateBought.value),
                createDate = LocalDateTime.now(),
                endDate = LocalDateTime.MAX,
                cutOutDate = period,
                interest = NumbersUtil.toBigDecimal(creditRate.value).toDouble(),
                kind = KindInterestRateEnum.CREDIT_CARD,
                kindOfTax = taxDto?.kindOfTax!!,
                nameCreditCard = creditCardName.value,
                recurrent = if(recurrent.value) 1 else 0
            )

            settingName.value?.let{
                buySetting = BuyCreditCardSettingDTO(
                    id = buySetting?.id?:0,
                    codeBuyCreditCard = codeBought,
                    codeCreditCardSetting = it.first,
                    create = LocalDateTime.now(),
                    active=1
                )
            }
        }
    }

    private fun calculateValues(month:Boolean,value: Boolean){
        if(month && value){
            val month = NumbersUtil.toLong(monthProduct.value).toInt()
            val value = NumbersUtil.toBigDecimal(valueProduct.value)
            boughtSvc?.let {
                taxDto?.let {taxDto->
                    val quoteBought = it.quoteValue(
                        taxDto?.id!!,
                        month.toShort(),
                        value.toDouble(),
                        taxDto?.kindOfTax!!,
                        taxDto?.kind!!
                    )
                    val interestBought = it.interestValue(
                        taxDto?.id!!,
                        month.toShort(),
                        value.toDouble(),
                        taxDto?.kindOfTax!!,
                        taxDto?.kind!!
                    )
                    val capitalBought = it.capitalValue(
                        taxDto?.id!!,
                        month.toShort(),
                        value.toDouble(),
                        taxDto?.kindOfTax!!,
                        taxDto?.kind!!
                    )
                    quoteValue.onValueChange(NumbersUtil.COPtoString(quoteBought))
                    interestValue.onValueChange(NumbersUtil.COPtoString(interestBought))
                    capitalValue.onValueChange(NumbersUtil.COPtoString(capitalBought))
                }
            }

        }
    }

    fun main()= runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
        loading.value = false
    }

    suspend fun execute(){
        progress.floatValue = 0.2f
        dateBought.onValueChange(DateUtils.localDateToStringDate(LocalDate.now()))
        progress.floatValue = 0.3f
        creditCardSvc?.let {
            it.getCreditCard(codeCreditCard)?.let {
                creditCardName.onValueChange(it.name)
                DateUtils.cutOff(it.cutOffDay, period.toLocalDate())?.let {
                    cutOffDate = it
                }
                progress.floatValue = 0.4f
            }
        }

        creditRateSvc?.let {
            it.get(codeCreditCard,cutOffDate?.monthValue!!,cutOffDate?.year!!,KindInterestRateEnum.CREDIT_CARD)?.let {
                taxDto = it
                creditRate.onValueChange(it.value.toString())
                creditRateKind.onValueChange( it.kindOfTax?.getName()?: KindOfTaxEnum.MONTHLY_EFFECTIVE.name)
                progress.floatValue = 0.5f
            }
        }

        boughtSvc?.let {
            if(codeBought > 0) {
                it.getById(codeBought, prefs.simulator)?.let {
                    bought = it
                    isNew.value = false

                    dateBought.onValueChange(DateUtils.localDateTimeToStringDate(it.boughtDate))
                    nameProduct.onValueChange(it.nameItem)
                    valueProduct.onValueChange(NumbersUtil.toString(it.valueItem))
                    monthProduct.onValueChange(it.month.toString())
                    recurrent.onValueChange( it.recurrent == "1".toShort())
                    validate()

                    progress.floatValue = 0.6f
                }
            }
        }

        loadTags()

        loadSettings()

    }

    private fun loadSettings(){
        creditCardSettingSvc?.let {
            it.getAll(codeCreditCard)?.let {
                settingList.clear()
                settingKind.list.clear()
                settingList.addAll(it)
                it.map {it.type}.distinct()
                    .mapIndexed {index, type ->
                        Pair(index, type)
                    }.forEach(settingKind.list::add)
                progress.floatValue = 0.8f
            }
        }

        buyCreditCardSettingSvc?.let{
            it.get(codeBought)?.let{dto->
                buySetting = dto
                settingKind.onValueChange( settingList.filter { it.id == dto.codeCreditCardSetting }?.map {setting->
                    settingKind.list.first { it?.second == setting.type }
                }?.first())
                settingName.onValueChange(settingList.filter { it.id == dto.codeCreditCardSetting }?.map {Pair(it.id,it.name)}?.first())
                progress.floatValue = 0.9f
            }
        }
    }

    private fun loadTags(){
        tagSvc?.let {
            it.getAll()?.let {
                tagSelected.list.clear()
                tagSelected.list.addAll(it)
                progress.floatValue = 0.7f
            }
            it.get(codeBought)?.let {
                tagSelected.onValueChange( it )
                progress.floatValue = 0.75f
            }
        }
    }

}

