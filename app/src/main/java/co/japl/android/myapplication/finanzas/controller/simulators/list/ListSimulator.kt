package co.japl.android.myapplication.finanzas.controller.simulators.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.module.credit.views.simulator.SimulatorList as SimulatorListCredit
import co.com.japl.module.creditcard.views.simulator.SimulatorList as SimulatorListCreditCard

@Composable
fun ListSimulator(viewModel: ListSimulatorViewModel, navController: NavController?) {
    val progress = remember { viewModel.progress }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    if (progress.value) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else {
        if (viewModel.list.isEmpty()) {
            Text(
                text = stringResource(co.com.japl.ui.R.string.no_records),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            BodyList(viewModel, navController)
        }
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
