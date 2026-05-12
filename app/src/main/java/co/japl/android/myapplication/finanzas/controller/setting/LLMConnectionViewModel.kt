package co.japl.android.myapplication.finanzas.controller.setting

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.dtos.LLMConfigDTO
import co.com.japl.finances.iports.enums.LLMType
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R

class LLMConnectionViewModel(private val prefs: Prefs, private val context: Context) : ViewModel() {
    val llmEnabled = mutableStateOf(prefs.llmEnabled)
    val llmType = mutableStateOf(prefs.llmType)
    val llmApiKey = mutableStateOf(prefs.llmApiKey)

    val llmTypeList = LLMType.values().map { Pair(it.ordinal, it.name) }

    fun save() {
        prefs.llmEnabled = llmEnabled.value
        prefs.llmType = llmType.value
        prefs.llmApiKey = llmApiKey.value
        Toast.makeText(context, R.string.toast_save_successful, Toast.LENGTH_SHORT).show()
    }
}
