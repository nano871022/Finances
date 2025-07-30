package co.japl.android.credit.view.extavalue

import androidx.lifecycle.ViewModel
import co.japl.android.finances.services.enums.AmortizationKindOfEnum
import co.japl.android.finances.services.interfaces.IAddAmortizationSvc
import co.japl.android.finances.services.interfaces.IExtraValueAmortizationCreditSvc
import co.japl.android.finances.services.interfaces.IExtraValueAmortizationQuoteCreditCardSvc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExtraValueListViewModel @Inject constructor(
    private val addAmortizationSvc: IAddAmortizationSvc,
    private val extraValueAmortizationCreditSvc: IExtraValueAmortizationCreditSvc,
    private val extraValueAmortizationQuoteCreditCardSvc: IExtraValueAmortizationQuoteCreditCardSvc
) : ViewModel() {

    private val _extraValues = MutableStateFlow<List<Any>>(emptyList())
    val extraValues: StateFlow<List<Any>> = _extraValues

    fun loadExtraValues(creditId: Int, kindOf: AmortizationKindOfEnum) {
        _extraValues.value = when (kindOf) {
            AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION -> addAmortizationSvc.getAll(creditId)
            AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION_CREDIT -> extraValueAmortizationCreditSvc.getAll(creditId)
            AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION_QUOTE_CREDIT_CARD -> extraValueAmortizationQuoteCreditCardSvc.getAll(creditId)
        }
    }

    fun create(creditId: Int, numQuotes: Int, value: Double, kindOf: AmortizationKindOfEnum) {
        when (kindOf) {
            AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION -> addAmortizationSvc.createNew(creditId, numQuotes.toLong(), value)
            else -> {}
        }
        loadExtraValues(creditId, kindOf)
    }
}
