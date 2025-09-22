package co.com.japl.module.paid.controllers.accounts.form

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.module.paid.R
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class AccountViewModel constructor(private val savedStateHandle: SavedStateHandle, private val accountSvc: IAccountPort) : ViewModel() {

    private var codeAccount: Int? = null

    var loader = mutableStateOf(true)
    var progress = mutableFloatStateOf(0f)

    private var _item: AccountDTO? = null
    private var save = false

    var name = mutableStateOf("")
    var active = mutableStateOf(true)

    var errorName = mutableStateOf(false)

    init {
        codeAccount = savedStateHandle.get<Int>("id_account")
        main()
    }

    fun validation() {
        errorName.value = name.value.isBlank()
        save = !errorName.value
        if (save) {
            _item = AccountDTO(
                id = if (_item != null && _item?.id ?: 0 > 0) _item?.id!! else 0,
                name = name.value,
                active = active.value,
                create = LocalDate.now()
            )
        }
    }

    fun save(navController: NavController, context: Context) {
        if (save && _item != null) {
            if (_item?.id == 0) {
                if (accountSvc.create(_item!!) > 0) {
                    navController.navigateUp()
                    Toast.makeText(context, R.string.toast_save_successful, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.toast_save_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                if (accountSvc.update(_item!!)) {
                    navController.navigateUp()
                    Toast.makeText(context, R.string.toast_update_successful, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.toast_update_error, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            validation()
        }
    }

    private fun main() = runBlocking {
        progress.value = 0.1f
        execution()
        progress.value = 1f
    }

    private suspend fun execution() {
        progress.value = 0.2f
        codeAccount?.takeIf { it > 0 }?.let {
            accountSvc.getById(it)?.let {
                progress.value = 0.6f
                _item = it
                name.value = it.name
                active.value = it.active
            }
        }
        progress.value = 0.7f
        loader.value = false
        progress.value = 0.8f
    }

    companion object {
        fun create(extras: CreationExtras, accountSvc: IAccountPort): AccountViewModel {
            val savedStateHandle = extras.createSavedStateHandle()
            return AccountViewModel(savedStateHandle, accountSvc)
        }
    }
}