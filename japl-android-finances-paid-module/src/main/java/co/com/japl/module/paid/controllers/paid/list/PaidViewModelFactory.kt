package co.com.japl.module.paid.controllers.paid.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.ui.Prefs

class PaidViewModelFactory(
    private val paidSvc: IPaidPort,
    private val prefs: Prefs
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PaidViewModel(extras.createSavedStateHandle(), paidSvc, prefs) as T
    }
}
