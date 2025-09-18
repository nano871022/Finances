package co.com.japl.module.paid.controllers.sms.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort

class SmsViewModelFactory(
    private val smsSvc: ISMSPaidPort,
    private val accountSvc: IAccountPort
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SmsViewModel(extras.createSavedStateHandle(), smsSvc, accountSvc) as T
    }
}
