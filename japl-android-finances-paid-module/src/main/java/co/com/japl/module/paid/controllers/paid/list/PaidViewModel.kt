package co.com.japl.module.paid.controllers.paid.list

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.enums.PaidListOptions
import co.com.japl.module.paid.navigations.Paid
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaidViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val paidSvc: IPaidPort,
    val prefs: Prefs
) : ViewModel() {
    private var accountCode: Int = 0
    private var period: YearMonth = YearMonth.now()

    val progressStatus = mutableStateOf(0.0f)
    val loaderState = mutableStateOf(true)

    val list = mutableStateMapOf<YearMonth, List<PaidDTO>>()

    val periodOfList = mutableStateOf("")
    val allValues = mutableStateOf(0.0)

    init {
        accountCode = savedStateHandle.get<Int>("code_account") ?: 0
        savedStateHandle.get<String>("date_period")?.let {
            period = YearMonth.from(LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE))
        }
        main()
    }

    fun newOne(navController: NavController) {
        Paid.navigate(accountCode, navController)
    }

    fun delete(id: Int, context: Context) {
        if (paidSvc.delete(id)) {
            Toast.makeText(context, R.string.toast_successful_deleted, Toast.LENGTH_LONG).show()
            loaderState.value = true
        } else {
            Toast.makeText(context, R.string.toast_unsuccessful_deleted, Toast.LENGTH_LONG).show()
        }
    }

    fun edit(id: Int, navController: NavController) {
        Paid.navigate(id, accountCode, navController)
    }

    fun endRecurrent(id: Int, context: Context) {
        if (paidSvc.endRecurrent(id)) {
            Toast.makeText(context, R.string.toast_successful_end_recurrent, Toast.LENGTH_LONG).show()
            loaderState.value = true
        } else {
            Toast.makeText(context, R.string.toast_unsuccessful_end_recurrent, Toast.LENGTH_LONG).show()
        }
    }

    fun copy(id: Int, context: Context) {
        if (paidSvc.copy(id)) {
            Toast.makeText(context, R.string.toast_successful_copy, Toast.LENGTH_LONG).show()
            loaderState.value = true
        } else {
            Toast.makeText(context, R.string.toast_unsuccessful_copy, Toast.LENGTH_LONG).show()
        }
    }

    fun listOptions(dto: PaidDTO): List<PaidListOptions> {
        var list = PaidListOptions.values().toList()
        if (!dto.recurrent) {
            list = PaidListOptions.values().toList().filter { it != PaidListOptions.END }
        }
        if (!dto.datePaid.isAfter(LocalDateTime.now().minusMonths(2))) {
            list = list.filter { it != PaidListOptions.EDIT }
        }
        return list
    }

    private fun main() = runBlocking {
        periodOfList.value = period.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "CO")))
        progressStatus.value = 0.0f
        execute()
        progressStatus.value = 1.0f
    }

    private suspend fun execute() {
        paidSvc.get(accountCode, period).takeIf { it.isNotEmpty() }?.let {
            list.clear()
            progressStatus.value = 0.3f
            list.putAll(it.sortedByDescending { it.datePaid }.groupBy { YearMonth.of(it.datePaid.year, it.datePaid.monthValue) })
            progressStatus.value = 0.5f
            it.sumOf { it.itemValue }.let { allValues.value = it }
            progressStatus.value = 0.8f
        }
        loaderState.value = false
    }

    companion object {
        fun create(extras: CreationExtras, paidSvc: IPaidPort, prefs: Prefs): PaidViewModel {
            val savedStateHandle = extras.createSavedStateHandle()
            return PaidViewModel(savedStateHandle, paidSvc, prefs)
        }
    }
}