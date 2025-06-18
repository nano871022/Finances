package co.com.japl.module.credit.controllers.forms

import android.content.Context
import android.util.Log
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort
import co.com.japl.module.credit.R
import co.com.japl.ui.utils.initialFieldState
import co.japl.android.myapplication.utils.NumbersUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AdditionalFormViewModel @Inject constructor(private val context: Context, private val id:Int=-1, private val codeCredit:Int, private val additionalSvc: IAdditionalFormPort?, private val navController: NavController?) : ViewModel(){
    private val _dto = MutableStateFlow<AdditionalCreditDTO>(AdditionalCreditDTO(
        id=id,
        creditCode = codeCredit.toLong(),
        name = "",
        value = BigDecimal.ZERO,
        startDate = LocalDate.now(),
        endDate = LocalDate.now()
    ))
    val hostState: SnackbarHostState = SnackbarHostState()
    val loading = mutableStateOf(false)
    val name = initialFieldState(
        initialValue = "",
        validator = { it.isNotBlank()},
        onValueChangeCallBack = {name->_dto.update{
            it.copy(name = name)
        }}
    )
    val value = initialFieldState<BigDecimal>(
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
        initialValue = LocalDate.now(),
        validator = { it.isAfter(LocalDate.now().withYear(2000))},
        onValueChangeCallBack = { date->
            _dto.update {
                it.copy(startDate = date)
            }
        }
    )

    init{
        main()
    }

    fun create(){
        if(validation()) {
            additionalSvc?.let {
                try {
                    if (_dto.value.id <= 0) {
                        if (it.create(_dto.value)) {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.create_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                ).also {
                                    navController?.popBackStack()
                                }
                            }
                        } else {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.error_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } else {
                        if (it.update(_dto.value)) {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.update_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                ).also {
                                    navController?.popBackStack()
                                }
                            }
                        } else {
                            viewModelScope.launch {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.error_upd_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        hostState.showSnackbar(
                            message = "Error: ${e.message}",
                            actionLabel = context.getString(R.string.close),
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
        additionalSvc?.let{ svc ->
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