package co.com.japl.module.credit.controllers.forms

import android.content.Context
import android.util.Log
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort
import co.com.japl.module.credit.R
import co.japl.android.graphs.utils.NumbersUtil
import co.com.japl.ui.utils.initialFieldState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class AdditionalFormViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val additionalSvc: IAdditionalFormPort
) : ViewModel(){
    private var id:Int=-1
    private var codeCredit:Int=0
    private val _dto = MutableStateFlow<AdditionalCreditDTO>(AdditionalCreditDTO(
        id=0,
        creditCode = 0,
        name = "",
        value = BigDecimal.ZERO,
        startDate = LocalDate.now(),
        endDate = LocalDate.now()
    ))
    val hostState: SnackbarHostState = SnackbarHostState()
    val loading = mutableStateOf(false)
    val name = initialFieldState(
        savedStateHandle,
        "FORM_NAME",
        initialValue = "",
        validator = { it.isNotBlank()},
        onValueChangeCallBack = {name->_dto.update{
            it.copy(name = name)
        }}
    )
    val value = initialFieldState<BigDecimal>(
        savedStateHandle,
        "FORM_VALUE",
        initialValue = BigDecimal.ZERO,
        formatter = { if(it.isNotBlank() && NumbersUtil.isNumber(it)) NumbersUtil.toBigDecimal(it) else BigDecimal.ZERO  },
        validator = { BigDecimal.ZERO < it },
        onValueChangeCallBack = { value ->
            _dto.update {
                it.copy(value = value)
            }
        }
    ).also {
        it.onValueChangeStr("")
    }
    val startDate = initialFieldState(
        savedStateHandle,
        "FORM_START_DATE",
        initialValue = LocalDate.now(),
        validator = { it.isAfter(LocalDate.now().withYear(2000))},
        onValueChangeCallBack = { date->
            _dto.update {
                it.copy(startDate = date)
            }
        }
    )

    init{
        savedStateHandle.get<Int>("id")?.let{ id = it }
        savedStateHandle.get<Int>("codeCredit")?.let{ codeCredit = it }
        _dto.update { it.copy(id=id,creditCode = codeCredit.toLong()) }
        main()
    }

    fun create(navController: NavController){
        if(validation()) {
            additionalSvc.let {
                try {
                    if (_dto.value.id <= 0) {
                        if (it.create(_dto.value)) {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = navController.context.getString(R.string.create_record_success),
                                    actionLabel = navController.context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                ).also {
                                    navController.popBackStack()
                                }
                            }
                        } else {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = navController.context.getString(R.string.error_record_success),
                                    actionLabel = navController.context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } else {
                        if (it.update(_dto.value)) {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = navController.context.getString(R.string.update_record_success),
                                    actionLabel = navController.context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                ).also {
                                    navController.popBackStack()
                                }
                            }
                        } else {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = navController.context.getString(R.string.error_upd_record_success),
                                    actionLabel = navController.context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        hostState.showSnackbar(
                            message = "Error: ${e.message}",
                            actionLabel = navController.context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    fun clear(){
        name.reset("")
        value.reset(BigDecimal.ZERO)
        startDate.reset(LocalDate.now())
    }

    fun validation():Boolean {
        name.validate().takeIf { !it }?.let {
            name.error.value = false
        }
        value.validate().takeIf { !it }?.let {
            value.error.value = false
        }
        startDate.validate().takeIf { !it }?.let {
            startDate.error.value = false
        }
        return name.error.value.not() && value.error.value.not() && startDate.error.value.not()
    }

    fun main()= runBlocking {
        loading.value = true
        viewModelScope.launch {
            execute()
        }
    }

    suspend fun execute(){
        additionalSvc.let{ svc ->
            id.takeIf { it > 0 }?.let {
                svc.get(id)?.let{
                    Log.d(this.javaClass.name,"<<<=== Execute:List $id $codeCredit $it")
                    name.onValueChange(it.name)
                    value.onValueChange(it.value)
                    startDate.onValueChange(it.startDate)
                    _dto.update{
                        it.copy(
                            name = it.name,
                            value = it.value,
                            startDate = it.startDate,
                            endDate = it.endDate
                        )
                    }
                }
            }
        }
        loading.value = false
    }
}
