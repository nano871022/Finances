package co.com.japl.ui.components

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import co.japl.android.graphs.interfaces.IGraph
import co.japl.android.graphs.pieceofpie.PieceOfPie

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PiecePieGraph(list:List<Pair<String,Double>>) {
    val context = LocalContext.current
    val piecePie : IGraph = PieceOfPie(context)
    piecePie.clear()
    var invalidations by remember { mutableIntStateOf(0) }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
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

            for (values in list) {
                piecePie.addPiece(title = values.first, value = values.second)
            }

            piecePie.drawing(this.drawContext.canvas.nativeCanvas)
        }
    }
}