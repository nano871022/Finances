package co.com.japl.module.credit.views.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.forms.AdditionalFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.DateUtils

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AdditionalForm(viewModel: AdditionalFormViewModel = viewModel()){
    val loadingState = remember { viewModel.loading }
    if(loadingState.value) {
        LinearProgressIndicator()
    }else {
        ScafoldBody(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun ScafoldBody(viewModel: AdditionalFormViewModel ){
    val state = remember{ viewModel.hostState }
    Scaffold (
        snackbarHost = { SnackbarHost(hostState = state) },
        floatingActionButton = {
            FloatButton(viewModel = viewModel)
        }
    ){
        Body(viewModel = viewModel, modifier = Modifier.padding(it))
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: AdditionalFormViewModel ,modifier: Modifier){

    Column(modifier = modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)){
        FieldDatePicker(
            title =  R.string.start_date ,
            value = viewModel.startDate.valueStr,
            validation = {viewModel.startDate.validate()},
            isError = viewModel.startDate.error ,
            callable = {
                val value = DateUtils.toLocalDate(it)
                viewModel.startDate.onValueChange(value)
                       },
            modifier = Modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT)

        )
        FieldText(
            title = stringResource( R.string.name ),
            value = viewModel.name.value,
            icon = Icons.Rounded.Clear,
            validation = {viewModel.name.validate()},
            hasErrorState = viewModel.name.error,
            callback = {
                viewModel.name.onValueChange(it)
            }, modifier =  Modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT)
        )

        FieldText(
            title = stringResource( R.string.value ),
            value = viewModel.value.valueStr,
            icon = Icons.Rounded.Clear,
            validation = {viewModel.value.validate()},
            currency = true,
            keyboardType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            hasErrorState = viewModel.value.error,
            callback = {
                viewModel.value.onValueChangeStr(it)
            }, modifier =  Modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT)
        )
    }
}

@Composable
private fun FloatButton(viewModel: AdditionalFormViewModel ){
    Column {
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = R.string.clear_additiona_credit_form
        ) {
            viewModel.clear()
        }
        FloatButton(
            imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.add_additiona_credit
        ) {
            viewModel.create()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, backgroundColor = 0X000000)
private fun AdditionalFormPreview(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        AdditionalForm(viewModel)
    }
}

@Composable
private fun getViewModel(): AdditionalFormViewModel{
    val vm = AdditionalFormViewModel(context= LocalContext.current,codeCredit = 0, additionalSvc = null ,navController = null)
    vm.loading.value = false
    return vm
}

