package co.com.japl.module.creditcard.controllers.simulator

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.utils.initialFieldState
import co.japl.android.graphs.utils.NumbersUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(private val context: Context?=null, private val simuladorSvc: ISimulatorCreditVariablePort?=null, private val navigator: NavController?=null) : ViewModel(){
    private val _simulator = MutableStateFlow(SimulatorCreditDTO(
        value = BigDecimal.ZERO,
        periods = 0,
        tax = 0.0,
        isCreditVariable = true,
        kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE
    ))
    val simulator = _simulator.asStateFlow()
    val stateCalculation = mutableStateOf(false)
    val snackbar = mutableStateOf(SnackbarHostState())

    val kindTaxList = KindOfTaxEnum.entries.map{ Pair(it.ordinal, context?.getString( it.title)?:it.value) }

    val creditValue = initialFieldState(
        initialValue = BigDecimal.ZERO,
        validator = { it > BigDecimal.ZERO },
        formatter = {NumbersUtil.toBigDecimal(it)},
        onValueChangeCallBack = { cv ->
            _simulator.update {
                it.copy(value = cv)
            }
        }
    )

    val creditRate = initialFieldState<Double>(
        initialValue = 0.0,
        validator = { it > 0.0 },
        formatter = {NumbersUtil.toBigDecimal(it).toDouble()},
        onValueChangeCallBack = {cr ->
            _simulator.update {
                it.copy(tax = cr.toDouble())
            }
        }
    )

    val creditKindRate = initialFieldState<KindOfTaxEnum>(
        initialValue = KindOfTaxEnum.MONTHLY_EFFECTIVE,
        validator = { it != null },
        list =  KindOfTaxEnum.entries.toList(),

        onValueChangeCallBack = {ckr->
            _simulator.update{
                it.copy(kindOfTax = ckr)
            }
        }
    )

    val month = initialFieldState(
        initialValue = 0,
        validator = { it > 0 },
        formatter = {NumbersUtil.toBigDecimal(it).toInt()},
        onValueChangeCallBack = {mon->
            _simulator.update{
                it.copy(periods = mon.toShort())
            }
        }
    )

    fun clear(){
        creditValue.reset(BigDecimal.ZERO)
        creditRate.reset(0.0)
        month.reset(0)
        creditKindRate.reset(KindOfTaxEnum.MONTHLY_EFFECTIVE )
    }

    fun isValid():Boolean = creditValue.validate() && creditRate.validate() && month.validate()

    fun calculate() = viewModelScope.launch{
        simuladorSvc?.calculate(_simulator.value)?.let{ calc->
            _simulator.update {
                it.copy(
                    capitalValue = calc.capitalValue,
                    interestValue = calc.interestValue,
                    quoteValue = calc.quoteValue
                )
            }
        }?:snackbar.value
            .showSnackbar(
                message = context?.getString( R.string.calculation_error)?:""
            )
    }

    fun amortization(){
        navigator?.let{

        }
    }

    fun save() = viewModelScope.launch{
        simuladorSvc?.save(_simulator.value).takeIf { it == true }?.let{
            snackbar.value
                    .showSnackbar(
                        message = context?.getString( R.string.save_success)?:""
                    )

        }?:snackbar.value
                .showSnackbar(
                    message = context?.getString( R.string.save_unsuccess)?:""
                )

    }

}