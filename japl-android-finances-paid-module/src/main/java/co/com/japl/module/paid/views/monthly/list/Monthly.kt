package co.com.japl.module.paid.views.monthly.list

import android.content.res.Configuration
import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.monthly.list.MonthlyViewModel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.PiecePieGraph
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.graphs.interfaces.IGraph
import co.japl.android.graphs.pieceofpie.PieceOfPie
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun Monthly(viewModel:MonthlyViewModel) {
    val loaderState = remember {
        viewModel.loaderState   }
    val progressStatus = remember {
        viewModel.progressStatus
    }

    CoroutineScope(Dispatchers.IO).launch{
        viewModel.main()
    }

    if(loaderState.value){
        LinearProgressIndicator(progress = progressStatus.value,modifier=Modifier.fillMaxWidth())
    }else{
        Body(viewModel)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Body(viewModel: MonthlyViewModel){
    val accountList = remember {
viewModel.accountList
    }
    val accountState = remember {
        viewModel.accountState    }

    val listGraph = remember {
        viewModel.listGraph
    }
    Scaffold (
       floatingActionButton = {
            Buttons (gotoList = {viewModel.goToListDetail()},gotoPeriod = {viewModel.goToListPeriod()},gotoCreate = {viewModel.goToListCreate()})
       },
        modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ){
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
          FieldSelect(title = stringResource(id = R.string.account),
              value = accountState.value?.name?:"",
              list = accountList,
              modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT),
              callAble={pair->
                pair?.let{viewModel.listAccount?.first { it.id == pair.first }?.let {
                    accountState.value = it
                    viewModel.loaderState.value = true
                }}?:accountState?.let { it.value = null }
              })

            if(accountState.value != null) {
                Accounts(viewModel = viewModel)

                PiecePieGraph(list = listGraph)
            }

        }
    }
}

@Composable
private fun Buttons(gotoList:()->Unit,gotoPeriod:()->Unit,gotoCreate:()->Unit) {
    Column {


        FloatButton(imageVector = Icons.Rounded.RemoveRedEye, descriptionIcon = R.string.go_to_list, onClick = gotoList)
        FloatButton(
            imageVector = Icons.Rounded.CalendarMonth,
            descriptionIcon =R.string.go_to_periods,
            onClick = gotoPeriod
        )
        FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon =R.string.go_to_add, onClick = gotoCreate)
    }


}

@Composable
private fun Accounts(viewModel: MonthlyViewModel) {
    val periodState = remember {
        viewModel.periodState
    }
    val countState = remember {
        viewModel.countState
    }
    val paidTotalState = remember {
        viewModel.paidTotalState
    }
    val incomesTotalState = remember {
        viewModel.incomesTotalState
    }
    Row (modifier = Modifier.padding(bottom=Dimensions.PADDING_SHORT)){

        FieldView(name = stringResource(id = R.string.period),
            value = periodState.value,
            isMoney= false,
            modifier = Modifier
                .weight(1f)
                .padding(end = Dimensions.PADDING_SHORT))

        FieldView(name = stringResource(id = R.string.count),
            value = countState.value.toString(),
            isMoney=false,
            modifier = Modifier.weight(1f))

    }
    FieldView(name = stringResource(id = R.string.paid_total),
        value = NumbersUtil.toString(paidTotalState.value),

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT))

    Row (
        modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)
    ){

        FieldView(name = stringResource(id = R.string.incomes_total),
            value = NumbersUtil.toString(incomesTotalState.value),
            modifier = Modifier
                .weight(1f)
                .padding(end = Dimensions.PADDING_SHORT))

        FieldView(name = stringResource(id = R.string.incomes_less_paids),
            value = NumbersUtil.toString(incomesTotalState.value - paidTotalState.value)
            , modifier = Modifier.weight(1f))
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MonthlyPreview() {
    MaterialThemeComposeUI {

        Monthly(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MonthlyPreviewDark() {
    MaterialThemeComposeUI {

        Monthly(getViewModel())
    }
}

@Composable
private fun getViewModel():MonthlyViewModel{
    val viewModel = MonthlyViewModel(paidSvc = null,incomesSvc = null,accountSvc = null,navController = null)
    viewModel.loaderState.value = false
    viewModel.accountState.value = AccountDTO(id = 0, name = "Cuenta", active = true, create = LocalDate.now())
    viewModel.periodState.value = "Enero 2022"
    viewModel.countState.value = 1
    viewModel.incomesTotalState.value = 1000.0
    viewModel.paidTotalState.value = 1000.0
    return viewModel
}