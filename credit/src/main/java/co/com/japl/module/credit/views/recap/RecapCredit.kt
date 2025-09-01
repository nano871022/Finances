package co.com.japl.module.credit.views.recap

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.recap.RecapViewModel
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.graphs.interfaces.IGraph
import co.japl.android.graphs.pieceofpie.PieceOfPie
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.YearMonth
import co.com.japl.ui.components.FieldView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Recap(viewModel:RecapViewModel){
    val loader = remember { viewModel.loader }
    val progression = remember { viewModel.progress }
    if(loader.value){
        viewModel.execute()
        LinearProgressIndicator(
            progress = { progression.floatValue },
            modifier = Modifier.fillMaxWidth(),
        )
    }else{
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel:RecapViewModel){
    Scaffold(floatingActionButton = {
        Buttons(viewModel)
    }) {
        if(viewModel.listCredits.isEmpty()){
            Column {
                Text(text = stringResource(R.string.no_data),
                modifier = Modifier.fillMaxWidth())
            }
    }else{
            List(viewModel.listCredits, yearMonth = viewModel.yearMonth)
        }
    }
}

 @Composable
 private fun Buttons(viewModel:RecapViewModel){
     Column {
         if(viewModel.listCredits.isNotEmpty()) {
             FloatButton(
                 imageVector = Icons.Rounded.CalendarMonth,
                 descriptionIcon = R.string.check_periods,
                 onClick = { viewModel.periodCredits() })

             FloatButton(
                 imageVector = Icons.Rounded.RemoveRedEye,
                 descriptionIcon = R.string.detail_credits,
                 onClick = { viewModel.detailCredits() })
         }
         FloatButton(
             imageVector = Icons.Rounded.Add,
             descriptionIcon = R.string.add_credit,
             onClick = { viewModel.addCredit() })
     }
 }

@Composable
private fun List(list:List<RecapCreditDTO>,yearMonth: YearMonth){
        val months = stringArrayResource(R.array.months)
        Column(modifier = Modifier.fillMaxWidth()) {

            Row (modifier = Modifier.padding(bottom=Dimensions.PADDING_BOTTOM)) {
                FieldView(
                    name = stringResource(R.string.num_credits),
                    value = "${list.size}",
                    modifier = Modifier.weight(1f)
                        .padding(end = Dimensions.PADDING_SHORT)
                )

                FieldView(
                    name = stringResource(R.string.month_payment),
                    value = "${months[yearMonth.monthValue]} ${yearMonth.year}",
                    modifier = Modifier.weight(2f)
                        .padding(start = Dimensions.PADDING_SHORT)
                )
            }

            FieldView(
                name = stringResource(R.string.quote_value),
                value = NumbersUtil.COPtoString(list.sumOf { it.quoteValue }),
                modifier = Modifier.fillMaxWidth().padding(bottom = Dimensions.PADDING_BOTTOM)
            )

            Row (modifier = Modifier.padding(bottom=Dimensions.PADDING_BOTTOM)){
                FieldView(
                    name = stringResource(R.string.interest_value),
                    value = NumbersUtil.COPtoString(list.sumOf { it.interestValue }),
                    modifier = Modifier.fillMaxWidth().weight(2f)
                        .padding(end = Dimensions.PADDING_SHORT)
                )

                FieldView(
                    name = stringResource(R.string.capital_value),
                    value = NumbersUtil.COPtoString(list.sumOf { it.capitalValue }),
                    modifier = Modifier.fillMaxWidth().weight(2f)
                        .padding(start = Dimensions.PADDING_SHORT)
                )
            }
            Row (modifier = Modifier.padding(bottom=Dimensions.PADDING_BOTTOM)){
                FieldView(
                    name = stringResource(R.string.pending_payment),
                    value = NumbersUtil.COPtoString(list.sumOf { it.pendingPerPaid }),
                    modifier = Modifier.fillMaxWidth().weight(2f)
                        .padding(end = Dimensions.PADDING_SHORT)
                )

                FieldView(
                    name = stringResource(R.string.additional_value),
                    value = NumbersUtil.COPtoString(list.sumOf { it.additionalAmount }),
                    modifier = Modifier.fillMaxWidth().weight(2f)
                        .padding(start = Dimensions.PADDING_SHORT)
                )
            }

            GraphList(list)
        }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GraphList(list:List<RecapCreditDTO>){
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
                piecePie.addPiece(title = values.name, value = values.quoteValue.toDouble())
            }

            piecePie.drawing(this.drawContext.canvas.nativeCanvas)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0x111000)
fun RecapPreview(){
    MaterialThemeComposeUI {
        Recap(viewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RecapPreviewDark(){
    MaterialThemeComposeUI {
        Recap(viewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RecapEmptyPreviewDark(){
    val viewModel = viewModel()
    viewModel.listCredits.clear()

    MaterialThemeComposeUI {
        Recap(viewModel)
    }
}

fun viewModel():RecapViewModel{
    val viewModel = RecapViewModel(null, YearMonth.now(),null)
    viewModel.listCredits.add(
        RecapCreditDTO(
            month = 4,
            year = 2025,
            quoteValue = BigDecimal(10000),
            interestValue = BigDecimal(10000),
            capitalValue = BigDecimal(10000),
            additionalAmount = BigDecimal(1000),
            pendingPerPaid = BigDecimal(100000),
            name = "Credit1",
            id = 1
        ))
    viewModel.listCredits.add(
        RecapCreditDTO(
            month = 4,
            year = 2024,
            quoteValue = BigDecimal(40000),
            interestValue = BigDecimal(40000),
            capitalValue = BigDecimal(50000),
            additionalAmount = BigDecimal(6000),
            pendingPerPaid = BigDecimal(300000),
            name = "Credit2",
            id = 2
        ))
    viewModel.loader.value = false
    return viewModel
}