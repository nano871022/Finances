package co.com.japl.module.paid.views.email.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.emailpaid.form.EmailPaidViewModel
import co.com.japl.ui.components.*
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun EmailPaid(viewModel: EmailPaidViewModel) {
    val load by viewModel.load

    LaunchedEffect(Unit) {
        viewModel.main()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (load) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            Body(viewModel)
        }
    }
}

@Composable
private fun Body(viewModel: EmailPaidViewModel) {
    val account = viewModel.account
    val accountList = viewModel.accountList
    val isErrorAccount = viewModel.errorAccount
    val sender = viewModel.sender
    val isErrorSender = viewModel.errorSender
    val subjectPattern = viewModel.subjectPattern
    val isErrorSubjectPattern = viewModel.errorSubjectPattern
    val bodyPattern = viewModel.bodyPattern
    val isErrorBodyPattern = viewModel.errorBodyPattern
    val aiEnabled = viewModel.isAIValid()

    Scaffold(
        floatingActionButton = { Buttons(addClick = { viewModel.save() }, clear = { viewModel.clean() }) },
        modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)
    ) {
        Column(
            modifier = Modifier.padding(it).fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
            FieldSelect(
                title = stringResource(R.string.account),
                value = account.value?.second ?: "",
                list = accountList,
                isError = isErrorAccount,
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_TOP)
            ) { account.value = it }

            FieldText(
                title = stringResource(R.string.sender),
                value = sender.value,
                hasErrorState = isErrorSender,
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_TOP)
            ) { sender.value = it }

            FieldText(
                title = stringResource(R.string.subject_email_pattern),
                value = subjectPattern.value,
                hasErrorState = isErrorSubjectPattern,
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_TOP)
            ) { subjectPattern.value = it }

            FieldText(
                title = stringResource(R.string.message_email_pattern),
                value = bodyPattern.value,
                hasErrorState = isErrorBodyPattern,
                modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_TOP)
            ) { bodyPattern.value = it }

            if (aiEnabled) {
                Button(
                    onClick = { viewModel.loadEmailSamples() },
                    modifier = Modifier.align(Alignment.End).padding(top = Dimensions.PADDING_TOP)
                ) {
                    Text(text = stringResource(R.string.generate_by_ai), color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            OutlinedButton(
                onClick = { viewModel.validatePatternWithMessages() },
                modifier = Modifier.align(Alignment.End).padding(top = Dimensions.PADDING_TOP)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.DirectionsRun, contentDescription = stringResource(R.string.validate), tint = MaterialTheme.colorScheme.onSurface)
                Text(text = stringResource(R.string.validate), color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }
        }
    }
    ValidationPopup(viewModel)
    DialogAIEmail(viewModel)
}

@Composable
private fun Buttons(addClick: () -> Unit, clear: () -> Unit) {
    Column {
        FloatButton(imageVector = Icons.Rounded.CleaningServices, descriptionIcon = R.string.clean_form) { clear() }
        FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.save) { addClick() }
    }
}

@Composable
private fun DialogAIEmail(viewModel: EmailPaidViewModel) {
    val showDialog = viewModel.showEmailDialog
    val examples = viewModel.emailSamples
    viewModel.aiFailed
    val showModelSelection = remember { mutableStateOf(false) }

    if (showDialog.value && examples.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.showEmailDialog.value = false },
            title = { Text(text = stringResource(R.string.select_example_email), color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { showModelSelection.value = !showModelSelection.value }) {
                        Text(text = stringResource(R.string.ai_model), modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                        Icon(imageVector = Icons.Rounded.ArrowDownward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                    }

                    if (showModelSelection.value) {
                        FieldSelect(
                            title = stringResource(R.string.select_ai_model),
                            value = viewModel.selectedLLMModel.value?.second ?: "",
                            list = viewModel.llmModels,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            viewModel.selectedLLMModel.value = it
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimensions.PADDING_SHORT))

                    examples.forEachIndexed { index, pair ->
                        CheckBoxField(title = pair.first, value = pair.second, callback = { viewModel.emailSamples[index] = pair.copy(second = it) })
                    }


                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.showEmailDialog.value = false; viewModel.generateRegexWithAI() }) {
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
}

@Composable
private fun ValidationPopup(viewModel: EmailPaidViewModel) {
    val showPopup = viewModel.showPopup
    val validating = viewModel.validating.value
    val validationResults = viewModel.validationResults

    Popup(title = R.string.validation_results_title, state = showPopup) {
        Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT).fillMaxWidth()) {
            if (validating) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp), color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                Text(text=viewModel.bodyPattern.value,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    modifier = Modifier.fillMaxWidth())

                Carousel(size = validationResults.size, modifier = Modifier.fillMaxWidth().height(200.dp)) { index ->
                    val result = validationResults[index]
                    Column(modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)) {
                        if (result.matched) {
                            Text(text = stringResource(R.string.extracted_name, result.name ?: ""), color = MaterialTheme.colorScheme.onPrimary)
                            Text(text = stringResource(R.string.extracted_value, result.value ?: ""), color = MaterialTheme.colorScheme.onPrimary)
                            Text(text = stringResource(R.string.extracted_date, result.date ?: ""), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(text = stringResource(R.string.not_extracted), color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleMedium)
                        }
                        Text(text = result.bodySnippet, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodySmall)
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
                    Text(text = stringResource(R.string.close), color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
