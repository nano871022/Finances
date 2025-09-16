package co.com.japl.module.paid.views.projections.list

import android.content.res.Configuration
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.projections.list.ProjectionListViewModel
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialogPair
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.utils.NumbersUtil
import java.math.BigDecimal
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProjectionList(viewModel: ProjectionListViewModel) {
    val loadingStatus = remember { viewModel.loader }

    if(loadingStatus.value){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else{
        Scafold(viewModel)
    }
}

@Composable
private fun Scafold(viewModel: ProjectionListViewModel){
    val snackbarHost = remember { viewModel.snackbarHost }
    Scaffold (
        snackbarHost = {snackbarHost},
        floatingActionButton = {
            FloatButton(viewModel = viewModel)
        }, modifier = Modifier.padding(Dimensions.PADDING_SHORT)

    ){
        Body(viewModel, modifier = Modifier.padding(it))
    }
}

@Composable
private fun Body(viewModel: ProjectionListViewModel,modifier:Modifier){
    val mapList = viewModel.list.sortedBy { it.end }.groupBy { it.end.year }
    LazyColumn(modifier = modifier){
        item{
            Header(viewModel)
        }
        items(mapList.size){
            val entries = mapList.entries.elementAt(it)
            val list = entries.value
            Yearly(list,viewModel::edit,viewModel::delete)
        }
        item{
            if(viewModel.list.isEmpty()){
                Text(text = stringResource(R.string.not_record_available),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.PADDING_SHORT)
                    )
            }
        }
    }
}

@Composable
private fun Yearly(items: List<ProjectionDTO>, edit:(Int)->Unit, delete:(Int)->Unit){
    Surface (
        border= BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        modifier = Modifier.padding(bottom= Dimensions.PADDING_BOTTOM)) {
        Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
            Text(text = items.first().end.format(DateTimeFormatter.ofPattern("yyyy",Locale("es"))),
                modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT))
            Monthly(items, edit, delete)
        }
    }
}

@Composable
private fun Monthly(items: List<ProjectionDTO>, edit:(Int)->Unit, delete:(Int)->Unit){
    val mapList = items.sortedBy { it.end }.groupBy { it.end.month }
    mapList.forEach {
        Surface {
            Column {
                Text(
                    text = it.value.first().end.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM",
                            Locale("es:CO")
                        )
                    ),
                    modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)
                )
                it.value.forEach {
                    RowBody(it, edit, delete)
                }
            }
        }
    }
}

import androidx.navigation.NavController
import co.com.japl.module.paid.views.fakeSvc.ProjectionListPortFake

@Composable
private fun RowBody(item: ProjectionDTO, edit: (Int, NavController) -> Unit, delete: (Int) -> Unit) {
    val optionState = remember { mutableStateOf(false) }
    val optionDeleteState = remember { mutableStateOf(false) }
    val showStatus = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT),
        onClick = {
            showStatus.value = !showStatus.value
        }) {
        Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            Row {
                Text(
                    text = item.end.format(DateTimeFormatter.ofPattern("dd", Locale("es:CO"))),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = item.name,
                    modifier = Modifier
                        .weight(3f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = NumbersUtil.COPtoString(item.value),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                )
                IconButton(
                    imageVector = Icons.Rounded.MoreVert,
                    descriptionContent = R.string.options_projection,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        optionState.value = !optionState.value
                    }
                )
            }
            if (showStatus.value) {
                Row {
                    FieldView(
                        title = stringResource(R.string.months_left_to_pay_short),
                        value = item.monthsLeft.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    FieldView(
                        title = stringResource(R.string.saved_cash),
                        value = NumbersUtil.COPtoString(item.amountSaved),
                        modifier = Modifier.weight(1f)
                    )
                    FieldView(
                        title = stringResource(R.string.quote_value),
                        value = NumbersUtil.COPtoString(item.quote),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
    if (optionState.value) {
        MoreOptionsDialogPair(
            listOptions = listOf(
                Pair(0, stringResource(R.string.edit)),
                Pair(1, stringResource(R.string.delete))
            ),
            onDismiss = {
                optionState.value = false
            }) {
            when (it.first) {
                0 -> {
                    edit.invoke(item.id, navController)
                }
                1 -> {
                    optionDeleteState.value = true
                }
            }
            optionState.value = false
        }
    }
    if (optionDeleteState.value) {
        AlertDialogOkCancel(
            title = R.string.do_you_want_remove_this_projection,
            confirmNameButton = R.string.delete,
            onDismiss = {
                optionDeleteState.value = false

            }) {
            delete.invoke(item.id)
            optionDeleteState.value = false

        }

    }
}

@Composable
private fun Header(viewModel: ProjectionListViewModel){
    Row (modifier = Modifier.padding(bottom = Dimensions.PADDING_BOTTOM)){
        FieldView(
            title = stringResource(R.string.count_projection),
            value = viewModel.list.size.toString(),
            modifier = Modifier.weight(1f).padding(end= Dimensions.PADDING_SHORT)
        )
        FieldView(
            title = stringResource(R.string.total_saved),
            value = NumbersUtil.COPtoString(viewModel.list.sumOf { it.quote }),
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
private fun FloatButton(viewModel: ProjectionListViewModel){
    Column {
        FloatButton(
            imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.create_projection
        ) {
            viewModel.goToCreate()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true, showSystemUi = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun ProjectionListPreviewLight(){
    MaterialThemeComposeUI {
        ProjectionList(getViewModelList())
    }
}

@Composable
private fun getViewModelList(): ProjectionListViewModel {
    val vm = ProjectionListViewModel(context = LocalContext.current)
    vm.list.add(
        ProjectionDTO(
            id = 0,
            name = "prueba1",
            type= KindPaymentsEnums.MONTHLY,
            value= BigDecimal.valueOf(2_000),
            quote = BigDecimal.valueOf(200),
            amountSaved = BigDecimal.valueOf(800),
            monthsLeft = 7,
            active = true,
            create = LocalDate.now().minusMonths(3),
            end = LocalDate.now().plusMonths(6)
        ))
    vm.list.add(
        ProjectionDTO(
            id = 0,
            name = "prueba2",
            type= KindPaymentsEnums.MONTHLY,
            value= BigDecimal.valueOf(5_000),
            quote = BigDecimal.valueOf(1_000),
            amountSaved = BigDecimal.valueOf(2_000),
            monthsLeft = 3,
            active = true,
            create = LocalDate.now().minusMonths(1).minusDays(5),
            end = LocalDate.now().plusMonths(3).minusDays(5)
        ))
    vm.list.add(
        ProjectionDTO(
            id = 0,
            name = "prueba3",
            type= KindPaymentsEnums.MONTHLY,
            value= BigDecimal.valueOf(5_000),
            quote = BigDecimal.valueOf(1_000),
            amountSaved = BigDecimal.valueOf(2_000),
            monthsLeft = 3,
            active = true,
            create = LocalDate.now().minusMonths(1).minusDays(5),
            end = LocalDate.now().plusMonths(4).minusDays(5).plusYears(1)
        ))
    vm.list.add(
        ProjectionDTO(
            id = 0,
            name = "prueba4",
            type= KindPaymentsEnums.MONTHLY,
            value= BigDecimal.valueOf(5_000),
            quote = BigDecimal.valueOf(1_000),
            amountSaved = BigDecimal.valueOf(2_000),
            monthsLeft = 3,
            active = true,
            create = LocalDate.now().minusMonths(1).minusDays(5),
            end = LocalDate.now().plusMonths(2).minusDays(2).plusYears(1)
        ))
    return vm
}