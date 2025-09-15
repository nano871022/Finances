package co.com.japl.module.creditcard.controllers.amortization;

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
class  AmortizationViewModel constructor(private val savedStateHandle: SavedStateHandle, private val svc: IAmortizationTablePort):
	ViewModel(){
	private var id:Int = 0
	val progressStatus = mutableStateOf(true)
	val list = mutableListOf<AmortizationRowDTO>()

	init{
		 id = savedStateHandle.get<Int>("credit_code")?:0
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
