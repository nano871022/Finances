package co.japl.android.myapplication.finanzas.controller.setting

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.enums.LLMType
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LLMConnectionViewModel(private val prefs: Prefs, private val context: Context, private val llmService: ILLMService) : ViewModel() {
    val llmEnabled = mutableStateOf(prefs.llmEnabled)
    val llmType = mutableStateOf(prefs.llmType)
    val llmApiKey = mutableStateOf(prefs.llmApiKey)
    val llmModel = mutableStateOf(prefs.llmModel)
    val llmGeminiUrl = mutableStateOf(prefs.llmGeminiUrl)
    val llmDeepSeekUrl = mutableStateOf(prefs.llmDeepSeekUrl)
    val modelsList = mutableStateListOf<Pair<Int, String>>()
    val isLoadingModels = mutableStateOf(false)

    val llmTypeList = LLMType.values().map { Pair(it.ordinal, it.name) }

    val loadProgress = mutableStateOf(0f)

    fun loadModels() {
        viewModelScope.launch {
            if (llmApiKey.value.isNotEmpty()) {
                loadProgress.value = 0.1f
                isLoadingModels.value = true
                loadProgress.value = 0.3f
                withContext(Dispatchers.IO) {
                    llmService.getModels()
                }.onSuccess { models ->
                    loadProgress.value = 0.5f
                    modelsList.clear()
                    models.forEachIndexed { index, s ->
                        modelsList.add(Pair(index, s))
                    }
                    loadProgress.value = 0.8f
                }.onFailure {
                    loadProgress.value = 0.7f
                    Toast.makeText(
                        context,
                        "Error al cargar modelos: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(this.javaClass.name,"Error al cargar modelos: ${it.message}")
                }
                isLoadingModels.value = false
                loadProgress.value = 1f
            }
        }
    }

    fun save() {
        prefs.llmEnabled = llmEnabled.value
        prefs.llmType = llmType.value
        prefs.llmApiKey = llmApiKey.value
        prefs.llmModel = llmModel.value
        prefs.llmGeminiUrl = llmGeminiUrl.value
        prefs.llmDeepSeekUrl = llmDeepSeekUrl.value
        Toast.makeText(context, R.string.toast_save_successful, Toast.LENGTH_SHORT).show()
    }
}
