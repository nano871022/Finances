package co.com.japl.module.creditcard.controllers.amortization

import android.content.Context
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
import kotlinx.coroutines.launch

@HiltViewModel
class  AmortizationViewModel(@ApplicationContext private val context: Context, private val id:Int, private val svc: IAmortizationTablePort?=null, private val navController: NavController?=null):
	ViewModel(){
	val progressStatus = mutableStateOf(true)
	val list = mutableListOf<AmortizationRowDTO>()

	init{
		execute()	
	}
	
	fun execute() = viewModelScope.launch{
		callApi()
	}

	suspend fun callApi(){
		try {
			svc?.getAmortization(id, KindAmortization.VARIABLE_QUOTE_SIMULATOR, true)?.let {
				list.addAll(it)
			}
			progressStatus.value = false
		}catch (e: Exception){
			Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
		}
	}

}
