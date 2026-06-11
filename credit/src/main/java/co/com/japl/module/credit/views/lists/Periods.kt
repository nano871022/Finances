package co.com.japl.module.credit.views.lists

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.list.PeriodsViewModel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.LoadingProgress
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
    val viewState = remember {viewModel.loading}
    LoadingProgress(
        message = R.string.load_data,
        showProgress = viewState,
        execute = viewModel::execute,
        validateData = viewModel.records::isNotEmpty,
        messageNoData = R.string.no_data
    ) { Body(viewModel)}
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
            border= BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
            Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT).clickable{ showState.value = !showState.value }){
                Text(
                    text = record.first.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    FieldView(
                        name = R.string.num_credits,
                        value = record.second.sumOf { it.count }.toString(),
                        isMoney = false,
                        modifier = Modifier.weight(1f))

                    FieldView(
                        name = R.string.value_paid,
                        value = NumbersUtil.COPtoString(record.second.sumOf { it.value }),
                        modifier = Modifier.weight(3f))
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
    Row(modifier=Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT).border(1.dp,MaterialTheme.colorScheme.onBackground,shape= ShapeDefaults.Medium)){
        val sdf = SimpleDateFormat("MMMM", Locale("es"))
        Text(text= value.count.toString(), modifier=Modifier.weight(1f).padding(top= Dimensions.PADDING_SHORT, bottom = Dimensions.PADDING_SHORT, start = Dimensions.PADDING_SHORT))

        Text(text= sdf.format(Date(value.date.year,value.date.monthValue-1,1)).uppercase(),
            modifier=Modifier.weight(4f).padding(top= Dimensions.PADDING_SHORT, bottom = Dimensions.PADDING_SHORT))
        Text(text= NumbersUtil.COPtoString(value.value),
            textAlign = TextAlign.Right,
            modifier=Modifier.weight(4f).padding(top= Dimensions.PADDING_SHORT, bottom = Dimensions.PADDING_SHORT, end= Dimensions.PADDING_SHORT))
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
internal fun previewLight(){
    val vw = getViewModel()
    vw.loading.value = false
    MaterialThemeComposeUI {
        Periods(vw)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = false,  backgroundColor = 0x00000, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun previewNoDark(){
    val vw = getViewModel()
    vw.loading.value = false
    vw.records.clear()
    MaterialThemeComposeUI {
        Periods(vw)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = false, backgroundColor = 0x0000, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
