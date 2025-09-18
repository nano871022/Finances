package co.com.japl.module.paid.controllers.accounts.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import androidx.lifecycle.SavedStateHandleSupport

class AccountViewModelFactory(private val accountSvc: IAccountPort) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = SavedStateHandleSupport.createSavedStateHandle(extras)
        return AccountViewModel(savedStateHandle, accountSvc) as T
    }
}
