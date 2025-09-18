package co.com.japl.module.paid.controllers.period.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort

class PeriodsViewModelFactory(
    private val paidSvc: IPeriodPaidPort
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PeriodsViewModel(extras.createSavedStateHandle(), paidSvc) as T
    }
}
