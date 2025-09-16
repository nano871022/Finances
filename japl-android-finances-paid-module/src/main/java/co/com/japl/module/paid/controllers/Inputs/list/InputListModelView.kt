package co.com.japl.module.paid.controllers.Inputs.list

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.enums.MoreOptionsItemsInput
import co.com.japl.module.paid.navigations.Input
import kotlinx.coroutines.runBlocking

class InputListModelView (private val context:Context, val accountCode:Int,private val inputSvc:IInputPort?) : ViewModel() {

    var _items = mutableStateListOf<InputDTO>()

    val stateDialogOptionsMore = mutableStateOf(false)
    var stateLoader = mutableStateOf(true)
    var progress = mutableFloatStateOf( 0f)

    fun optionSelected(id:Int,value:Double = 0.0, option:MoreOptionsItemsInput){
        when(option){
            MoreOptionsItemsInput.DELETE ->{deleteInput(id)}
            MoreOptionsItemsInput.UPDATE_VALUE ->{ updateValue(id,value) }
        }
    }

    private fun deleteInput(id:Int){
        if(inputSvc?.deleteRecord(id) == true){
            Toast.makeText(context,context.resources.getText(R.string.toast_successful_deleted),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,context.resources.getText(R.string.toast_unsuccessful_deleted),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateValue(id:Int,value:Double){
        if(inputSvc?.updateValue(id,value) == true){
            Toast.makeText(context,context.resources.getText(R.string.toast_update_successful),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,context.resources.getText(R.string.toast_update_error),Toast.LENGTH_SHORT).show()
        }
    }

    fun goToInputForm(navController: NavController){
       Input.navigate(accountCode,navController)
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