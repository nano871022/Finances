package co.com.japl.module.paid.views.sms.form

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.ui.theme.MaterialThemeComposeUI
import  androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.sms.form.SmsViewModel
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.Popup
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f

@Composable
fun Sms(viewModel:SmsViewModel){
    val load by remember {viewModel.load}
    var progress  by remember {viewModel.progress}

    LaunchedEffect(Unit) {
        viewModel.main()
    }
    if(load){
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
        )
    }else{
        Form(viewModel = viewModel)
    }

}

@Composable
private fun Form(viewModel: SmsViewModel){
    Scaffold (
        floatingActionButton = {
            Buttons({viewModel.clean()}, {viewModel.save()})
        }
    ) {
        Body(viewModel = viewModel, modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT))
    }
}

@Composable
private fun Body(viewModel: SmsViewModel,modifier:Modifier) {
    val listCreditCard = remember { viewModel.accountList }
    val creditCard = remember { viewModel.account }
    val errorCreditCard = remember { viewModel.errorAccount }
    val kinInterestList = remember { viewModel.kindInterestRateList }
    val phoneNumber = remember { viewModel.phoneNumber }
    val errorPhoneNumber = remember { viewModel.errorPhoneNumber }
    val pattern = remember { viewModel.pattern }
    val errorPattern = remember { viewModel.errorPattern }
    val validationResult = remember { viewModel.validationResult }
    val stateScroll = rememberScrollState(0)
    val AIValid = remember { viewModel.isAIValid()}

    Column(modifier = modifier.padding(Dimensions.PADDING_SHORT).verticalScroll(stateScroll)) {
        FieldSelect(
            title = stringResource(id = R.string.account),
            value = creditCard.value?.second ?: "",
            list = listCreditCard,
            isError = errorCreditCard,
            modifier = modifier,
            callAble = {
                it?.let {
                    creditCard.value = it
                    viewModel.validate()
                }

            })

        FieldText(
            title = stringResource(id = R.string.phone_number),
            value = phoneNumber.value,
            hasErrorState = errorPhoneNumber,
            validation = { viewModel.validate() },
            icon = Icons.Rounded.Cancel,
            callback = {
                phoneNumber.value = it
            },
            modifier = modifier
        )

        FieldText(
            title = stringResource(id = R.string.pattern),
            value = pattern.value,
            hasErrorState = errorPattern,
            validation = { viewModel.validate() },
            icon = Icons.Rounded.Cancel,
            lines = 3,
            callback = {
                pattern.value = it
            },
            modifier = modifier
        )

        if(AIValid) {
            Button(
                onClick = { viewModel.loadSmsSamples() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(R.string.generate_by_ai), color = MaterialTheme.colorScheme.onPrimary)
            }
        }


        OutlinedButton(
            onClick = { viewModel.validatePatternWithMessages() },
            modifier = modifier.align(alignment = Alignment.End)
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.DirectionsRun,
                contentDescription = stringResource(id = R.string.validate),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = stringResource(id = R.string.validate),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Weight1f()
            )
        }
    }
    ValidationPopup(viewModel)
    AIDialog(viewModel)
}

@Composable
private fun ValidationPopup(viewModel: SmsViewModel) {
    val showPopup = remember { viewModel.showPopup }
    val validating = remember { viewModel.validating }
    val validationResults = remember { viewModel.validationResults }

    Popup(title = R.string.validation_results_title, state = showPopup) {
        Column(
            modifier = Modifier
                .padding(Dimensions.PADDING_SHORT)
                .fillMaxWidth()
        ) {
            if (validating.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                if (validationResults.isEmpty() && viewModel.validationResult.value.isNotEmpty()) {
                    Text(
                        text = viewModel.validationResult.value,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(Dimensions.PADDING_SHORT)
                    )
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
                                    text = stringResource(
                                        id = R.string.extracted_name,
                                        result.name ?: ""
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.extracted_value,
                                        result.value ?: ""
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.extracted_date,
                                        result.date ?: ""
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = result.bodySnippet,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = stringResource(id = R.string.not_extracted),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(Dimensions.PADDING_SHORT))
                                Text(
                                    text = result.bodySnippet,
                                    color = MaterialTheme.colorScheme.onSurface,
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
                            text = stringResource(id = R.string.sms_found, validationResults.size),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(
                                id = R.string.sms_matched,
                                validationResults.count { it.matched }),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimensions.PADDING_SHORT))

                OutlinedButton(onClick = { showPopup.value = false }, modifier = Modifier.align(Alignment.End)) {
                    Text(text = stringResource(id = R.string.validation_close), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
private fun AIDialog(viewModel: SmsViewModel){
    val showSms by viewModel.showSmsDialog
    val aiFaile by   viewModel.aiFaile
    val examples = remember { viewModel.smsSamples}

  if (showSms && examples.isNotEmpty()) {
      AlertDialog(
          onDismissRequest = { viewModel.showSmsDialog.value = false },
          title = { Text(text = stringResource(R.string.select_example_sms), color = MaterialTheme.colorScheme.onSurface) },
          text = {
              Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                  examples.forEachIndexed { index, pair ->
                      CheckBoxField(
                          title = pair.first,
                          value = pair.second,
                          callback = { viewModel.smsSamples[index] = pair.copy(second = it) }
                      )
                  }
              }
          },
          confirmButton = {
              TextButton(onClick = {
                  viewModel.showSmsDialog.value = false
                  viewModel.generateRegexWithAI()
              }) {
                  Text(stringResource(R.string.generate))
              }
          },
          dismissButton = {
              TextButton(onClick = { viewModel.showSmsDialog.value = false }) {
                  Text(stringResource(R.string.cancel))
              }
          }
      )
  }

    if((showSms && examples.isEmpty()) || aiFaile){
        AlertDialogOkCancel (
            title = R.string.no_data,
            confirmNameButton = R.string.ok,
            onDismiss = {
                viewModel.showSmsDialog.value = false
                viewModel.aiFaile.value = false
            },
            onClick = {
                viewModel.showSmsDialog.value = false
                viewModel.aiFaile.value = false
            }
        )
    }
}

@Composable
private fun Buttons(clean:()->Unit, save:()->Unit){
    Column {
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = co.com.japl.ui.R.string.clear
        ) {
            clean.invoke()
        }

        FloatButton(
            imageVector = Icons.Rounded.Save,
            descriptionIcon = R.string.save
        ) {
            save.invoke()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun SmsPreview(){
    MaterialThemeComposeUI {
        Sms(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun SmsPreviewLight(){
    MaterialThemeComposeUI {
        Sms(getViewModel())
    }
}
@Composable
private fun getViewModel():SmsViewModel{
    val viewModel = SmsViewModel(null,null,null,null)
    viewModel.load.value = false
    return viewModel
}
