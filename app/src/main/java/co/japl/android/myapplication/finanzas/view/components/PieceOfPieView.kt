package co.japl.android.myapplication.finanzas.view.components

import android.view.MotionEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.RecapDTO
import co.japl.android.graphs.interfaces.IGraph
import co.japl.android.graphs.pieceofpie.PieceOfPie
import co.japl.android.myapplication.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PieceOfPieDraw(recap: RecapDTO?){
    val context = LocalContext.current
    val paidsText = stringResource(id = R.string.total_paids)
    val creditQuoteText = stringResource(id = R.string.total_credits)
    val creditCardQuoteText = stringResource(id = R.string.total_quote_credit_card)
    val piecePie : IGraph = PieceOfPie(context)
    var invalidations by remember { mutableIntStateOf(0) }
    Surface(modifier=Modifier.padding(10.dp).fillMaxWidth()
    ,border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
        , shape = MaterialTheme.shapes.small
    ) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .pointerInteropFilter {
                when (it.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        piecePie.validateTouch(it.x, it.y)
                        invalidations++
                        true
                    }

                    else -> false
                }
            }) {
            invalidations.let {
                piecePie.drawBackground()
                piecePie.addPiece(title = paidsText, value = recap?.totalPaid ?: 0.0)
                piecePie.addPiece(title = creditQuoteText, value = recap?.totalQuoteCredit ?: 0.0)
                piecePie.addPiece(
                    title = creditCardQuoteText,
                    value = recap?.totalQuoteCreditCard ?: 0.0
                )
                piecePie.drawing(this.drawContext.canvas.nativeCanvas)
            }
        }
    }
}