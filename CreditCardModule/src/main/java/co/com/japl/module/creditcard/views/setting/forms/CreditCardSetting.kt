package co.com.japl.module.creditcard.views.setting.forms

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingViewModel
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import androidx.lifecycle.SavedStateHandle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch

@Composable
fun CreditCardSetting(viewModel: CreditCardSettingViewModel) {
    val showProgress = remember { viewModel.showProgress }
    val progress = remember { viewModel.progress }

    if (showProgress.value) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                progress = { progress.floatValue },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.loading_data),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    } else {
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel: CreditCardSettingViewModel) {
    Scaffold(
        floatingActionButton = {
            Buttons(viewModel)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(Dimensions.PADDING_SHORT)
        ) {
            Header(viewModel)
            Form(viewModel)
        }
    }
}

@Composable
private fun Header(viewModel: CreditCardSettingViewModel) {
    viewModel.creditCard?.let {
        FieldView(
            name = R.string.credit_card,
            value = it.name,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Form(viewModel: CreditCardSettingViewModel) {
    val name = remember { viewModel.name }
    val value = remember { viewModel.value }
    val type = remember { viewModel.type }
    val active = remember { viewModel.active }

    Column {
        FieldText(
            title = stringResource(R.string.name),
            value = name.value,
            callback = { name.value = it; viewModel.validate() },
            modifier = Modifier.fillMaxWidth()
        )

        FieldText(
            title = stringResource(R.string.value),
            value = value.value,
            callback = { value.value = it; viewModel.validate() },
            modifier = Modifier.fillMaxWidth()
        )

        FieldText(
            title = stringResource(R.string.type),
            value = type.value,
            callback = { type.value = it; viewModel.validate() },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.padding(top = Dimensions.PADDING_SHORT)) {
            Text(
                text = stringResource(R.string.active),
                modifier = Weight1f()
            )
            Switch(checked = active.value, onCheckedChange = { active.value = it; viewModel.validate() })
        }
    }
}

@Composable
private fun Buttons(viewModel: CreditCardSettingViewModel) {
    val newOne = remember { viewModel.newOne }
    if (newOne.value.not()) {
        FloatButton(
            imageVector = Icons.Rounded.Save,
            descriptionIcon = R.string.update
        ) {
            viewModel.update()
        }
    } else {
        FloatButton(
            imageVector = Icons.Rounded.Create,
            descriptionIcon = R.string.save
        ) {
            viewModel.create()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun CreditCardSettingPreview() {
    val viewModel = CreditCardSettingViewModel(SavedStateHandle(), null, null)
    viewModel.showProgress.value = false
    MaterialThemeComposeUI {
        CreditCardSetting(viewModel = viewModel)
    }
}
