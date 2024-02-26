package co.japl.android.myapplication.finanzas.view.accounts.inputs.lists

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsInput
import co.japl.android.myapplication.finanzas.putParams.InputListParams
import kotlinx.coroutines.runBlocking

class InputListModelView (private val context:Context, val accountCode:Int,private val navController: NavController,private val inputSvc:IInputPort?) : ViewModel() {

    var _items = mutableListOf<InputDTO>()

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
            Toast.makeText(context,context.resources.getText(R.string.delete_successfull),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,context.resources.getText(R.string.dont_deleted),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateValue(id:Int,value:Double){
        if(inputSvc?.updateValue(id,value) == true){
            Toast.makeText(context,context.resources.getText(R.string.toast_successful_update),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,context.resources.getText(R.string.toast_dont_successful_update),Toast.LENGTH_SHORT).show()
        }
    }

    fun goToInputForm(){
        InputListParams.newInstanceDeeplink(accountCode,navController)
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
            _items.addAll(it.getInputs(accountCode)?: emptyList())
            progress.value = 0.7f
            stateLoader.value = false
        }
        progress.value = 0.8f
    }

}