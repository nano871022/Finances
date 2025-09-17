package co.com.japl.module.paid.controllers.period.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort

class PeriodsViewModelFactory(
    private val paidSvc: IPeriodPaidPort
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return PeriodsViewModel(paidSvc, savedStateHandle) as T
    }
}
