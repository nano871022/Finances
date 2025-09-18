package co.com.japl.module.paid.views.monthly.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.monthly.list.MonthlyViewModel
import co.com.japl.module.paid.views.accounts.form.fakes.FakeAccountPort
import co.com.japl.module.paid.views.inputs.form.fakes.FakeInputPort
import co.com.japl.module.paid.views.monthly.list.fakes.FakeSmsPort
import co.com.japl.module.paid.views.paid.form.fakes.FakePaidPort
import co.com.japl.module.paid.views.paid.list.fakes.FakePrefs
import co.com.japl.module.paid.views.sms.form.fakes.FakeSMSPaidPort
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.HelpWikiButton
import co.com.japl.ui.components.PiecePieGraph
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.graphs.utils.NumbersUtil

@Composable
fun Monthly(viewModel: MonthlyViewModel, navController: NavController) {
    val loaderState = remember { viewModel.loaderState }
    val progressStatus = remember { viewModel.progressStatus }

    if (loaderState.value) {
        LinearProgressIndicator(
            progress = { progressStatus.value },
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        Body(viewModel, navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Body(viewModel: MonthlyViewModel, navController: NavController) {
    val accountList = remember { viewModel.accountList }
    val accountState = remember { viewModel.accountState }
    val listGraph = remember { viewModel.listGraph }

    Scaffold(
        floatingActionButton = {
            if (accountState.value != null) {
                Buttons(
                    gotoList = { viewModel.goToListDetail(navController) },
                    gotoPeriod = { viewModel.goToListPeriod(navController) },
                    gotoCreate = { viewModel.goToListCreate(navController) })
            }
        },
        modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                FieldSelect(
                    title = stringResource(id = R.string.account),
                    value = accountState.value?.name ?: "",
                    list = accountList,
                    modifier = Modifier
                        .padding(bottom = Dimensions.PADDING_SHORT)
                        .weight(1f),
                    callAble = { pair ->
                        pair?.let {
                            viewModel.listAccount?.first { it.id == pair.first }?.let {
                                accountState.value = it
                                viewModel.loaderState.value = true
                            }
                        } ?: accountState.let { it.value = null }
                    })
                HelpWikiButton(
                    wikiUrl = R.string.wiki_paid_url,
                    descriptionContent = R.string.wiki_paid_description
                )
            }

            if (accountState.value != null) {
                Accounts(viewModel = viewModel)
                PiecePieGraph(list = listGraph)
            }
        }
    }
}

@Composable
private fun Buttons(gotoList: () -> Unit, gotoPeriod: () -> Unit, gotoCreate: () -> Unit) {
    Column {
        FloatButton(
            imageVector = Icons.Rounded.RemoveRedEye,
            descriptionIcon = R.string.go_to_list,
            onClick = gotoList
        )
        FloatButton(
            imageVector = Icons.Rounded.CalendarMonth,
            descriptionIcon = R.string.go_to_periods,
            onClick = gotoPeriod
        )
        FloatButton(
            imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.go_to_add,
            onClick = gotoCreate
        )
    }
}

@Composable
private fun Accounts(viewModel: MonthlyViewModel) {
    val periodState = remember { viewModel.periodState }
    val countState = remember { viewModel.countState }
    val paidTotalState = remember { viewModel.paidTotalState }
    val incomesTotalState = remember { viewModel.incomesTotalState }

    Row(modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
        FieldView(
            name = stringResource(id = R.string.period),
            value = periodState.value.replaceFirstChar { it.titlecase() },
            isMoney = false,
            alignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(end = Dimensions.PADDING_SHORT)
        )
        FieldView(
            name = stringResource(id = R.string.count),
            value = countState.value.toString(),
            isMoney = false,
            modifier = Modifier.weight(1f)
        )
    }
    FieldView(
        name = stringResource(id = R.string.paid_total),
        value = NumbersUtil.toString(paidTotalState.value),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT)
    )
    Row(
        modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)
    ) {
        FieldView(
            name = stringResource(id = R.string.incomes_total),
            value = NumbersUtil.toString(incomesTotalState.value),
            modifier = Modifier
                .weight(1f)
                .padding(end = Dimensions.PADDING_SHORT)
        )
        FieldView(
            name = stringResource(id = R.string.incomes_less_paids),
            value = NumbersUtil.toString(incomesTotalState.value - paidTotalState.value),
            color = if ((incomesTotalState.value - paidTotalState.value) > 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MonthlyPreview() {
    val paidSvc: IPaidPort = FakePaidPort()
    val incomesSvc: IInputPort = FakeInputPort()
    val accountSvc: IAccountPort = FakeAccountPort()
    val smsSvc: ISMSPaidPort = FakeSMSPaidPort()
    val paidSmsSvc: ISmsPort = FakeSmsPort()
    val prefs: Prefs = FakePrefs()
    MaterialThemeComposeUI {
        Monthly(
            viewModel(
                factory = MonthlyViewModel.Companion.create(
                    extras = viewModel(),
                    paidSvc = paidSvc,
                    incomesSvc = incomesSvc,
                    accountSvc = accountSvc,
                    smsSvc = smsSvc,
                    paidSmsSvc = paidSmsSvc,
                    prefs = prefs
                )
            ),
            navController = rememberNavController()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MonthlyPreviewDark() {
    val paidSvc: IPaidPort = FakePaidPort()
    val incomesSvc: IInputPort = FakeInputPort()
    val accountSvc: IAccountPort = FakeAccountPort()
    val smsSvc: ISMSPaidPort = FakeSMSPaidPort()
    val paidSmsSvc: ISmsPort = FakeSmsPort()
    val prefs: Prefs = FakePrefs()
    MaterialThemeComposeUI {
        Monthly(
            viewModel(
                factory = MonthlyViewModel.Companion.create(
                    extras = viewModel(),
                    paidSvc = paidSvc,
                    incomesSvc = incomesSvc,
                    accountSvc = accountSvc,
                    smsSvc = smsSvc,
                    paidSmsSvc = paidSmsSvc,
                    prefs = prefs
                )
            ),
            navController = rememberNavController()
        )
    }
}