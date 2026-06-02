package co.com.japl.module.credit.views.forms

import android.os.Build
import android.content.res.Configuration
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.forms.AdditionalFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndPaddintRightSpace
import co.com.japl.ui.utils.DateUtils
import androidx.lifecycle.SavedStateHandle
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun AdditionalForm(viewModel: AdditionalFormViewModel){
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = viewModel.hostState)
        },
        floatingActionButton = {
            Buttons(viewModel)
        }
    ){
        Column(modifier = Modifier.padding(it)) {
            Form(viewModel)
        }
    }
}

@Composable
private fun Buttons(viewModel: AdditionalFormViewModel){
    Column {
        FloatButton(imageVector = Icons.Rounded.CleaningServices, descriptionIcon = R.string.clear) {
            viewModel.clear()
        }
        FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
            viewModel.create()
        }
    }
}

@Composable
private fun Form(viewModel: AdditionalFormViewModel){
    val name by viewModel.name.value.collectAsState()
    val value by viewModel.value.value.collectAsState()
    val startDate by viewModel.startDate.value.collectAsState()

    Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        FieldText(
            title = stringResource(id = R.string.name),
            value = name,
            callback = { viewModel.name.onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            validation = { viewModel.name.validate() }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT)) {
            FieldText(
                title = stringResource(id = R.string.value),
                value = value.takeIf { it > BigDecimal.ZERO }?.toString()?:"",
                callback = { viewModel.value.onValueChangeStr(it) },
                modifier = Weight1fAndPaddintRightSpace(),
                validation = { viewModel.value.validate() },
                currency = true
            )

            FieldDatePicker(
                title = R.string.start_date,
                value = DateUtils.localDateToString(startDate),
                callable = { date -> viewModel.startDate.onValueChange(DateUtils.toLocalDate(date)) },
                modifier = Weight1f(),
                validation = { viewModel.startDate.validate() }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun AdditionalFormPreview(){
    MaterialThemeComposeUI {
        AdditionalForm(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AdditionalFormPreviewDark(){
    MaterialThemeComposeUI {
        AdditionalForm(getViewModel())
    }
}

@Composable
private fun getViewModel(): AdditionalFormViewModel{
    val vm = AdditionalFormViewModel(
        context = LocalContext.current, savedStateHandle = SavedStateHandle(),
        additionalSvc = TODO(),
    )
    vm.loading.value = false
    return vm
}
