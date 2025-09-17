package co.com.japl.module.paid.controllers.sms.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort

class SmsViewModelFactory(
    private val smsSvc: ISMSPaidPort,
    private val accountSvc: IAccountPort
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return SmsViewModel(smsSvc, accountSvc, savedStateHandle) as T
    }
}
