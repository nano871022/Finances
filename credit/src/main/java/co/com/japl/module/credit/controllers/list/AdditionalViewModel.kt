package co.com.japl.module.credit.controllers.list

import android.content.Context
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.AdditionalList
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel(assistedFactory = AdditionalViewModel.Factory::class)
class AdditionalViewModel @AssistedInject constructor(@Assisted private val context: Context, @Assisted private val code:Int=0, private val additionalSvc: IAdditional?, @Assisted private val navController: NavController? ): ViewModel(){

    @AssistedFactory
    interface Factory {
        fun create(context: Context, code: Int, navController: NavController?): AdditionalViewModel
    }

    val list = mutableStateListOf<AdditionalCreditDTO>()
    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()
    val hostState: SnackbarHostState = SnackbarHostState()


    init{
        main()
    }

    fun addAdditional(){
        navController?.let{
            AdditionalList.navigateForm(code,it)
        }
    }

    fun deleteAdditional(idCode:Int){
        additionalSvc?.let{
            it.delete(idCode).also{ resp ->
                viewModelScope.launch {
                    when(resp) {
                       true -> hostState.showSnackbar(
                            message = context.getString(R.string.delete_record),
                            actionLabel = context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        ).also {
                            viewModelScope.launch {
                                main()
                            }
                       }
                        false -> hostState.showSnackbar(
                            message = context.getString(R.string.error_delete_record),
                            actionLabel = context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    fun updateAdditional(id:Int){
        navController?.let{
            AdditionalList.navigateForm(id,code,it)
        }
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