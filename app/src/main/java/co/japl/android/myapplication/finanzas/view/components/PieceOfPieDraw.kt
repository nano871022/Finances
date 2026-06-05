package co.japl.android.myapplication.finanzas.view.components

import androidx.compose.runtime.Composable
import co.com.japl.finances.iports.dtos.RecapDTO
import co.com.japl.ui.components.PiecePieGraph

@Composable
fun PieceOfPieDraw(recap: RecapDTO?) {
    recap?.let {
        val list = listOf(
            "Inputs" to it.totalInputs,
            "Paid" to it.totalPaid,
            "Credits" to it.totalQuoteCredit,
            "Credit Card" to it.totalQuoteCreditCard
        )
        PiecePieGraph(list = list)
    }
}
