package co.com.japl.module.paid.controllers.accounts.list

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.module.paid.navigations.Account
import kotlinx.coroutines.runBlocking

class AccountViewModel constructor(
    private val accountSvc: IAccountPort?,
    public val inputSvc: IInputPort?,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val progress = mutableFloatStateOf(0f)
    val loading = mutableStateOf(true)
    var list = mutableStateListOf<AccountDTO>()
    val save = false

    fun add(navController: NavController) {
        Account.navigate(navController)
    }

    fun edit(codeAccount: Int, navController: NavController) {
        Account.navigate(codeAccount, navController)
    }

    fun delete(codeAccount: Int) {
        accountSvc?.let {
            accountSvc.delete(codeAccount)
        }
    }


    fun main() = runBlocking {
        progress.value = 0.1f
        services()
        progress.value = 1f
    }

    suspend fun services(){
        progress.value = 0.2f
        accountSvc?.let {
            accountSvc.getAll()?.takeIf { it.isNotEmpty() }?.let {
                list.clear()
                list.addAll(it)
                loading.value = false
            }
        }
        progress.value = 0.8f
        loading.value = false
    }

}