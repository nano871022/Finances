package co.japl.android.myapplication.finanzas.view.simulators

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.japl.android.myapplication.R
import co.com.japl.ui.components.LoadingProgress
import co.com.japl.ui.components.OnResumeEffect
import co.japl.android.myapplication.finanzas.controller.simulators.list.ListSimulatorViewModel
import co.com.japl.module.credit.views.simulator.SimulatorList as SimulatorListCredit
import co.com.japl.module.creditcard.views.simulator.SimulatorList as SimulatorListCreditCard

@Composable
fun ListSimulator(viewModel: ListSimulatorViewModel, navController: NavController?) {
    val progress = remember { viewModel.progress }
    val context = LocalContext.current

    OnResumeEffect {
        viewModel.load()
    }

    LoadingProgress(
        message = R.string.loading_data,
        showProgress = progress,
        validateData = { viewModel.list.isNotEmpty() },
        messageNoData = R.string.no_records
    ) {
        BodyList(viewModel, navController)
    }
}

@Composable
private fun BodyList(viewModel: ListSimulatorViewModel, navController: NavController?) {
    val list = remember { viewModel.list.sortedByDescending { it.code } }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.padding(it)
        ) {
            items(list) { dto ->
                Body(dto, viewModel, navController)
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun Body(dto: SimulatorCreditDTO, viewModel: ListSimulatorViewModel, navController: NavController?) {
    val context = LocalContext.current
    if (dto.isCreditVariable.not()) {
        val vm = remember(dto.code) { viewModel.createViewModelQuoteFix(context, dto, navController) }
        SimulatorListCredit(vm)
    } else {
        val vm = remember(dto.code) { viewModel.createViewModelQuoteVariable(context, dto, navController) }
        SimulatorListCreditCard(vm)
    }
}
