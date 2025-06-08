package co.com.japl.module.credit.views.lists

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import co.com.japl.module.credit.controllers.list.PeriodsViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.FormUIState
import co.japl.android.graphs.utils.NumbersUtil
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.Date
import java.util.Locale

@Composable
fun Periods(viewModel: PeriodsViewModel){
    val viewState = viewModel.viewState.collectAsState()

    if(viewState.value == FormUIState.Loading){
        LinearProgressIndicator( modifier = Modifier.fillMaxWidth())
    }else{
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel: PeriodsViewModel){
    val records = remember { viewModel.records }

    LazyColumn (modifier=Modifier.fillMaxWidth()){
        items(records.groupBy { it.date.year }.toList()){
            RowYearly(it)
        }
    }
}

@Composable
private fun RowYearly(record: Pair<Int,List<PeriodCreditDTO>>){
    val showState = remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth()) {
        Surface (
            shape = ShapeDefaults.Medium,
            modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
            Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
                Row(modifier = Modifier.fillMaxWidth().clickable{ showState.value = !showState.value }) {
                    Text(text = record.second.sumOf { it.count }.toString(),
                        modifier = Modifier.weight(1f))
                    Text(
                        text = record.first.toString(),
                        modifier = Modifier.weight(4f)
                    )
                    Text(text = NumbersUtil.COPtoString(record.second.sumOf { it.value }),
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(4f))
                }
                if(showState.value){
                HorizontalDivider(modifier = Modifier.padding(Dimensions.PADDING_SHORT))
                record.second.forEach{
                    RowMonthly(it)
                }
                    }
            }
        }
    }
}


@Composable
private fun RowMonthly(value: PeriodCreditDTO){
    Row(modifier=Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)){
        val sdf = SimpleDateFormat("MMMM", Locale("es"))
        Text(text= value.count.toString(), modifier=Modifier.weight(1f))

        Text(text= sdf.format(Date(value.date.year,value.date.monthValue-1,1)).uppercase(),
            modifier=Modifier.weight(4f))
        Text(text= NumbersUtil.COPtoString(value.value),
            textAlign = TextAlign.Right,
            modifier=Modifier.weight(4f))
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
internal fun previewLight(){
    val vw = getViewModel()
    MaterialThemeComposeUI {
        Periods(vw)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = false, showSystemUi = true,  backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun previewDark(){
    val vw = getViewModel()
    MaterialThemeComposeUI {
        Periods(vw)
    }
}

private fun getViewModel(): PeriodsViewModel{
    val vw = PeriodsViewModel(null)
    vw.records.add(PeriodCreditDTO(YearMonth.of(2024,10),1, BigDecimal.TEN))
    vw.records.add(PeriodCreditDTO(YearMonth.of(2024,11),1, BigDecimal.TEN))
    vw.records.add(PeriodCreditDTO(YearMonth.of(2024,12),1, BigDecimal.TEN))
    vw.records.add(PeriodCreditDTO(YearMonth.of(2025,1),1, BigDecimal.TEN))
    vw.records.add(PeriodCreditDTO(YearMonth.of(2025,2),2, BigDecimal(500)))
    vw.records.add(PeriodCreditDTO(YearMonth.of(2025,3),2, BigDecimal(1000)))
    return vw
}
