package co.com.japl.module.creditcard.views.email.form

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.emailcreditcard.form.EmailCreditCardViewModel
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun EmailBought(viewModel: EmailCreditCardViewModel){
    val load by viewModel.load

    LaunchedEffect(Unit) {
        viewModel.main()
    }

    if (load) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel: EmailCreditCardViewModel) {
    val valueCC = viewModel.creditCard
    val listCC = viewModel.creditCardList
    val isErrorCC = viewModel.errorCreditCard
    
    val valueKCB = viewModel.kindInterestRate
    val listKCB = viewModel.kindInterestRateList
    val isErrorKCB = viewModel.errorKindInterestRate
    
    val sender = viewModel.sender
    val isErrorSender = viewModel.errorSender
    
    val subjectPattern = viewModel.subjectPattern
    val isErrorSubjectPattern = viewModel.errorSubjectPattern
    
    val bodyPattern = viewModel.bodyPattern
    val isErrorBodyPattern = viewModel.errorBodyPattern
    
    val validationResult = viewModel.validationResult

    Scaffold(
        floatingActionButton = { Buttons(addClick = { viewModel.save() }, clear = { viewModel.clean() }) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PADDING_SHORT)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            FieldSelect(
                title = stringResource(R.string.credit_card),
                value = valueCC.value?.second ?: "",
                list = listCC,
                isError = isErrorCC,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.PADDING_TOP)
            ) {
                valueCC.value = it
            }

            FieldSelect(
                title = stringResource(R.string.kind_credit_rate),
                value = valueKCB.value?.second ?: "",
                list = listKCB,
                isError = isErrorKCB,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.PADDING_TOP)
            ) {
                valueKCB.value = it
            }

            FieldText(
                title = stringResource(R.string.sender),
                value = sender.value,
                hasErrorState = isErrorSender,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.PADDING_TOP)
            ) {
                sender.value = it
            }

            FieldText(
                title = stringResource(R.string.subject_email_pattern),
                value = subjectPattern.value,
                hasErrorState = isErrorSubjectPattern,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.PADDING_TOP)
            ) {
                subjectPattern.value = it
            }

            FieldText(
                title = stringResource(R.string.message_email_pattern),
                value = bodyPattern.value,
                hasErrorState = isErrorBodyPattern,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.PADDING_TOP)
            ) {
                bodyPattern.value = it
            }

            OutlinedButton(
                onClick = { viewModel.validatePatternWithMessages() },
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(top = Dimensions.PADDING_TOP)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.DirectionsRun,
                    contentDescription = stringResource(id = R.string.validate),
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(id = R.string.validate),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }

            FieldText(
                title = stringResource(id = R.string.validate),
                value = validationResult.value,
                lines = 6,
                callback = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.PADDING_TOP)
            )
        }
    }
}

@Composable
private fun Buttons(addClick: () -> Unit, clear: () -> Unit) {
    Column {
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = R.string.clear
        ) {
            clear()
        }

        FloatButton(
            imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.add
        ) {
            addClick()
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
internal fun EmailBoughtPreview(){
    val vm = getViewModel()
    vm.load.value = false
    MaterialThemeComposeUI {
        EmailBought(vm)
    }
}

@Composable
private fun getViewModel(): EmailCreditCardViewModel {
    return EmailCreditCardViewModel(
        codeEmailCC = null,
        svc = null,
        creditCardSvc = null,
        navController = null
    )
}
