package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.japl.finances.iports.dtos.RecapDTO
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.RecapViewModel
import co.japl.android.myapplication.finanzas.view.components.Carousel
import co.japl.android.myapplication.finanzas.view.components.PieceOfPieDraw

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Recap(model:RecapViewModel= viewModel()) {
    model.main()
    val currentProgress = remember { model.currentProgress }
    val loading = remember {model.loading }

    Column {
        if(loading) {
            LinearProgressIndicator(
                progress = currentProgress,
                modifier = Modifier.fillMaxWidth()
            )
        }else{
            CarouselView(model = model)

            PieceOfPieDraw(model.recap)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarouselView(model:RecapViewModel){
    Carousel(
        size = 4, modifier = Modifier.height(250.dp)
    ) {
        when (it) {
            0 -> CardRecap(
                title = stringResource(id = R.string.recap),
                model.totalInbound,
                model.totalPayed + model.totalCredits + model.totalCreditCard
            )

            1 -> CardProjections(
                title = stringResource(id = R.string.projections),
                totalSaved = model.totalSaved,
                projectionValue = model.projectionsValue
            )

            2 -> CardPaymentAndCredits(
                title = stringResource(id = R.string.cash_paids_credits),
                totalInbound = model.totalInbound,
                totalPayed = model.totalPayed,
                totalCredits = model.totalCredits,
            )

            3 -> CardCredits(
                title = stringResource(id = R.string.credit_cards),
                totalCC = model.totalCreditCard,
                warningValue = model.warningValue
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecap() {
    val model = RecapViewModel(null)
    val recap = RecapDTO(
      projectionSaved = 20000.0,
     projectionNext = 30000.0,
     totalPaid = 40000.0,
     totalQuoteCredit = 50000.0,
     totalInputs = 60000.0,
     totalQuoteCreditCard = 70000.0,
     warningValueCreditCard = 80000.0
    )
    model.setRecap(recap)
    co.com.japl.ui.theme.MaterialThemeComposeUI {
        Recap(model)
    }
}