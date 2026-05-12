package co.japl.android.myapplication.finanzas.view.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import co.com.japl.ui.components.*
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.setting.LLMConnectionViewModel

@Composable
fun LLMConnectionForm(viewModel: LLMConnectionViewModel) {
    val enabled by viewModel.llmEnabled
    val type by viewModel.llmType
    val apiKey by viewModel.llmApiKey
    val typeList = remember { viewModel.llmTypeList.toMutableStateList() }

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
                callAble = { it?.let { viewModel.llmType.value = it.second } }
            )

            FieldText(
                title = "API Key",
                value = apiKey,
                callback = { viewModel.llmApiKey.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
