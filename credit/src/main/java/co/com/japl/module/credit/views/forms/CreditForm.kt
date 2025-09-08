package co.com.japl.module.credit.views.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.forms.CreditFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.DateUtils
import co.com.japl.utils.NumbersUtil
import java.math.BigDecimal

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CreditForm(viewModel: CreditFormViewModel, navController: NavController) {
    val progress = remember { viewModel.progress }
    val showProgress = remember { viewModel.showProgress }

    if (showProgress.value) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        Body(viewModel, navController)
    }

}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: CreditFormViewModel, navController: NavController) {
    val snackbarHostState = remember { viewModel.snackbarHostState }
    Scaffold(
        floatingActionButton = {
            FloatButton(
                backView = {viewModel.backView(navController)},
                clean = {viewModel.clean()},
                amortization = {viewModel.amortization(navController)},
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
    val kindPayment = viewModel.kindPayment.value.collectAsState()
    val creditDate = viewModel.creditDate.value.collectAsState()
    val name = viewModel.name.value.collectAsState()
    val value = viewModel.value.value.collectAsState()
    val rate = viewModel.rate.value.collectAsState()
    val kindRate = viewModel.kindRate.value.collectAsState()
    val month = viewModel.month.value.collectAsState()
    val quoteCredit = viewModel.quoteCredit.value.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PADDING_SHORT)
    ) {
        FieldSelect(
            title = stringResource(R.string.kind_payment),
            value = kindPayment.value?.second?:"",
            cleanTitle = R.string.clean_credit,
            list = viewModel.kindPayment.list.map{Pair (it?.first?:0,it?.second?:"")},
            modifier = Modifier.padding(bottom = Dimensions.PADDING_BOTTOM),
            isError = viewModel.kindPayment.error,
            callable = { viewModel.kindPayment.onValueChange(Triple(it?.first?:0, it?.second?:"",viewModel.kindPayment.list?.find{ its -> its?.first!! == it?.first?:-1}?.third?: KindPaymentsEnums.MONTHLY))})


        FieldDatePicker(
            title = R.string.date_credit,
            value = DateUtils.localDateToStringDate(creditDate.value),
            validation = viewModel::validate,
            isError = viewModel.creditDate.error,
            callable = { viewModel.creditDate.onValueChange(DateUtils.toLocalDate(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )
        FieldText(
            title = stringResource(R.string.name_credit),
            value = name.value,
            clearTitle = R.string.clean_credit,
            callback = viewModel.name::onValueChange,
            validation = viewModel::validate,
            hasErrorState = viewModel.name.error,
            icon= Icons.Rounded.Cancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldText(
            title = stringResource(R.string.value_credit),
            value = value.value,
            clearTitle = R.string.clean_credit,
            placeHolder = viewModel.value.valueStr,
            currency = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM),
            validation = viewModel::validate,
            hasErrorState = viewModel.value.error.value,
            keyboardType = KeyboardOptions.Default.copy( keyboardType = KeyboardType.Decimal),
            icon= Icons.Rounded.Cancel,
            callback = { viewModel.value.onValueChange(it) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        ) {
            FieldText(
                title = stringResource(R.string.rate_credit),
                value = rate.value,
                placeHolder = viewModel.rate.valueStr,
                clearTitle = R.string.clean_credit,
                callback = { viewModel.rate.onValueChange(it) },
                validation = viewModel::validate,
                hasErrorState = viewModel.rate.error.value,
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
                value = kindRate.value?.third?.getName()?:"",
                list = viewModel.kindRate.list.map { Pair(it?.first?:0,it?.second?:"")  },
                isError = viewModel.kindRate.error,
                modifier = Modifier
                    .padding(bottom = Dimensions.PADDING_BOTTOM)
                    .weight(1f),
                callable = {
                    viewModel.kindRate.onValueChange(Triple(it?.first?:0, it?.second?:"",viewModel.kindRate.list?.first { its->its?.first!! == it?.first?:-1}?.third?: KindOfTaxEnum.ANUAL_EFFECTIVE))
                })
        }

        FieldText(
            title = stringResource(R.string.periods_credit),
            value = month.value,
            placeHolder = viewModel.month.valueStr,
            clearTitle = R.string.clean_credit,
            callback = { viewModel.month.onValueChange(it) },
            validation = viewModel::validate,
            decimal = true,
            hasErrorState = viewModel.month.error.value,
            icon= Icons.Rounded.Cancel,
            keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PADDING_BOTTOM)
        )

        FieldView(
            name = R.string.quote_credit,
            value = quoteCredit.value,
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
    MaterialThemeComposeUI {
        CreditForm(viewModel = viewModel, navController = NavController(LocalContext.current))
    }
}

@Composable
private fun creditViewModel(): CreditFormViewModel {
    val context = LocalContext.current
    val savedStateHandle = SavedStateHandle()
    val creditSvc = FakeCreditFormSvc()
    return CreditFormViewModel(savedStateHandle, creditSvc, context)
}
