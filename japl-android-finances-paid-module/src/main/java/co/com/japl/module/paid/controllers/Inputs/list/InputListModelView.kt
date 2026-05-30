package co.com.japl.module.paid.controllers.Inputs.list

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.enums.MoreOptionsItemsInput
import co.com.japl.module.paid.navigations.Input
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel(assistedFactory = InputListModelView.Factory::class)
class InputListModelView @AssistedInject constructor(@Assisted private val context:Context, @Assisted val accountCode:Int, @Assisted private val navController: NavController?,private val inputSvc:IInputPort?) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(context: Context, accountCode: Int, navController: NavController?): InputListModelView
    }

    var _items = mutableStateListOf<InputDTO>()

    val stateDialogOptionsMore = mutableStateOf(false)
    var stateLoader = mutableStateOf(true)
    var progress = mutableFloatStateOf( 0f)

    fun optionSelected(id:Int,value:Double = 0.0, option:MoreOptionsItemsInput){
        when(option){
            MoreOptionsItemsInput.DELETE ->{deleteInput(id)}
            MoreOptionsItemsInput.UPDATE_VALUE ->{ updateValue(id,value) }
            MoreOptionsItemsInput.EDIT -> { goToInputForm(id) }
        }
    }

    private fun deleteInput(id:Int){
        if(inputSvc?.deleteRecord(id) == true){
            Toast.makeText(context,context.resources.getText(R.string.toast_successful_deleted),Toast.LENGTH_SHORT).show()
            viewModelScope.launch {
                main()
            }
        }else{
            Toast.makeText(context,context.resources.getText(R.string.toast_unsuccessful_deleted),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateValue(id:Int,value:Double){
        if(inputSvc?.updateValue(id,value) == true){
            Toast.makeText(context,context.resources.getText(R.string.toast_update_successful),Toast.LENGTH_SHORT).show()
            viewModelScope.launch {
                main()
            }
        }else{
            Toast.makeText(context,context.resources.getText(R.string.toast_update_error),Toast.LENGTH_SHORT).show()
        }
    }

    fun goToInputForm(){
       navController?.let{Input.navigate(accountCode,navController)}
    }

    fun goToInputForm(code:Int){
        navController?.let{Input.navigate(accountCode,code,navController)}
    }

    fun main()= runBlocking  {
        progress.value = 0.1f
        getItems()
        progress.value = 1f
    }

    suspend fun getItems() {
        progress.value = 0.2f
        inputSvc?.let{
            _items.clear()
            it.getInputs(accountCode).let {
                _items.addAll(it)
                progress.value = 0.7f
                stateLoader.value = false
            }

        }
        progress.value = 0.8f
    }

}