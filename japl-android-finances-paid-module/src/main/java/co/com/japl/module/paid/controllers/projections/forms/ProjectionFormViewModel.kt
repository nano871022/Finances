package co.com.japl.module.paid.controllers.projections.forms

import android.content.Context
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
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
import co.japl.android.graphs.utils.NumbersUtil
import co.com.japl.ui.utils.initialFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class ProjectionFormViewModel constructor(
    private val context: Context,
    private val saveStateHandler: SavedStateHandle,
    private val projectionSvc: IProjectionFormPort,
) : ViewModel() {
    val loaderStatus = mutableStateOf(false)
    val disableSaveStatus = mutableStateOf(true)
    private val periodsOpt = KindPaymentsEnums.entries.map{ Pair(it.month,context.getString(it.title) ) }
    val hostState: SnackbarHostState = SnackbarHostState()
    private var id:Int?=null
    private val projection = MutableStateFlow(ProjectionDTO(
        id= 0,
        create= LocalDate.now(),
         end= LocalDate.now(),
         name= "",
         value= BigDecimal.ZERO,
         quote= BigDecimal.ZERO,
         monthsLeft = 0,
         amountSaved = BigDecimal.ZERO,
    ))

    val datePayment = initialFieldState<LocalDate>(
        saveStateHandler,
        "FORM_DATE_PAYMENT",
        initialValue = LocalDate.now(),
        formatter = {
            if(it.isNotBlank()) {
                DateUtils.toLocalDate(it)
            }else{
                null
            }
        },
        validator = { !it.isBefore(LocalDate.now()) },
        onValueChangeCallBack = {date->
            projection.update {
            it.copy(end = date)
        }}
    )
    val period = initialFieldState<Pair<Int,String>>(
        saveStateHandler,
        "FORM_PERIOD",
        initialValue = periodsOpt.first(),
        validator = { KindPaymentsEnums.existIndex( it.first ) },
        list = periodsOpt,
        onValueChangeCallBack = {
            projection.update {
                it.copy(type = it.type)
            }
        })
    val name = initialFieldState<String>(
        saveStateHandler,
        "FORM_NAME",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = {
            projection.update{
                it.copy(name = it.name)
            }
        })
    val value = initialFieldState<BigDecimal>(
        saveStateHandler,
        "FORM_VALUE",
        initialValue = BigDecimal.ZERO,
        formatter = { if(NumbersUtil.isNumber(it)) BigDecimal(it) else BigDecimal.ZERO },
        validator = { it > BigDecimal.ZERO },
        onValueChangeCallBack = {
            projection.update{
                it.copy(value = it.value)
            }
        }).also {
        it.onValueChangeStr("")
    }
    val quote = initialFieldState<BigDecimal>(
        saveStateHandler,
        "FORM_QUOTE",
        initialValue = BigDecimal.ZERO,
        formatter = { if(NumbersUtil.isNumber(it)) BigDecimal(it) else BigDecimal.ZERO },
        validator = { it > BigDecimal.ZERO },
        onValueChangeCallBack = {
            projection.update{
                it.copy(quote = it.quote)
            }
        }).also {
        it.onValueChangeStr("")
    }

    init {
        saveStateHandler.get<Int>("id")?.let{ id = it }
        main()
    }

    fun save(navController: NavController){
        if(disableSaveStatus.value.not()){
            projectionSvc.let{
                it.save(projection.value).let{
                    viewModelScope.launch {
                        if (it) {
                            hostState.showSnackbar(
                                message = context.getString(R.string.save_project_success),
                                actionLabel = context.getString(R.string.close),
                                duration = SnackbarDuration.Short
                            ).also {
                                navController.popBackStack()
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
    }

    fun update(navController: NavController){
        if(disableSaveStatus.value.not()){
            projectionSvc.let{
                it.save(projection.value).let{
                    viewModelScope.launch {
                        if (it) {
                            hostState.showSnackbar(
                                message = context.getString(R.string.update_project_success),
                                actionLabel = context.getString(R.string.close),
                                duration = SnackbarDuration.Short
                            ).also {
                                navController.popBackStack()
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
    }

    fun validate(){
        datePayment.validate()
        period.validate()
        name.validate()
        value.validate()
        if(datePayment.error.value.not() && period.error.value.not() && name.error.value.not() && value.error.value.not()){
            disableSaveStatus.value = false
            quoteCalculation()
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
        loaderStatus.value = true
        projectionSvc.let{
            id?.let {
                projectionSvc.findById(id!!)?.let{ dto ->
                    projection.update {
                        dto
                    }
                }
            }
        }
        loaderStatus.value = false
    }

    private fun quoteCalculation()= viewModelScope.launch {
        loaderStatus.value = true
        projectionSvc.let{
            it.calculateQuote(KindPaymentsEnums.findByIndex(period.value.value.first),datePayment.value.value,value.value.value).let(quote::onValueChange)
        }
        loaderStatus.value = false
    }
}
