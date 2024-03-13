package co.com.japl.module.creditcard.controllers.bought.forms

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
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

    val creditCardName = mutableStateOf("")
    val nameProduct = mutableStateOf("")
    val errorNameProduct = mutableStateOf(false)
    val valueProduct = mutableStateOf("")
    val errorValueProduct = mutableStateOf(false)
    val monthProduct = mutableStateOf("")
    val errorMonthProduct = mutableStateOf(false)
    val creditRate = mutableStateOf("")
    val capitalValue = mutableStateOf("")
    val dateBought = mutableStateOf("")
    val errorDateBought = mutableStateOf(false)
    val quoteValue = mutableStateOf("")
    val interestValue = mutableStateOf("")
    val creditRateKind = mutableStateOf("")

    val tagList = mutableStateListOf<TagDTO>()
    val tagSelected = mutableStateOf<TagDTO?>(null)

    val settingKind = mutableStateOf<Pair<Int,String>?>(null)
    val settingName = mutableStateOf<Pair<Int,String>?>(null)
    val settingKindListState = mutableStateListOf<Pair<Int, String>>()
    val settingNameListState = mutableStateListOf<Pair<Int, String>>()

    val recurrent = mutableStateOf(false)


    fun clear(){

        nameProduct.value = ""
        valueProduct.value = ""
        monthProduct.value = ""
        creditRate.value = ""
        capitalValue.value = ""
        isNew.value = true
        quoteValue.value = ""
        interestValue.value = ""

        errorMonthProduct.value = false
        errorValueProduct.value = false
        errorNameProduct.value = false
        errorDateBought.value = false

        loading.value = true

        validate = false

        bought = null

    }

    fun create(){
        if(validate) {
            boughtSvc?.let {
                val id = it.create(bought!!,prefs.simulator)
                if(id > 0) {
                    buyCreditCardSettingSvc?.let{svc->
                        buySetting?.let{svc.createOrUpdate(it.copy(codeBuyCreditCard = id))}
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
        if(validate) {
            boughtSvc?.let {
                val id = it.create(bought!!,prefs.simulator)
                if(id > 0) {
                    buyCreditCardSettingSvc?.let{svc->
                        buySetting?.let{svc.createOrUpdate(it.copy(codeBuyCreditCard = id))}
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
        if(validate) {
            boughtSvc?.let {
                if(it.update(bought!!,prefs.simulator)) {
                    buyCreditCardSettingSvc?.let{svc->
                        buySetting?.let{svc.createOrUpdate(it)}
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
            settingNameListState.clear()
            settingList.filter { it.type == settingKind.value?.second }?.map { Pair(it.id,it.name) }?.forEach(settingNameListState::add)
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

    fun validate(){
        var value = true
        var month = true

        validate = true
        nameProduct.value.takeIf { it.isBlank() }?.let {
            errorNameProduct.value = true
            validate = false
        }?:errorNameProduct.takeIf { it.value }?.let{ it.value = false}

        valueProduct.value.takeIf { it.isBlank() || NumbersUtil.isNumber(it).not() }?.let {
            errorValueProduct.value = true
            validate = false
            value = false
        }?:errorValueProduct.takeIf { it.value }?.let { it.value = false}

        monthProduct.value.takeIf { it.isBlank() || NumbersUtil.isNumber(it).not() }?.let {
            errorMonthProduct.value = true
            validate = false
            month = false
        }?:errorMonthProduct.takeIf { it.value }?.let { it.value = false}

        dateBought.value.takeIf { it.isBlank() || DateUtils.isDateValid(it).not() }?.let {
            errorDateBought.value = true
            validate = false
        }?:errorDateBought.takeIf { it.value }?.let { it.value = false}

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
                    quoteValue.value = NumbersUtil.COPtoString(quoteBought)
                    interestValue.value = NumbersUtil.COPtoString(interestBought)
                    capitalValue.value = NumbersUtil.COPtoString(capitalBought)
                }
            }

        }

        if(validate && taxDto != null){
            bought = CreditCardBoughtDTO(
                codeCreditCard = codeCreditCard,
                id = codeBought,
                nameItem = nameProduct.value,
                valueItem = NumbersUtil.toBigDecimal(valueProduct.value),
                month = NumbersUtil.toLong(monthProduct.value).toInt(),
                boughtDate = DateUtils.toLocalDateTime(dateBought.value),
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

    fun main()= runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
        loading.value = false
    }

    suspend fun execute(){
        progress.floatValue = 0.2f
        dateBought.value = DateUtils.localDateToStringDate(LocalDate.now())
        progress.floatValue = 0.3f
        creditCardSvc?.let {
            it.getCreditCard(codeCreditCard)?.let {
                creditCardName.value = it.name
                DateUtils.cutOff(it.cutOffDay, period.toLocalDate())?.let {
                    cutOffDate = it
                }
                progress.floatValue = 0.4f
            }
        }

        creditRateSvc?.let {
            it.get(codeCreditCard,cutOffDate?.monthValue!!,cutOffDate?.year!!,KindInterestRateEnum.CREDIT_CARD)?.let {
                taxDto = it
                creditRate.value = it.value.toString()
                creditRateKind.value = it.kindOfTax?.getName()?:"EM"
                progress.floatValue = 0.5f
            }
        }

        boughtSvc?.let {
            if(codeBought > 0) {
                it.getById(codeBought, prefs.simulator)?.let {
                    bought = it
                    isNew.value = false

                    dateBought.value = DateUtils.localDateTimeToStringDate(it.boughtDate)
                    nameProduct.value = it.nameItem
                    valueProduct.value = NumbersUtil.toString(it.valueItem)
                    monthProduct.value = it.month.toString()
                    recurrent.value = it.recurrent == "1".toShort()
                    validate()

                    progress.floatValue = 0.6f
                }
            }
        }

        tagSvc?.let {
            it.getAll()?.let {
                tagList.clear()
                tagList.addAll(it)
                progress.floatValue = 0.7f
            }
            it.get(codeBought)?.let {
                tagSelected.value = it
                progress.floatValue = 0.75f
            }
        }

        creditCardSettingSvc?.let {
            it.getAll(codeCreditCard)?.let {
                settingList.clear()
                settingKindListState.clear()
                settingList.addAll(it)
                it.map {it.type}.distinct()
                    .mapIndexed {index, type ->
                        Pair(index, type)
                    }.forEach(settingKindListState::add)
                progress.floatValue = 0.8f
            }
        }

        buyCreditCardSettingSvc?.let{
            it.get(codeBought)?.let{dto->
                buySetting = dto
                settingKind.value = settingList.filter { it.id == dto.codeCreditCardSetting }?.map {setting->
                    settingKindListState.first { it.second == setting.type }
                }?.first()
                settingName.value= settingList.filter { it.id == dto.codeCreditCardSetting }?.map {Pair(it.id,it.name)}?.first()
                progress.floatValue = 0.9f
            }
        }
    }


}