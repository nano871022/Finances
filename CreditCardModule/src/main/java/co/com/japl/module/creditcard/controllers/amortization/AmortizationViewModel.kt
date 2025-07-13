package co.com.japl.module.creditcard.controllers.amortization;

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch

@HiltViewModel
class  AmortizationViewModel constructor(private val id:Int, private val svc: IAmortizationTablePort?=null, private val navController: NavController?=null):
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
		svc?.getAmortization(id, KindAmortization.VARIABLE_QUOTE_SIMULATOR,true)?.let{
			list.addAll(it)
		}
		progressStatus.value = false
	}

}
