package co.japl.android.myapplication.finanzas.view.creditcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.view.components.CheckBoxField
import co.japl.android.myapplication.finanzas.view.components.FieldText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreditCard(viewModel:CreditCardViewModel){

    val progression = remember{viewModel.progress}
    val showProgress = remember{viewModel.showProgress}

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(showProgress.value){
        LinearProgressIndicator(progress = progression.floatValue)
    }else{
        Body(viewModel = viewModel)
    }
}

@Composable
private fun Body(viewModel: CreditCardViewModel){
    val nameState = remember { viewModel.name }
    val maxQuotesState = remember { viewModel.maxQuotes }
    val cutOffDayState = remember { viewModel.cutOffDay }
    val warningValueState = remember { viewModel.warningValue }

    val statusState = remember { viewModel.state }
    val interest1QuoteState = remember { viewModel.interest1Quote }
    val interest1NotQuoteState = remember { viewModel.interest1NotQuote }

    val hasErrorNameState = remember { viewModel.hasErrorName}
    val hasErrorQuoteMaxState = remember { viewModel.hasErrorQuoteMax}
    val hasErrorCutOfDayState = remember {viewModel.hasErrorCutOfDay}
    val hasErrorWarningState = remember {viewModel.hasErrorWarning}

    Column {

        FieldText(title = stringResource(id = R.string.credit_card),
            value = nameState.value,
            callback = { nameState.value = it },
            icon=Icons.Rounded.Cancel,
            hasErrorState = hasErrorNameState,
            validation = { viewModel.validate() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldText(title = stringResource(id = R.string.max_quotes),
            value = maxQuotesState.value,
            keyboardType = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            hasErrorState = hasErrorQuoteMaxState,
            icon=Icons.Rounded.Cancel,
            callback = {maxQuotesState.value = it},
            validation = { viewModel.validate() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldText(title = stringResource(id = R.string.cut_off_day),
            value = cutOffDayState.value,
            keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
            icon=Icons.Rounded.Cancel,
            hasErrorState = hasErrorCutOfDayState,
            callback = {cutOffDayState.value = it},
            validation = { viewModel.validate() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldText(title = stringResource(id = R.string.warning_quote_value),
            value = warningValueState.value,
            keyboardType = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            icon=Icons.Rounded.Cancel,
            hasErrorState = hasErrorWarningState,
            callback = {warningValueState.value = it},
            validation = { viewModel.validate() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        CheckBoxField(
            title = stringResource(id = R.string.status),
            value = statusState.value,
            callback = {
                statusState.value = it
                viewModel.validate()
            },modifier= Modifier
                .fillMaxWidth()
                .padding(5.dp))

        CheckBoxField(
            title = stringResource(id = R.string.interest_1_quote),
            value = interest1QuoteState.value,
            callback = {
                interest1QuoteState.value = it
                viewModel.validate()
            },modifier= Modifier
                .fillMaxWidth()
                .padding(5.dp))

        CheckBoxField(
            title = stringResource(id = R.string.interest_1not_quote),
            value = interest1NotQuoteState.value,
            callback = {
                interest1NotQuoteState.value = it
                viewModel.validate()
            },modifier= Modifier
                .fillMaxWidth()
                .padding(5.dp))

        ButtonSave(viewModel = viewModel)
    }
}

@Composable
private fun ButtonSave(viewModel:CreditCardViewModel){
    val buttonsState = remember { viewModel.showButtons }
    val buttonUpdateState = remember { viewModel.showButtonUpdate}
    if(buttonsState.value) {
        Row {
            if (!buttonUpdateState.value) {
                Button(onClick = { viewModel.create() }, modifier=Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = stringResource(id = R.string.create)
                    )

                    Text(text = stringResource(id = R.string.create))
                }
            } else {
                
                Button(onClick = { viewModel.goSettings() }, modifier=Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = stringResource(
                        id = R.string.setting_redirect
                    ))
                    Text(text = stringResource(id = R.string.setting_redirect))
                }
                
                Button(onClick = { viewModel.update() }, modifier=Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Rounded.Update,
                        contentDescription = stringResource(id = R.string.update)
                    )

                    Text(text = stringResource(id = R.string.update))
                }
            }
        }
    }
}