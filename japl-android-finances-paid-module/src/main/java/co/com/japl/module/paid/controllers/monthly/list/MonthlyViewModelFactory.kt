package co.com.japl.module.paid.controllers.monthly.list

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
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
    private val prefs: Prefs,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return MonthlyViewModel(
            paidSvc,
            incomesSvc,
            accountSvc,
            smsSvc,
            paidSmsSvc,
            prefs,
            handle
        ) as T
    }
}
