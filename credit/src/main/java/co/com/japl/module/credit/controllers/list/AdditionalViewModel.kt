package co.com.japl.module.credit.controllers.list

import android.content.Context
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.AdditionalList
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ViewModelScoped
class AdditionalViewModel constructor(private val savedStateHandle: SavedStateHandle, private val additionalSvc: IAdditional?): ViewModel(){

    val list = mutableStateListOf<AdditionalCreditDTO>()
    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()
    val hostState: SnackbarHostState = SnackbarHostState()
    var code:Int = 0
    private set


    init{
        savedStateHandle.get<Int>("code")?.let{ code = it }
        main()
    }

    fun addAdditional(navController: NavController){
        AdditionalList.navigateForm(code,navController)
    }

    fun deleteAdditional(idCode:Int, navController: NavController){
        additionalSvc?.let{
            it.delete(idCode).also{ resp ->
                viewModelScope.launch {
                    when(resp) {
                       true -> hostState.showSnackbar(
                            message = navController.context.getString(R.string.delete_record),
                            actionLabel = navController.context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        ).also {
                            viewModelScope.launch {
                                main()
                            }
                       }
                        false -> hostState.showSnackbar(
                            message = navController.context.getString(R.string.error_delete_record),
                            actionLabel = navController.context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    fun updateAdditional(id:Int, navController: NavController){
        AdditionalList.navigateForm(id,code,navController)
    }

    fun main()= runBlocking {
        _loading.value = true
        viewModelScope.launch {
            execute()
        }
    }

    suspend fun execute(){
        additionalSvc?.let{
            list.clear()
            it.getAdditional(code).forEach(list::add)
        }
        _loading.value = false
    }
}