package co.com.japl.module.creditcard.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.CreditCardViewModel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreditCard(viewModel:CreditCardViewModel){

    val progression = remember{viewModel.progress}
    val showProgress = remember{viewModel.showProgress}

    val buttonsState = remember { viewModel.showButtons }
    val buttonUpdateState = remember { viewModel.showButtonUpdate}

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(showProgress.value){
        LinearProgressIndicator(progress = progression.floatValue)
    }else{
        Scaffold(
            floatingActionButton = {
                Column {
                        if(buttonUpdateState.value) {
                            IconButton(onClick = { viewModel.goSettings() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = stringResource(id = R.string.setting_redirect)
                                )
                            }

                            IconButton(onClick = { viewModel.update() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Update,
                                    contentDescription = stringResource(id = R.string.update)
                                )
                            }
                        }else {
                            IconButton(onClick = { viewModel.create() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Create,
                                    contentDescription = stringResource(id = R.string.create)
                                )
                            }
                        }
                    }
            }
        ) {
            Body(viewModel = viewModel, modifier = Modifier.padding(it))
        }
    }
}

@Composable
private fun Body(viewModel: CreditCardViewModel,modifier:Modifier=Modifier){
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
            currency = true,
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
    }
}