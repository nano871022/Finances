package co.com.japl.module.paid.controllers.projections.forms

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import co.com.japl.module.paid.R
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.FieldState
import co.com.japl.ui.utils.initialFieldState
import co.com.japl.ui.utils.NumbersUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate

@HiltViewModel(assistedFactory = ProjectionFormViewModel.Factory::class)
class ProjectionFormViewModel @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val saveStateHandler: SavedStateHandle,
    @Assisted public val id:Int?=null,
    private val projectionSvc: IProjectionFormPort,
    @Assisted private val navController: NavController,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): ViewModel(){

    @AssistedFactory
    interface Factory {
        fun create(context: Context, saveStateHandler: SavedStateHandle, id: Int?, navController: NavController): ProjectionFormViewModel
    }

    val loaderStatus = mutableStateOf(true)
    val disableSaveStatus = mutableStateOf(true)
    private val periodsOpt = KindPaymentsEnums.entries.map{ Pair(it.month,context.getString(it.title) ) }
    val hostState: SnackbarHostState = SnackbarHostState()
    private val projection = MutableStateFlow(ProjectionDTO(
        id= 0,
        create= LocalDate.now(),
         end= LocalDate.now(),
         name= "",
         value= BigDecimal.ZERO,
         quote= BigDecimal.ZERO,
         monthsLeft = 0,
         amountSaved = BigDecimal.ZERO,
         type= KindPaymentsEnums.MONTHLY
    ))

    val datePayment: FieldState<LocalDate> = initialFieldState<LocalDate>(
        saveStateHandler!!,
        "FORM_DATE_PAYMENT",
        initialValue = projection.value.end,
        formatter = {DateUtils.toLocalDate(it)},
        validator = { !it.isBefore(LocalDate.now()) },
        onValueChangeCallBack = {date->
            projection.update {
                it.copy(end = date)
            }
            validate()
            quoteCalculation()
        }
    )
    val period: FieldState<Pair<Int,String>> = initialFieldState<Pair<Int,String>>(
        saveStateHandler!!,
        "FORM_PERIOD",
        initialValue = periodsOpt.find{ it.first == projection.value.type.month}?:periodsOpt.first(),
        validator = { KindPaymentsEnums.existIndex( it.first ) },
        list = periodsOpt,
        onValueChangeCallBack = { newPeriod ->
            projection.update {
                it.copy(type = KindPaymentsEnums.findByIndex(newPeriod.first))
            }
            validate()
            quoteCalculation()
        }
    )
    val name: FieldState<String> = initialFieldState<String>(
        saveStateHandler!!,
        "FORM_NAME",
        initialValue = projection.value.name,
        validator = { it.isNotBlank() },
        onValueChangeCallBack = { newName ->
            projection.update{
                it.copy(name = newName)
            }
            validate()
        })
    val value: FieldState<BigDecimal> = initialFieldState<BigDecimal>(
        saveStateHandler!!,
        "FORM_VALUE",
        initialValue = projection.value.value,
        formatter = { if(NumbersUtil.isNumber(it)) BigDecimal(it) else BigDecimal.ZERO },
        validator = { it > BigDecimal.ZERO },
        onValueChangeCallBack = { newValue ->
            projection.update{
                it.copy(value = newValue)
            }
            validate()
            quoteCalculation()
        })

    val quote: FieldState<BigDecimal> = initialFieldState<BigDecimal>(
        saveStateHandler!!,
        "FORM_QUOTE",
        initialValue = projection.value.quote,
        formatter = { if(NumbersUtil.isNumber(it)) BigDecimal(it) else BigDecimal.ZERO },
        validator = { it > BigDecimal.ZERO },
        onValueChangeCallBack = { newValue ->
            projection.update{
                it.copy(quote = newValue)
            }
            validate()
        })

    init {
        main()
    }

    fun save()= viewModelScope.launch {
        if(disableSaveStatus.value.not()){
            projectionSvc?.let{
                withContext(ioDispatcher) {
                    Log.d(javaClass.simpleName,"DTO:: ${projection.value}")
                    it.save(projection.value)
                }.let{resp->
                        if (resp) {
                            hostState.showSnackbar(
                                message = context.getString(R.string.save_project_success),
                                actionLabel = context.getString(R.string.close),
                                duration = SnackbarDuration.Short
                            ).also {
                                navController?.let {
                                    it.popBackStack()
                                }
                            }
                        } else {
                            hostState.showSnackbar(
                                message = context.getString(R.string.save_project_error),
                                actionLabel = context.getString(R.string.close),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }


    fun update() = viewModelScope.launch {
        if(disableSaveStatus.value.not()){
            projectionSvc?.let{
                withContext(ioDispatcher) {
                    it.save(projection.value)
                }.let{ resp->
                        if (resp) {
                            hostState.showSnackbar(
                                message = context.getString(R.string.update_project_success),
                                actionLabel = context.getString(R.string.close),
                                duration = SnackbarDuration.Short
                            ).also {
                                navController?.let {
                                    it.popBackStack()
                                }
                            }
                        } else {
                            hostState.showSnackbar(
                                message = context.getString(R.string.update_project_error),
                                actionLabel = context.getString(R.string.close),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }


    fun validate(){
        datePayment.validate()
        period.validate()
        name.validate()
        value.validate()
        quote.validate()
        if(datePayment.error.value.not() && period.error.value.not() && name.error.value.not() && value.error.value.not() && quote.error.value.not()){
            disableSaveStatus.value = false
        }else{
            disableSaveStatus.value = true
        }

    }

    fun clear(){
        datePayment.reset(LocalDate.now())
        name.reset("")
        value.reset(BigDecimal.ZERO)
        quote.reset(BigDecimal.ZERO)
        period.reset(Pair(0,""))
    }

    fun main() = viewModelScope.launch {
        Log.d(javaClass.simpleName,"Runing Execution projections get data")

        loaderStatus.value = true
        projectionSvc?.let{
            id?.let {
                withContext(ioDispatcher) {
                    Log.d(javaClass.simpleName,"Execution findById")
                    projectionSvc.findById(id)
                }?.let{ dto ->
                    Log.d(javaClass.simpleName,"DTO: $dto")
                    projection.update {
                        it.copy(id=dto.id,
                            create=dto.create,
                            end=dto.end,
                            name=dto.name,
                            type=dto.type,
                            value=dto.value,
                            quote=dto.quote,
                            monthsLeft =dto.monthsLeft,
                            amountSaved = dto.amountSaved,
                            active = dto.active
                        )
                    }
                    datePayment.onValueChange(dto.end)
                    name.onValueChange(dto.name)
                    value.onValueChange(dto.value)
                    quote.onValueChange(dto.quote)
                    period.onValueChange(periodsOpt.find { it.first == dto.type.month } ?: periodsOpt.first())

                }
            }
        }
        loaderStatus.value = false
    }

    private fun quoteCalculation()= viewModelScope.launch {
        if(datePayment.error.value.not() && period.error.value.not() && value.error.value.not()) {
            loaderStatus.value = true
            projectionSvc?.let {
                withContext(ioDispatcher) {
                    Log.d(
                        javaClass.simpleName,
                        "Month: ${period.value.value.first} Date: ${datePayment.value.value} Value: ${value.value.value}"
                    )
                    it.calculateQuote(
                        KindPaymentsEnums.findByIndex(period.value.value.first),
                        datePayment.value.value,
                        value.value.value
                    )
                }.let(quote::onValueChange)
            }
            loaderStatus.value = false
        }
    }
}