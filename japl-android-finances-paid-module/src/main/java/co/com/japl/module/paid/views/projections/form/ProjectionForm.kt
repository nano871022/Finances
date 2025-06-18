package co.com.japl.module.paid.views.projections.form

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.projections.forms.ProjectionFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ProjectionForm(viewModel: ProjectionFormViewModel){
    val loading = remember { viewModel.loaderStatus }

    if(loading.value){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else{
        Scafold(viewModel=viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Scafold(viewModel: ProjectionFormViewModel){
    val snackhost = remember {viewModel.hostState}
    Scaffold (
        snackbarHost = {SnackbarHost(hostState = snackhost)},
        floatingActionButton = {
            FloatButton(viewModel=viewModel)
        }){
        Body(viewModel=viewModel,modifier = Modifier.padding(it))
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: ProjectionFormViewModel,modifier:Modifier){
    Column (modifier = modifier.fillMaxWidth().padding(Dimensions.PADDING_VIEW_SPACE)){
        FieldDatePicker(
            title = R.string.date_of_projection,
            value = viewModel.datePayment.valueStr,
            isError = viewModel.datePayment.error,
            validation = {viewModel.validate()},
            callable = {viewModel.datePayment.onValueChangeStr(it)},
            modifier = modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldSelect(
            title = stringResource(id = R.string.period_projection),
            value = viewModel.period.value.second,
            list = viewModel.period.list,
            cleanTitle = R.string.clean_period,
            isError = viewModel.period.error,
            callable = {
                it?.let {
                    viewModel.period.onValueChange(it)
                }?:viewModel.period.reset(Pair(0,""))},
            modifier = modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldText(
            title = stringResource(id = R.string.name_projection),
            value = viewModel.name.value,
            clearTitle = R.string.clean_period,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.name.error,
            validation = {viewModel.validate()},
            callback = {viewModel.name.onValueChange(it)},
            modifier = modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldText(
            title = stringResource(id = R.string.value_projection),
            value = viewModel.value.valueStr,
            keyboardType = KeyboardOptions.Default.copy( keyboardType = KeyboardType.Decimal ),
            currency = true,
            clearTitle = R.string.clean_period,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.value.error,
            validation = {viewModel.validate()},
            callback = {viewModel.value.onValueChangeStr(it)},
            modifier = modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldView(
            title = stringResource(id = R.string.quote_projection),
            value = NumbersUtil.COPtoString(viewModel.quote.value),
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
private fun FloatButton(viewModel: ProjectionFormViewModel){
    val disableStatus = remember { viewModel.disableSaveStatus }
    Column{
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = R.string.clean_form
        ){
            viewModel.clear()
        }
        if(disableStatus.value.not()) {
            FloatButton(
                imageVector = Icons.Rounded.Add,
                descriptionIcon = R.string.add_projection
            ) {
                viewModel.save()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, backgroundColor = 0xFFFFFFFF)
private fun ProjectionFormPreviewLight(){
    MaterialThemeComposeUI{
        ProjectionForm(getViewModel())
    }
}

@Composable
private fun getViewModel(): ProjectionFormViewModel{
    val vm = ProjectionFormViewModel(context= LocalContext.current)
    vm.datePayment.onValueChange(LocalDate.now())
    vm.period.onValueChange(Pair(KindPaymentsEnums.MONTHLY.month,KindPaymentsEnums.MONTHLY.name))
    vm.name.onValueChange("Salario")
    vm.value.onValueChange(BigDecimal.valueOf(1_000_000))
    vm.quote.onValueChange(BigDecimal.valueOf(100_000))
    return vm
}