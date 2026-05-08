package co.com.japl.module.creditcard.views.email.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EmailForm(viewModel: EmailCreditCardViewModel) {
    val load by remember { viewModel.load }
    var progress by remember { viewModel.progress }

    LaunchedEffect(key1 = viewModel) {
        viewModel.main()
    }
    if (load) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        Form(viewModel = viewModel)
    }
}

@Composable
private fun Form(viewModel: EmailCreditCardViewModel) {
    Scaffold(
        floatingActionButton = {
            Buttons({ viewModel.clean() }, { viewModel.save() })
        }
    ) {
        Body(
            viewModel = viewModel, modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_SHORT)
        )
    }
}

@Composable
private fun Body(viewModel: EmailCreditCardViewModel, modifier: Modifier) {
    val listCreditCard = remember { viewModel.creditCardList }
    val creditCard = remember { viewModel.creditCard }
    val errorCreditCard = remember { viewModel.errorCreditCard }
    val kindInterest = remember { viewModel.kindInterestRate }
    val kinInterestList = remember { viewModel.kindInterestRateList }
    val errorKindInterest = remember { viewModel.errorKindInterestRate }
    val sender = remember { viewModel.sender }
    val errorSender = remember { viewModel.errorSender }
    val subjectPattern = remember { viewModel.subjectPattern }
    val errorSubjectPattern = remember { viewModel.errorSubjectPattern }
    val bodyPattern = remember { viewModel.bodyPattern }
    val errorBodyPattern = remember { viewModel.errorBodyPattern }
    val validationResult = remember { viewModel.validationResult }
    val stateScroll = rememberScrollState(0)

    Column(modifier = modifier.padding(Dimensions.PADDING_SHORT).verticalScroll(stateScroll)) {
        FieldSelect(title = stringResource(id = R.string.credit_card),
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

        FieldSelect(title = stringResource(id = R.string.kind_rate),
            value = kindInterest.value?.second ?: "",
            list = kinInterestList,
            isError = errorKindInterest,
            modifier = modifier,
            callAble = {
                it?.let {
                    kindInterest.value = it
                    viewModel.validate()
                }
            })

        FieldText(title = stringResource(id = R.string.email_sender),
            value = sender.value,
            hasErrorState = errorSender,
            validation = { viewModel.validate() },
            icon = Icons.Rounded.Cancel,
            callback = {
                sender.value = it
            },
            modifier = modifier)

        FieldText(title = stringResource(id = R.string.subject_pattern),
            value = subjectPattern.value,
            hasErrorState = errorSubjectPattern,
            validation = { viewModel.validate() },
            icon = Icons.Rounded.Cancel,
            callback = {
                subjectPattern.value = it
            },
            modifier = modifier)

        FieldText(title = stringResource(id = R.string.body_pattern),
            value = bodyPattern.value,
            hasErrorState = errorBodyPattern,
            validation = { viewModel.validate() },
            icon = Icons.Rounded.Cancel,
            lines = 3,
            callback = {
                bodyPattern.value = it
            },
            modifier = modifier)


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

        FieldText(
            title = stringResource(id = R.string.validate),
            value = validationResult.value,
            lines = 6,
            icon = Icons.Rounded.Cancel,
            callback = {
                validationResult.value = it
            },
            readOnly = true,
            modifier = modifier
        )
    }
}

@Composable
private fun Buttons(clean: () -> Unit, save: () -> Unit) {
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
