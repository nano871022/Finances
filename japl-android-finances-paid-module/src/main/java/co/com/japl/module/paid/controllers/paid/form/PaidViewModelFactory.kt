package co.com.japl.module.paid.controllers.paid.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort

class PaidViewModelFactory(
    private val accountSvc: IAccountPort,
    private val paidSvc: IPaidPort
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PaidViewModel(extras.createSavedStateHandle(), accountSvc, paidSvc) as T
    }
}
