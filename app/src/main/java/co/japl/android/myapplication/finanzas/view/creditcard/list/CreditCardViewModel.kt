package co.japl.android.myapplication.finanzas.view.creditcard.list

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.CreditCardParams
import kotlinx.coroutines.runBlocking

class CreditCardViewModel constructor(private val creditCardSvc:ICreditCardPort?,private val navController:NavController?) : ViewModel(){

    private var _list = listOf<CreditCardDTO>()
    val list get() = _list

    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(true)

    fun onClick(){
        navController?.let{CreditCardParams.newInstance(it)}
    }

    fun edit(id:Int){
        navController?.let { CreditCardParams.newInstance(id.toString(),it) }
    }

    fun delete(id:Int){
        creditCardSvc?.let{
            if(it.delete(id)){
                Toast.makeText(navController?.context, R.string.delete_successfull,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(navController?.context, R.string.dont_deleted,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1f
    }

    suspend fun execute(){
        progress.floatValue = 0.4f
        creditCardSvc?.let{
            _list = it.getAll()
            showProgress.value = false
        }
        progress.floatValue = 0.8f
    }
}