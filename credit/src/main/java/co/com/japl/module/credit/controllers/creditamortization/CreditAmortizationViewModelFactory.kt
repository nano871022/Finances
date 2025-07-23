package co.com.japl.module.credit.controllers.creditamortization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import javax.inject.Inject

class CreditAmortizationViewModelFactory @Inject constructor(
    private val creditSvc: ICreditPort,
    private val additionalSvc: IAdditional,
    private val gracePeriodSvc: IPeriodGracePort,
    private val amortizationSvc: IAmortizationTablePort
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreditAmortizationViewModel::class.java)) {
            return CreditAmortizationViewModel(
                creditSvc,
                additionalSvc,
                gracePeriodSvc,
                amortizationSvc
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
