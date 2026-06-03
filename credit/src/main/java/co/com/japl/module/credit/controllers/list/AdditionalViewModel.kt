package co.com.japl.module.credit.controllers.list

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.AdditionalList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import androidx.lifecycle.SavedStateHandle
import co.com.japl.module.credit.params.AdditionalCreditParams
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AdditionalViewModel @Inject constructor(
    @ApplicationContext private val context: Context, 
    private val savedStateHandle: SavedStateHandle,
    private val additionalSvc: IAdditional?
): ViewModel(){

    private val code: Int = AdditionalCreditParams.download(savedStateHandle).second
    var navController: NavController? = null

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
        viewModelScope.launch {
            val resp = withContext(Dispatchers.IO) {
                additionalSvc?.delete(idCode) ?: false
            }
            when(resp) {
               true -> hostState.showSnackbar(
                    message = context.getString(R.string.delete_record),
                    actionLabel = context.getString(R.string.close),
                    duration = SnackbarDuration.Short
                ).also {
                    main()
               }
                false -> hostState.showSnackbar(
                    message = context.getString(R.string.error_delete_record),
                    actionLabel = context.getString(R.string.close),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    fun updateAdditional(id:Int){
        navController?.let{
            AdditionalList.navigateForm(id,code,it)
        }
    }

    fun main() {
        _loading.value = true
        viewModelScope.launch {
            execute()
        }
    }

    suspend fun execute(){
        withContext(Dispatchers.IO) {
            additionalSvc?.getAdditional(code)
        }?.forEach(list::add)
        _loading.value = false
    }
}
