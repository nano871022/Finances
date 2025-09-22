package co.com.japl.module.paid.controllers.Inputs.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.inputs.IInputPort

class InputViewModelFactory(private val inputSvc: IInputPort) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return InputViewModel(savedStateHandle, inputSvc) as T
    }
}
