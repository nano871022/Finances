package co.japl.android.myapplication.finanzas.view.setting

import android.content.Context
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
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext


@Composable
fun LLMConnectionForm(viewModel: LLMConnectionViewModel) {
    val progress = remember { viewModel.loadProgress }
    val loading = remember { viewModel.isLoadingModels }

    viewModel.main()

    if(loading.value) {
        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }else {
        Body(viewModel)
    }

}
@Composable
private fun Body(viewModel: LLMConnectionViewModel){
    val enabled by viewModel.llmEnabled
    val type by viewModel.llmType
    val apiKey by viewModel.llmApiKey
    val model by viewModel.llmModel
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

            FieldSelect(
                title = "Proveedor de IA",
                value = type,
                list = typeList,
                modifier = Modifier.fillMaxWidth(),
                callAble = { it?.let { 
                    viewModel.llmType.value = it.second
                    viewModel.main()
                } }
            )

            FieldText(
                title = "API Key",
                value = apiKey,
                callback = { 
                    viewModel.llmApiKey.value = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                FieldSelect(
                    title = "Modelo",
                    value = model,
                    list = modelsList,
                    modifier = Modifier.weight(1f),
                    callAble = { it?.let { viewModel.llmModel.value = it.second } }
                )
                
                IconButton(onClick = { viewModel.main() }, enabled = !isLoadingModels) {
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

@Preview
@Composable
private fun FormPreview(){
    val vm = getLLMConnectFViewModel()
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
        override suspend fun getAiResponse(prompt: String): Result<String> {
            return Result.success("Respuesta de IA")
        }
        override suspend fun getModels(): Result<List<String>> {
            return Result.success(listOf("Modelo 1", "Modelo 2"))
        }
    }

    return LLMConnectionViewModel(
        prefs,
        context,
        llmsSvc)
}


