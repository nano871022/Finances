package co.com.japl.module.credit.controllers.simulator

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.Simulator
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.ui.utils.initialFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SimulatorFixViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val simuladorSvc: ISimulatorCreditFixPort,
    private val savedStateHandler: SavedStateHandle,
) : ViewModel(){

    val stateCalculation = mutableStateOf(false)
    val showCalculation = mutableStateOf(false)
    val snackbar = mutableStateOf(SnackbarHostState())
    val statePopup = mutableStateOf(false)
    val hasProgress = mutableStateOf(false)

    val name = initialFieldState(
        savedStateHandler,
        key="FORM_NAME",
        initialValue = "",
        validator = { it.isNotEmpty() },
        onValueChangeCallBack = {cv->
            _simulator.update {
                it.copy(name = cv)
            }
        }
    )

    val kindTaxList = KindOfTaxEnum.entries.map{ Pair(it.ordinal, context.getString( it.title)) }

    var _simulator = MutableStateFlow(SimulatorCreditDTO(
        value = BigDecimal.ZERO,
        periods = 0,
        tax = 0.0,
        isCreditVariable = false,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE
    ))
        private set
    val simulator = _simulator.asStateFlow()

    val creditValue = initialFieldState(
        savedStateHandler,
        key="FORM_CREDIT_VALUE",
        initialValue = BigDecimal.ZERO,
        validator = { it > BigDecimal.ZERO },
        formatter = {NumbersUtil.toBigDecimal(it)},
        onValueChangeCallBack = { cv ->
            _simulator.update {
                it.copy(value = cv)
            }
            isValid()
        }
    )

    val creditRate = initialFieldState<Double>(
        savedStateHandler,
        key="FORM_CREDIT_RATE",
        initialValue = 0.0,
        validator = { it > 0.0 },
        formatter = {NumbersUtil.toBigDecimal(it).toDouble()},
        onValueChangeCallBack = {cr ->
            _simulator.update {
                it.copy(tax = cr.toDouble())
            }
            isValid()
        }
    )

    val creditKindRate = initialFieldState<KindOfTaxEnum>(
        savedStateHandler,
        key="FORM_CREDIT_KIND_RATE",
        initialValue = KindOfTaxEnum.ANUAL_EFFECTIVE,
        validator = { it != null },
        list =  KindOfTaxEnum.entries.toList(),

        onValueChangeCallBack = {ckr->
            _simulator.update{
                it.copy(kindOfTax = ckr)
            }
        }
    )

    val month = initialFieldState(
        savedStateHandler,
        key="FORM_MONTH",
        initialValue = 0,
        validator = { it > 0 },
        formatter = {NumbersUtil.toBigDecimal(it).toInt()},
        onValueChangeCallBack = {mon->
            _simulator.update{
                it.copy(periods = mon.toShort())
            }
            isValid()
        }
    )

    fun clear(){
        creditValue.reset(BigDecimal.ZERO)
        creditRate.reset(0.0)
        month.reset(0)
        creditKindRate.reset(KindOfTaxEnum.ANUAL_EFFECTIVE )
    }

    fun isValid():Boolean {
        val valid =  creditValue.validate() && creditRate.validate() && month.validate()
        showCalculation.value = valid
        return valid
    }

    fun calculate() = viewModelScope.launch{
        simuladorSvc.calculate(_simulator.value)?.let{ calc->
            _simulator.update {
                it.copy(
                    capitalValue = calc.capitalValue,
                    interestValue = calc.interestValue,
                    quoteValue = calc.quoteValue
                )
            }
            stateCalculation.value = true
        }?:snackbar.value
            .showSnackbar(
                message = context.getString( R.string.calculation_error)
            )
    }

    fun amortization(navigator: NavController){
        simuladorSvc.save(_simulator.value.copy(code=0,name="In simulator"),true).let {
            Log.d("FORM","Amortization:: $it")
            Simulator.navigate(it.toInt(),navigator)
        }
    }

    fun save() = viewModelScope.launch{
        if(isValid() && name.validate()) {
            simuladorSvc.save(_simulator.value,false).takeIf { (it ) > 0L }
                ?.let { code ->
                    _simulator.update {
                        it.copy(code = code.toInt())
                    }
                    snackbar.value
                        .showSnackbar(
                            message = context.getString(R.string.save_success)
                        )

                } ?: snackbar.value
                .showSnackbar(
                    message = context.getString(R.string.save_unsuccess)
                )
        }else{
            snackbar.value.showSnackbar("Error los datos no estan completos")
        }
    }
}
