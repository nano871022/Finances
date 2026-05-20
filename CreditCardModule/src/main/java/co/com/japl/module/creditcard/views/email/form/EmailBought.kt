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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.emailcreditcard.form.EmailCreditCardViewModel
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.Popup
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
    val aiEnabled = viewModel.isAIValid()

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

            if (aiEnabled) {
                Button(
                    onClick = { viewModel.loadEmailSamples() },
                    modifier = Modifier.align(Alignment.End).padding(top = Dimensions.PADDING_TOP)
                ) {
                    Text(
                        text = stringResource(R.string.generate_by_ai),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
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
        }
    }

    ValidationPopup(viewModel)
    DialogAIEmail(viewModel)
}

@Composable
private fun DialogAIEmail(viewModel: EmailCreditCardViewModel) {
    val showDialog = viewModel.showEmailDialog
    val examples = viewModel.emailSamples
    val aiFailed = viewModel.aiFailed

    if (showDialog.value && examples.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.showEmailDialog.value = false },
            title = { Text(text = stringResource(R.string.select_example_email), color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    examples.forEachIndexed { index, pair ->
                        CheckBoxField(
                            title = pair.first,
                            value = pair.second,
                            callback = { viewModel.emailSamples[index] = pair.copy(second = it) }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.showEmailDialog.value = false
                    viewModel.generateRegexWithAI()
                }) {
                    Text(stringResource(R.string.generate), color = MaterialTheme.colorScheme.onSurface)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showEmailDialog.value = false }) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
    if ((showDialog.value && examples.isEmpty()) || aiFailed.value) {
        AlertDialogOkCancel(
            title = R.string.no_data,
            confirmNameButton = R.string.ok,
            onDismiss = {
                viewModel.showEmailDialog.value = false
                viewModel.aiFailed.value = false
            },
            onClick = {
                viewModel.showEmailDialog.value = false
                viewModel.aiFailed.value = false
            }
        )
    }
}

@Composable
private fun ValidationPopup(viewModel: EmailCreditCardViewModel) {
    val showPopup = viewModel.showPopup
    val validating = viewModel.validating.value
    val validationResults = viewModel.validationResults

    Popup(title = R.string.validation_results_title, state = showPopup) {
        Column(
            modifier = Modifier
                .padding(Dimensions.PADDING_SHORT)
                .fillMaxWidth()
        ) {
            if (validating) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                Carousel(
                    size = validationResults.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) { index ->
                    val result = validationResults[index]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimensions.PADDING_SHORT)
                    ) {
                        if (result.matched) {
                            Text(
                                text = stringResource(id = R.string.extracted_name, result.name ?: ""),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = stringResource(id = R.string.extracted_value, result.value ?: ""),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = stringResource(id = R.string.extracted_date, result.date ?: ""),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = result.bodySnippet,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.not_extracted),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(Dimensions.PADDING_SHORT))
                            Text(
                                text = result.bodySnippet,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = Dimensions.PADDING_SHORT))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.emails_found, validationResults.size),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(id = R.string.emails_matched, validationResults.count { it.matched }),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(Dimensions.PADDING_SHORT))

                OutlinedButton(onClick = { showPopup.value = false }, modifier = Modifier.align(Alignment.End)) {
                    Text(text = stringResource(id = R.string.validation_close), color = MaterialTheme.colorScheme.onPrimary)
                }
            }
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
