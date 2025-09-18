package co.com.japl.module.paid.controllers.monthly.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.ui.Prefs

class MonthlyViewModelFactory(
    private val paidSvc: IPaidPort,
    private val incomesSvc: IInputPort,
    private val accountSvc: IAccountPort,
    private val smsSvc: ISMSPaidPort,
    private val paidSmsSvc: ISmsPort,
    private val prefs: Prefs
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return MonthlyViewModel(
            extras.createSavedStateHandle(),
            paidSvc,
            incomesSvc,
            accountSvc,
            smsSvc,
            paidSmsSvc,
            prefs
        ) as T
    }
}
