package co.com.japl.module.paid.views.periods.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.period.list.PeriodsViewModel
import co.com.japl.module.paid.enums.PeriodListOptions
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Period(viewModel:PeriodsViewModel){
    val progressState = remember {
        viewModel.progress
    }
    val loaderState = remember {
        viewModel.loader
    }

    CoroutineScope(Dispatchers.IO).launch{
        viewModel.main()
    }

    if(loaderState.value){
        LinearProgressIndicator(
            progress = { progressState.value },
            modifier = Modifier.fillMaxWidth(),
        )
    }else{
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel: PeriodsViewModel){
    val listState = remember {
        viewModel.list
    }


    Column (modifier=Modifier.padding(Dimensions.PADDING_SHORT)){

        Header(count = listState.values.flatMap { it }.sumOf { it.count },
            value =  listState.values.flatMap { it }.sumOf { it.value })

        Year(listState = listState, goTo = {viewModel.goToListDetail(it)})
    }
}

@Composable
private fun Year(listState: SnapshotStateMap<Int, List<PeriodPaidDTO>>,goTo: (YearMonth) -> Unit){
    LazyColumn (modifier=Modifier.padding(Dimensions.PADDING_SHORT)){
        items(listState.keys.sortedByDescending { it }.size) { item ->
            val key = listState.keys.sortedByDescending { it }.filterIndexed { index, i -> index == item }.first()
            listState[key]?.let {
                BodyYear(key = key, list = it, goTo = goTo)
            }
        }
    }
}

@Composable
private fun BodyYear(key:Int,list:List<PeriodPaidDTO>,goTo: (YearMonth) -> Unit){
    val dropDownState = remember {
        mutableStateOf(false)
    }
    Surface(modifier = Modifier
        .padding(bottom = Dimensions.PADDING_SHORT)
        .clickable { dropDownState.value = !dropDownState.value },

        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            HeaderYearRecap(year = key,
                count = list.sumOf { it.count },
                value = list.sumOf { it.value }) {
                dropDownState.value = !dropDownState.value
            }
            if (dropDownState.value) {
                BodyYearRecap(list = list,goTo = goTo)
            }
        }
    }
}

@Composable
private fun Header(count: Int, value: Double) {
    Row (modifier=Modifier.padding(bottom = Dimensions.PADDING_SHORT)){
        FieldView(name = R.string.count,
            value = count.toString(),
            isMoney = false,
            modifier = Weight1f())

        FieldView(name = R.string.value,
            value = NumbersUtil.toString(value),
            modifier = Weight1f())
    }
}

@Composable
private fun BodyYearRecap(list:List<PeriodPaidDTO>,goTo: (YearMonth) -> Unit){
    Column{
        list.forEach{
            Item(it,goTo)
        }
    }
}

@Composable
private fun Item(item:PeriodPaidDTO,goTo:(YearMonth)->Unit){
    val optionsState = remember {
        mutableStateOf(false)
    }
    Card (modifier = Modifier.padding(bottom=Dimensions.PADDING_SHORT)) {
        Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            Row {
                Text(
                    text = item.date.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM",
                            Locale("es", "CO")
                        )
                    ).replaceFirstChar { it.titlecase() },
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                FieldViewCards(
                    name = R.string.count,
                    value = item.count.toString(),
                    modifier = Weight1f().align(alignment = Alignment.CenterVertically)
                )

                IconButton(imageVector = Icons.Rounded.MoreVert,
                    descriptionContent = co.com.japl.ui.R.string.see_more,
                    onClick = {
                        optionsState.value = true
                    })
            }
            FieldViewCards(
                name = R.string.value,
                value = NumbersUtil.COPtoString(item.value),
                modifier = Modifier
            )
        }
    }

    if (optionsState.value) {
        MoreOptionsDialog(listOptions =PeriodListOptions.values().toList(),
            onDismiss = { optionsState.value = false }) {
            when(it){
                PeriodListOptions.SEE->{
                    goTo(item.date)
                }
            }
            optionsState.value = false
        }
    }
}

@Composable
private fun HeaderYearRecap(year:Int,count:Int,value:Double,onclick:()->Unit){
    Row (modifier=Modifier.padding(bottom = Dimensions.PADDING_SHORT)){
        FieldView(name = R.string.year,
            value = year.toString(),
            isMoney = false,
            modifier = Weight1f()
        ,onClick = onclick)

        FieldView(name = R.string.count,
            value = count.toString(),
            isMoney = false,
            modifier = Weight1f(),
            onClick = onclick)

        FieldView(name = R.string.value,
            value = NumbersUtil.toString(value),
            modifier = Weight1f(),
            onClick = onclick)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun PeriodPreview(){
    MaterialThemeComposeUI {
        Period(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun PeriodPreviewDark(){
    MaterialThemeComposeUI {
        Period(getViewModel())
    }
}

@Composable
private fun getViewModel():PeriodsViewModel{
    val viewModel = PeriodsViewModel(
        codeAccount = null,
        navController = null,
        paidSvc = null
    )
    viewModel.list.put(2024, arrayListOf(
        PeriodPaidDTO(
            date = YearMonth.of(2024,3),
            value = 1000.0,
            count = 1
        ),
        PeriodPaidDTO(
            date = YearMonth.of(2024,2),
            value = 900.0,
            count = 2
        ),
        PeriodPaidDTO(
                date = YearMonth.of(2024,1),
        value = 700.0,
        count = 1
    )
    ))
    viewModel.list.put(2023, arrayListOf(
        PeriodPaidDTO(
            date = YearMonth.of(2023,12),
            value = 1200.0,
            count = 2
        ),
        PeriodPaidDTO(
            date = YearMonth.of(2023,11),
            value = 500.0,
            count = 5
        ),
        PeriodPaidDTO(
            date = YearMonth.of(2023,10),
            value = 400.0,
            count = 3
        )
    ))
    viewModel.loader.value = false
    return viewModel
}