package co.com.japl.module.creditcard.views.simulator

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.simulator.FormViewModel
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.Popup

import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.graphs.utils.NumbersUtil
import java.math.BigDecimal

@Composable
fun Simulator(viewModel: FormViewModel){
    val hasProgress = remember() { mutableStateOf(false) }

    if(hasProgress.value){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else{
        Body(viewModel)
    }

}

@Composable
private fun Body(viewModel: FormViewModel){
    val snackbar = remember { viewModel.snackbar }
    Scaffold( floatingActionButton = {
        FloatButton(viewModel)
    }, snackbarHost = { snackbar.value },
        topBar={
            Text(text=stringResource(R.string.simulator_quote_variable),modifier=Modifier.fillMaxWidth())
        }
    ){
        Column (modifier = Modifier.padding(it)){
            FormHeader(viewModel)
            BodyCalc(viewModel)
            PopupSave(viewModel)
        }
    }
}

@Composable
private fun PopupSave(viewModel: FormViewModel){
    val formName = viewModel.name.value.collectAsState()
    val statePopupSave = remember { viewModel.statePopUp }
    Popup(
        title = R.string.saving,
        state = statePopupSave
    ) {
        Column {
            FieldText(
                title = stringResource(R.string.name),
                value = formName.value,
                icon = Icons.Rounded.Clear,
                validation = { viewModel.name.validate() },
                hasErrorState = viewModel.name.error,
                callback = { viewModel.name.onValueChange(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Row{
                Button(
                    onClick = {
                        statePopupSave.value = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Rounded.Cancel,
                        contentDescription = stringResource(R.string.cancel))

                    Text(text = stringResource(R.string.cancel),
                        modifier=Modifier.padding(start = Dimensions.PADDING_SHORT))
                }

                Button(
                    onClick = {
                        statePopupSave.value = false
                        viewModel.save()
                    },
                    modifier = Modifier.weight(1f).padding(start = Dimensions.PADDING_SHORT)
                ) {
                    Icon(imageVector = Icons.Rounded.Save,
                        contentDescription = stringResource(R.string.save))
                    Text(text = stringResource(R.string.save),
                        modifier=Modifier.padding(start = Dimensions.PADDING_SHORT))
                }
            }
        }
    }
}

@Composable
private fun FloatButton(viewModel: FormViewModel){
    val stateCalculation = remember { viewModel.showCalculation }
    Column{
        FloatButton  (
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = R.string.clear,
        ){
            viewModel.clear()
        }

        if(stateCalculation.value) {
            FloatButton(
                imageVector = Icons.Rounded.Calculate,
                descriptionIcon = R.string.calculate,
            ) {
                viewModel.calculate()
            }
        }
        if(viewModel.stateCalculation.value){
            FloatButton  (
                imageVector = Icons.Rounded.TableChart,
                descriptionIcon = R.string.amortization,
            ){
                viewModel.amortization()
            }
            FloatButton(
                imageVector = Icons.Rounded.Save,
                descriptionIcon = R.string.save,
            ) {
                viewModel.statePopUp.value = true
            }
        }
    }
}

@Composable
private fun FormHeader(viewModel: FormViewModel){
    val formCreditValue by viewModel.creditValue.value.collectAsState()
    val formCreditRate by viewModel.creditRate.value.collectAsState()
    val formCreditKindRate by viewModel.creditKindRate.value.collectAsState()
    val formMonth by viewModel.month.value.collectAsState()

    Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        FieldText(
            title = stringResource(R.string.credit_value),
            value = formCreditValue.takeIf { it > BigDecimal.ZERO }?.toString()?:"",
            icon = Icons.Rounded.Clear,
            validation = {viewModel.creditValue.validate()},
            hasErrorState = viewModel.creditValue.error.value,
            callback = {viewModel.creditValue.onValueChangeStr(it)},
            currency = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row (modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT, bottom = Dimensions.PADDING_TOP)) {
            FieldText(
                title = stringResource(R.string.credit_rate),
                value = formCreditRate.takeIf { it > 0.0 }?.toString()?:"",
                icon = Icons.Rounded.Clear,
                validation = {viewModel.creditRate.validate()},
                hasErrorState = viewModel.creditRate.error.value,
                callback = {viewModel.creditRate.onValueChangeStr(it)},
                decimal = true,
                suffixValue = "%",
                modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically )
            )

            FieldSelect(
                title = stringResource(R.string.kind_credit_rate),
                value = formCreditKindRate.value,
                cleanTitle = R.string.clear,
                isError = viewModel.creditKindRate.error,
                list = viewModel.kindTaxList,
                modifier = Modifier.weight(1f),
                callable = {
                    viewModel.creditKindRate.onValueChange(
                        KindOfTaxEnum.findByIndex(it?.first?:0)
                    )
                }
            )
        }
        FieldText(
            title = stringResource(R.string.months),
            value = formMonth.takeIf { it > 0}?.toString()?:"",
            icon = Icons.Rounded.Clear,
            validation = {viewModel.month.validate()},
            hasErrorState = viewModel.month.error.value,
            callback = {viewModel.month.onValueChangeStr(it)},
            decimal = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BodyCalc(viewModel: FormViewModel){
    val simulator = viewModel.simulator.collectAsState()
    val state = remember { viewModel.stateCalculation }
    if(state.value) {
        Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {

            Text(
                text = stringResource(R.string.values_first_quote),
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = Dimensions.PADDING_TOP,
                        bottom = Dimensions.PADDING_TOP
                    )
            )

            FieldView(
                title = stringResource(R.string.capital_value),
                value = simulator.value.capitalValue?.let(NumbersUtil::COPtoString) ?: "",
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT)
            )
            FieldView(
                title = stringResource(R.string.interest_value),
                value = simulator.value.interestValue?.let(NumbersUtil::COPtoString) ?: "",
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_TOP)
            )
            FieldView(
                title = stringResource(R.string.quote_value),
                value = simulator.value.quoteValue?.let(NumbersUtil::COPtoString) ?: "",
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_TOP)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun SimulatorPreviewLight(){
    MaterialThemeComposeUI {
        Simulator(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
private fun SimulatorPreviewDark(){
    MaterialThemeComposeUI {
        Simulator(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
private fun SimulatorPreviewDarkPopup(){
    val viewModel = getViewModel()
    viewModel.statePopUp.value = true
    MaterialThemeComposeUI {
        Simulator(viewModel)
    }
}

private fun getViewModel():FormViewModel{
    return FormViewModel().also {
        it.month.onValueChange(12)
        it.creditRate.onValueChange(13.0)
        it.creditValue.onValueChange(BigDecimal.valueOf(1000000))
        it.stateCalculation.value = true
        it.simulator.value.capitalValue = BigDecimal.valueOf(200000)
        it.simulator.value.interestValue = BigDecimal.valueOf(100000)
        it.simulator.value.quoteValue = BigDecimal.valueOf(300000)
    }
}