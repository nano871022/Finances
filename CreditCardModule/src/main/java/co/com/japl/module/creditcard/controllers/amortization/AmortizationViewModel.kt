package co.com.japl.module.creditcard.controllers.amortization

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject
import co.com.japl.module.creditcard.params.AmortizationTableParams

@HiltViewModel
class AmortizationViewModel @Inject constructor(
    @ApplicationContext private val context: Context, 
    private val savedStateHandle: SavedStateHandle,
    private val svc: IAmortizationTablePort?
): ViewModel(){

	private val id: Int by lazy { (AmortizationTableParams.download(savedStateHandle).get("CODE") as? Long)?.toInt() ?: 0 }
	var navController: NavController? = null

	val progressStatus = mutableStateOf(true)
	val list = mutableListOf<AmortizationRowDTO>()

	init{
		execute()	
	}
	
	fun execute() = viewModelScope.launch{
		try {
			withContext(Dispatchers.IO) {
				svc?.getAmortization(id, KindAmortization.VARIABLE_QUOTE_SIMULATOR, false)
			}?.let {
				Log.d(javaClass.simpleName,"Org: ${list.size} New: ${it.size}")
				list.clear()
				list.addAll(it)
			}
			progressStatus.value = false
		}catch (e: Exception){
			Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
		}
	}

}
