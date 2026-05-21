package co.japl.android.myapplication.finanzas.view.setting

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.*
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.setting.LLMConnectionViewModel


@Composable
fun LLMConnectionForm(viewModel: LLMConnectionViewModel) {
    val progress = remember { viewModel.loadProgress }
    val loading = remember { viewModel.isLoadingModels }

    LaunchedEffect(Unit) {
        viewModel.loadModels()
    }

    Column(modifier=Modifier.fillMaxWidth()) {

        if (loading.value) {
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxWidth(),
                color = ProgressIndicatorDefaults.linearColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        } else {
            Body(viewModel)
        }
    }

}
@Composable
private fun Body(viewModel: LLMConnectionViewModel){
    val enabled by viewModel.llmEnabled
    val type by viewModel.llmType
    val apiKey by viewModel.llmApiKey
    val model by viewModel.llmModel
    val geminiUrl by viewModel.llmGeminiUrl
    val deepSeekUrl by viewModel.llmDeepSeekUrl
    val typeList = remember { viewModel.llmTypeList.toMutableStateList() }
    val modelsList = viewModel.modelsList
    val isLoadingModels by viewModel.isLoadingModels
    Scaffold(
        floatingActionButton = {
            FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
                viewModel.save()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(Dimensions.PADDING_SHORT)
                .fillMaxSize()
        ) {
            CheckBoxField(
                title = "Habilitar IA",
                value = enabled,
                callback = { viewModel.llmEnabled.value = it }
            )

            if (viewModel.llmEnabled.value) {
                FieldSelect(
                    title = "Proveedor de IA",
                    value = type,
                    list = typeList,
                    modifier = Modifier.fillMaxWidth(),
                    callAble = {
                        it?.let {
                            viewModel.llmType.value = it.second
                        }
                    }
                )

                FieldText(
                    title = "API Key",
                    value = apiKey,
                    callback = {
                        viewModel.llmApiKey.value = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (viewModel.llmType.value == "GEMINI") {
                    FieldText(
                        title = "Gemini URL",
                        value = geminiUrl,
                        callback = { viewModel.llmGeminiUrl.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (viewModel.llmType.value == "DEEP_SEEK") {
                    FieldText(
                        title = "DeepSeek URL",
                        value = deepSeekUrl,
                        callback = { viewModel.llmDeepSeekUrl.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    FieldSelect(
                        title = "Modelo",
                        value = model,
                        list = modelsList,
                        modifier = Modifier.weight(1f),
                        callAble = { it?.let { viewModel.llmModel.value = it.second } }
                    )

                    IconButton(onClick = { viewModel.loadModels() }, enabled = !isLoadingModels) {
                        if (isLoadingModels) {
                            CircularProgressIndicator(modifier = Modifier.size(Dimensions.ICON_SIZE_MEDIUM))
                        } else {
                            Icon(Icons.Rounded.Refresh, contentDescription = "Cargar modelos")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormLLMPreview(){
    val vm = getLLMConnectFViewModel()
    vm.llmEnabled.value = true
    MaterialThemeComposeUI {
        LLMConnectionForm(
            viewModel=vm
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FormLLMProgressPreview(){
    val vm = getLLMConnectFViewModel()
    vm.loadProgress.value = 0.5f
    vm.isLoadingModels.value = true
    MaterialThemeComposeUI {
        LLMConnectionForm(
            viewModel=vm
        )
    }
}

@Composable
fun getLLMConnectFViewModel(): LLMConnectionViewModel {
    var context = LocalContext.current
    val prefs = Prefs(context)
    var llmsSvc = object : ILLMService {
        override suspend fun getAiResponse(
            prompt: String,
            model: String?
        ): Result<String> {
            TODO("Not yet implemented")
        }

        override suspend fun getModels(): Result<List<String>> {
            TODO("Not yet implemented")
        }

    }

    return LLMConnectionViewModel(
        prefs,
        context,
        llmsSvc)
}
