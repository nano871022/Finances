package co.com.japl.module.credit.views

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.credit.controllers.simulator.SimulatorListViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.R



@Composable
fun ListSimulator(viewModel: SimulatorListViewModel){
    val progres = remember { viewModel.progres }

    if(progres.value){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else{
        if(viewModel.list.isEmpty()){
            Text(text= stringResource(R.string.no_records),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
        }else {
            BodyList(viewModel)
        }
    }

}

@Composable
private fun BodyList(viewModel: SimulatorListViewModel){
    val list = remember{ viewModel.list.sortedByDescending { it.code } }
    Scaffold (
        modifier = Modifier
    ){
        LazyColumn(
            state = rememberLazyListState()
            ,modifier=Modifier.padding(it)
        ) {
            items(list) {
                Body(it, viewModel)
                HorizontalDivider(modifier=Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun Body(dto: SimulatorCreditDTO,viewModel: SimulatorListViewModel) {
    if (dto.isCreditVariable.not()) {
        val viewModel = viewModel.createViewModelQuoteFix(dto)
        //SimulatorListCredit(viewModel)
    } else {
        // val viewModel = viewModel.createViewModelQuoteVariable(dto)
         //SimulatorList(viewModel) // Unresolved reference as CreditCardModule is not a dependency
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true, backgroundColor = 0x000000, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun ListSimulatorPreviewLight(){
    val viewModel = getSimulatorViewModel()
    MaterialThemeComposeUI {
        ListSimulator(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true, backgroundColor = 0x000000, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun ListSimulatorPreviewLightNoRecords(){
    val viewModel = getSimulatorViewModel()
    viewModel.list.clear()
    MaterialThemeComposeUI {
        ListSimulator(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true, backgroundColor = 0x000000, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun ListSimulatorPreviewLightProgress(){
    val viewModel = getSimulatorViewModel()
    viewModel.progres.value = true
    MaterialThemeComposeUI {
        ListSimulator(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true,  backgroundColor = 0x000000, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ListSimulatorPreviewDark(){
    val viewModel = getSimulatorViewModel()
    MaterialThemeComposeUI {
        ListSimulator(viewModel)
    }
}

@Composable
private fun getSimulatorViewModel(): SimulatorListViewModel {
    val viewModel = SimulatorListViewModel(LocalContext.current)
    viewModel.list.add(
        SimulatorCreditDTO(
            code = 1,
            name = "Name test 1",
            value = 10000000.toBigDecimal(),
            periods = 12,
            tax = 25.5,
            kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE,
            isCreditVariable = true,
            interestValue = 1000000.toBigDecimal(),
            capitalValue = 800000.toBigDecimal(),
            quoteValue = 1800000.toBigDecimal()
        )
    )
    viewModel.list.add(
        SimulatorCreditDTO(
            code = 2,
            name = "Name test 2",
            value = 10000000.toBigDecimal(),
            periods = 12,
            tax = 20.5,
            kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE,
            isCreditVariable = false,
            interestValue = 900000.toBigDecimal(),
            capitalValue = 800000.toBigDecimal(),
            quoteValue = 1700000.toBigDecimal()
        )
    )
    return viewModel
}
