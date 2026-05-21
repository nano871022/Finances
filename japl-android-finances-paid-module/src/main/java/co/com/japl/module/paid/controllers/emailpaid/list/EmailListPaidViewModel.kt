package co.com.japl.module.paid.controllers.emailpaid.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.module.paid.navigations.EmailPaid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailListPaidViewModel(
    private val svc: IEmailPaidPort?,
    private val navController: NavController?
) : ViewModel() {

    val load = mutableStateOf(true)
    val list = mutableStateListOf<EmailPaidDTO>()

    fun main() {
        viewModelScope.launch(Dispatchers.IO) {
            svc?.getAll()?.let {
                withContext(Dispatchers.Main) {
                    list.clear()
                    list.addAll(it)
                    load.value = false
                }
            }
        }
    }

    fun add() {
        navController?.let { EmailPaid.navigate(it) }
    }

    fun edit(id: Int) {
        navController?.let { EmailPaid.navigate(id, it) }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (svc?.delete(id) == true) {
                withContext(Dispatchers.Main) {
                    list.removeIf { it.id == id }
                }
            }
        }
    }

    fun activate(id: Int, active: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (svc?.activate(id, active) == true) {
                withContext(Dispatchers.Main) {
                    val index = list.indexOfFirst { it.id == id }
                    if (index != -1) {
                        list[index] = list[index].copy(active = active)
                    }
                }
            }
        }
    }

    fun clone(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (svc?.clone(id) == true) {
                main()
            }
        }
    }
}
