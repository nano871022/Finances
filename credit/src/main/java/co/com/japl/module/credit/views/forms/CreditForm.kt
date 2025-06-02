package co.com.japl.module.credit.views.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.forms.CreditFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CreditForm(viewModel: CreditFormViewModel = viewModel()) {
    val progress = remember { viewModel.progress }
    val showProgress = remember { viewModel.showProgress }

    if (showProgress.value) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        Body(viewModel)
    }

}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: CreditFormViewModel) {
    val snackbarHostState = remember { viewModel.snackbarHostState }
    Scaffold(
        floatingActionButton = {
            FloatButton(
                backView = {viewModel.backView()},
                clean = {viewModel.clean()},
                amortization = {viewModel.amortization()},
                save = {viewModel.onSubmitFormClicked()}
            )
        }, snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }) {
        Form(viewModel = viewModel, modifier = Modifier.padding(it))
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Form(viewModel: CreditFormViewModel, modifier: Modifier) {
    val uisState by viewModel.uiState.collectAsState()
    val isErrorKindPayment = remember { viewModel.isErrorKindPayment }
    val isErrorName = remember { viewModel.isErrorName }
    val isErrorValue = remember { viewModel.isErrorValue }
    val isErrorRate = remember { viewModel.isErrorRate }
    val isErrorMonth = remember { viewModel.isErrorMonth }
    val isErrorKindRate = remember { viewModel.isErrorKindRate }
    val isErrorDate = remember { viewModel.isErrorDate }
    val kindPayment = remember { viewModel.kindPayment }
    val kindRate = remember { viewModel.kindRate }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PADDING_SHORT)
    ) {
        FieldSelect(
            title = stringResource(R.string.kind_payment),
            value = uisState.kindPayment.second,
            cleanTitle = R.string.clean_credit,
            list = kindPayment.value,
            modifier = Modifier.padding(bottom = Dimensions.PADDING_BOTTOM),
            isError = isErrorKindPayment,
            callable = { viewModel.onKindPaymentChange(it?.first, it?.second) })

        FieldDatePicker(
            title = R.string.date_credit,
            value = DateUtils.localDateToStringDate(uisState.creditDate),
            validation = viewModel::validate,
            isError = isErrorDate,
            callable = { viewModel.onCreditDateChange(DateUtils.toLocalDate(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )
        FieldText(
            title = stringResource(R.string.name_credit),
            value = uisState.name,
            clearTitle = R.string.clean_credit,
            callback = viewModel::onNameChange,
            validation = viewModel::validate,
            hasErrorState = isErrorName,
            icon= Icons.Rounded.Cancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldText(
            title = stringResource(R.string.value_credit),
            value = uisState.value,
            clearTitle = R.string.clean_credit,
            placeHolder = NumbersUtil.toString(uisState.valueAmt),
            currency = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM),
            validation = viewModel::validate,
            hasErrorState = isErrorValue,
            keyboardType = KeyboardOptions.Default.copy( keyboardType = KeyboardType.Decimal),
            icon= Icons.Rounded.Cancel,
            callback = { viewModel.onValueChange(it) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        ) {
            FieldText(
                title = stringResource(R.string.rate_credit),
                value = uisState.rate,
                placeHolder = NumbersUtil.toString(uisState.rateAmt),
                clearTitle = R.string.clean_credit,
                callback = { viewModel.onRateChange(it) },
                validation = viewModel::validate,
                hasErrorState = isErrorRate,
                formatDecimal = NumbersUtil.format8Decimal,
                icon= Icons.Rounded.Cancel,
                suffixValue = "%",
                keyboardType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(end = Dimensions.PADDING_SHORT)
            )

            FieldSelect(
                title = stringResource(R.string.kind_rate),
                value = uisState.kindRate.third?.getName()?:"",
                list = kindRate.value,
                isError = isErrorKindRate,
                modifier = Modifier
                    .padding(bottom = Dimensions.PADDING_BOTTOM)
                    .weight(1f),
                callable = {
                    viewModel.onKindRateChange(it?.first, it?.second)
                })
        }

        FieldText(
            title = stringResource(R.string.periods_credit),
            value = if(uisState.month != 0)uisState.month.toString() else "",
            placeHolder = uisState.month.toString(),
            clearTitle = R.string.clean_credit,
            callback = { viewModel.onMonthChange(it) },
            validation = viewModel::validate,
            decimal = true,
            hasErrorState = isErrorMonth,
            icon= Icons.Rounded.Cancel,
            keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldView(
            name = R.string.quote_credit,
            value = NumbersUtil.toString(uisState.quoteCredit),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )
    }

}

@Composable
private fun FloatButton(save:()->Unit,clean:()->Unit,amortization:()->Unit,backView:()->Unit) {
    Column {
        FloatButton(
            imageVector = Icons.Rounded.ArrowBackIosNew,
            descriptionIcon = R.string.back_view
        ) {
            backView()
        }
        FloatButton(
            imageVector = Icons.Rounded.TableChart,
            descriptionIcon = R.string.amortization
        ) {
            amortization()
        }
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = R.string.clean_credit
        ) {
            clean()
        }
        FloatButton(
            imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.add_credit
        ) {
            save()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0)
@Composable
fun PreviewNight() {
    val viewModel = creditViewModel()
    viewModel.showProgress.value = false
    MaterialThemeComposeUI() {
        CreditForm(viewModel = viewModel)
    }
}

fun creditViewModel(): CreditFormViewModel {
    return CreditFormViewModel(null, null,null, null)
}