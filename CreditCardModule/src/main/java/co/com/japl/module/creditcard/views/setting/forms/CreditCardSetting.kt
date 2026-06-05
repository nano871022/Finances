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
import androidx.compose.material.icons.rounded.Clear
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.module.creditcard.enums.MoreOptionsItemsTypeSettings
import co.com.japl.ui.components.FieldSelect

@Composable
fun CreditCardSetting(viewModel: CreditCardSettingViewModel) {
    val showProgress = remember { viewModel.showProgress }

    if (showProgress.value) {

        viewModel.execute()

        Column(modifier = Modifier.fillMaxWidth()) {
            LinearProgressIndicator(

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
            isMoney = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Form(viewModel: CreditCardSettingViewModel) {
    val context = LocalContext.current
    val name = viewModel.name.value.collectAsState()
    val value = remember { viewModel.value.valueStr}
    val type = viewModel.type.value.collectAsState()
    val active = viewModel.active.value.collectAsState()

    Column {
        FieldSelect(
            title = stringResource(R.string.type),
            value = type.value,
            list = MoreOptionsItemsTypeSettings.entries,
            isError = viewModel.type.error,
            callable = {
                it?.let {
                    viewModel.type.onValueChange(context.getString(it.getName()))
                }
                       },
            modifier = Modifier.fillMaxWidth()
        )

        FieldText(
            title = stringResource(R.string.name),
            value = name.value,
            clearTitle = R.string.clear,
            icon=Icons.Rounded.Clear,
            validation = {viewModel.name.validate()},
            hasErrorState = viewModel.name.error,
            callback = viewModel.name::onValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        FieldText(
            title = stringResource(R.string.value),
            value = value,
            clearTitle = R.string.clear,
            icon=Icons.Rounded.Clear,
            validation = viewModel.value::validate,
            hasErrorState = viewModel.value.error,
            callback = viewModel.value::onValueChange,
            modifier = Modifier.fillMaxWidth()
        )



        Row(modifier = Modifier.padding(top = Dimensions.PADDING_SHORT)) {
            Text(
                text = stringResource(R.string.active),
                modifier = Weight1f()
            )
            Switch(
                   checked = active.value,
                   onCheckedChange =  viewModel.active::onValueChange
            )
        }
    }
}

@Composable
private fun Buttons(viewModel: CreditCardSettingViewModel) {
    val newOne = remember { viewModel.newOne }
    val button = remember { viewModel.showButtons }

    if(button.value) {
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
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun CreditCardSettingPreview() {
    val viewModel = CreditCardSettingViewModel(
        LocalContext.current,
        SavedStateHandle(),
        object:ICreditCardPort{
            override fun getCreditCard(codeCreditCard: Int): CreditCardDTO? {
                TODO("Not yet implemented")
            }

            override fun getAll(): List<CreditCardDTO> {
                TODO("Not yet implemented")
            }

            override fun delete(id: Int): Boolean {
                TODO("Not yet implemented")
            }

            override fun create(dto: CreditCardDTO): Int {
                TODO("Not yet implemented")
            }

            override fun update(dto: CreditCardDTO): Boolean {
                TODO("Not yet implemented")
            }
        },
        object: ICreditCardSettingPort{
            override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
                TODO("Not yet implemented")
            }

            override fun get(
                codeCreditCard: Int,
                codeCreditCardSetting: Int
            ): CreditCardSettingDTO? {
                TODO("Not yet implemented")
            }

            override fun delete(
                codeCreditCard: Int,
                codeCreditCardSetting: Int
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun update(dto: CreditCardSettingDTO): Boolean {
                TODO("Not yet implemented")
            }

            override fun create(dto: CreditCardSettingDTO): Int {
                TODO("Not yet implemented")
            }
        })
    viewModel.showProgress.value = false
    MaterialThemeComposeUI {
        CreditCardSetting(viewModel = viewModel)
    }
}
