package co.com.japl.module.paid.controllers.Inputs.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.com.japl.finances.iports.inbounds.inputs.IInputPort

class InputListModelViewFactory(
    private val context: Context,
    private val accountCode: Int,
    private val inputSvc: IInputPort?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputListModelView(context, accountCode, inputSvc) as T
    }
}
