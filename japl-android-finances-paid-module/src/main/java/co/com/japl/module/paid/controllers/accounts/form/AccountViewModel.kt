package co.com.japl.module.paid.controllers.accounts.form

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.module.paid.R
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class AccountViewModel constructor(private val codeAccount:Int?,private val accountSvc:IAccountPort,private val navController: NavController):ViewModel(){

    var loader = mutableStateOf(true)
    var progress = mutableFloatStateOf( 0f)

    private var _item : AccountDTO? = null
    private var save = false

    var name = mutableStateOf("")
    var active = mutableStateOf(true)

    var errorName = mutableStateOf(false)

    fun validation(){
        errorName.value = name.value.isBlank()
        save = !errorName.value
        if (save){
            _item = AccountDTO(
                id = if(_item != null && _item?.id?:0 > 0) _item?.id!! else 0,
                name = name.value,
                active = active.value,
                create = LocalDate.now()
            )
        }
    }

    fun save(){
        if(save && _item != null) {
            if(_item?.id == 0) {
                if(accountSvc.create(_item!!)>0){
                    navController.navigateUp()
                    Toast.makeText(navController.context, R.string.toast_save_successful, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(navController.context, R.string.toast_save_error, Toast.LENGTH_SHORT).show()
                }
            }else {
                if(accountSvc.update(_item!!)){
                    navController.navigateUp()
                    Toast.makeText(navController.context, R.string.toast_update_successful, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(navController.context, R.string.toast_update_error, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            validation()
        }
    }

    fun main() = runBlocking {
        progress.value = 0.1f
        execution()
        progress.value = 1f
    }

    suspend fun execution(){
        progress.value = 0.2f
        codeAccount?.takeIf { it > 0 }?.let{
            accountSvc.getById(codeAccount)?.let{
                progress.value = 0.6f
                _item = it
                name.value = it.name
                active.value = it.active
            }
        }
        progress.value = 0.7f
        loader.value = false
        progress.value = 0.8f
    }

}