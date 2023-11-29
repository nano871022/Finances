package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.RecapViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.view.components.Carousel

@Composable
fun Recap(model:RecapViewModel) {
    Carousel(size =4
        ,modifier = Modifier.height(250.dp)) {
        when (it) {
            0 -> CardRecap(title = stringResource(id = R.string.recap), model.totalInbound, model.totalPayment)
            1 -> CardProjections(
                title = stringResource(id = R.string.projections)
                , totalSaved = model.totalSaved
                , projectionValue = model.projectionsValue
            )

            2 -> CardPaymentAndCredits(
                title = stringResource(id = R.string.cash_paids_credits),
                totalInbound = model.totalInbound,
                totalPayed = model.totalPayed,
                totalCredits = model.totalCredits,
            )

            3 -> CardCredits(
                title = stringResource(id = R.string.credit_cards)
                , totalCC = model.totalCreditCard
                , warningValue = model.warningValue
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecap() {
    val model = RecapViewModel()
    co.com.japl.ui.theme.MaterialThemeComposeUI {
        Recap(model)
    }
}