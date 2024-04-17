package co.japl.android.myapplication.finanzas.view.checkpaids.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.japl.android.graphs.utils.NumbersUtil
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.paids.PeriodCheckPaymentViewModel
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CheckPaids(viewModel:PeriodCheckPaymentViewModel) {
    val loaderStatus = remember { viewModel.loader }
    val progressState = remember { viewModel.progression }

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if (loaderStatus.value) {
        LinearProgressIndicator( progress = progressState.value, modifier = Modifier.fillMaxWidth())
    } else if(viewModel.map.isNotEmpty()) {
        Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            Header(viewModel = viewModel)
            Body(viewModel = viewModel)
        }
    }else{
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(id = R.string.not_data),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
private fun Body(viewModel: PeriodCheckPaymentViewModel ){
    val listItems = remember { viewModel.map }
    val keys = listItems.keys.sortedByDescending {it}
    LazyColumn  {
        items(keys.size) { item ->
            val key = keys[item]
            listItems[key]?.let {
                Year(year = key, list = it)
            }
        }
    }
}

@Composable
private fun Header(viewModel: PeriodCheckPaymentViewModel){
    val countState = remember {  mutableStateOf(viewModel.map.values.flatten().sumOf{it.count}.toString())}
    val totalState = remember { mutableStateOf( NumbersUtil.toString(viewModel.map.values.flatten().sumOf{it.paid}))}

    Row (modifier = Modifier.padding(bottom=Dimensions.PADDING_SHORT)) {
         FieldView(name = R.string.count, value = countState.value, modifier = Weight1f())

         FieldView(name = R.string.total_paids, value = totalState.value, modifier = Weight1f())
    }
}

@Composable
private fun Year(year:Int, list:List<PeriodCheckPaymentDTO>){
    val colapseState = remember {mutableStateOf(false)}
    val countState = remember {mutableStateOf(list.sumOf { it.count })}
    val totalState = remember {mutableStateOf( NumbersUtil.toString(list.sumOf { it.paid }))}
    Surface (modifier = Modifier.padding(bottom=Dimensions.PADDING_SHORT)){

        Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
            Row (modifier = Modifier.clickable {
                colapseState.value = colapseState.value.not()
            }) {
                Text(text = year.toString(),modifier= Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(start = Dimensions.PADDING_SHORT))

                FieldView(
                    name = R.string.count,
                    value = countState.value.toString(),
                    isMoney = false,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    onClick = {colapseState.value = colapseState.value.not()}
                )

                FieldView(
                    name = R.string.total,
                    value = totalState.value.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    onClick = {colapseState.value = colapseState.value.not()}
                )
            }

            if (colapseState.value) {
                list.sortedByDescending { it.period.monthValue }.forEach {
                    Items(item = it)
                }
            }
        }
    }

}
@Composable
private fun Items(item:PeriodCheckPaymentDTO){
    val monthState = remember{ mutableStateOf(item.period.month.getDisplayName( TextStyle.FULL, Locale("es","CO"))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }) }
    val numPaidsState = remember { mutableStateOf(item.count.toString()) }
    val totalPaidState = remember { mutableStateOf(NumbersUtil.COPtoString(item.paid)) }

    Card (modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
        Column (modifier=Modifier.padding(Dimensions.PADDING_SHORT)){
            Row {
                Text(text = monthState.value, modifier = Modifier.align(alignment = Alignment.CenterVertically))

                FieldViewCards(name = R.string.num_paids, value = numPaidsState.value, modifier = Modifier.weight(2f))
            }

            FieldViewCards(name = R.string.total_paid, value = totalPaidState.value, modifier=Modifier)

        }

    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun CheckPaidsPreview(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CheckPaids(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CheckPaidsPreviewDark() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CheckPaids(viewModel = viewModel)
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CheckPaidsPreviewDarkWithoutdata(){
        val viewModel = getViewModel()
        viewModel.map.clear()
        MaterialThemeComposeUI {
            CheckPaids(viewModel = viewModel)
        }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun CheckPaidsPreviewWithoutdata(){
    val viewModel = getViewModel()
    viewModel.map.clear()
    MaterialThemeComposeUI {
        CheckPaids(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.FOLDABLE)
internal fun CheckPaidsPreview2(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CheckPaids(viewModel = viewModel)
    }
}

@Composable
private fun getViewModel():PeriodCheckPaymentViewModel{
    val viewModel =  PeriodCheckPaymentViewModel(null)
    viewModel.loader.value = false
    viewModel.map.put(2024, arrayListOf(
        PeriodCheckPaymentDTO(
            period = YearMonth.parse("202404", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 10,
            amount = 10000.0,
            paid = 100.0
        ), PeriodCheckPaymentDTO(
            period = YearMonth.parse("202403", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 9,
            amount = 10000.0,
            paid = 1000.0
        ), PeriodCheckPaymentDTO(
            period = YearMonth.parse("202402", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 8,
            amount = 10000.0,
            paid = 10000.0
        ), PeriodCheckPaymentDTO(
                period = YearMonth.parse("202401", DateTimeFormatter.ofPattern("yyyyMM")),
        count = 5,
        amount = 10000.0,
        paid = 5000.0
    )
    ))
    viewModel.map.put(2023, arrayListOf(
        PeriodCheckPaymentDTO(
            period = YearMonth.parse("202312", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 10,
            amount = 10000.0,
            paid = 100.0
        ), PeriodCheckPaymentDTO(
            period = YearMonth.parse("202311", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 9,
            amount = 10000.0,
            paid = 1000.0
        ), PeriodCheckPaymentDTO(
            period = YearMonth.parse("202310", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 8,
            amount = 10000.0,
            paid = 10000.0
        ), PeriodCheckPaymentDTO(
            period = YearMonth.parse("202309", DateTimeFormatter.ofPattern("yyyyMM")),
            count = 5,
            amount = 10000.0,
            paid = 5000.0
        )
    ))
    return viewModel
}