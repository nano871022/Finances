package co.com.japl.module.credit.views.lists

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.list.AdditionalViewModel
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialogPair
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDate

@Composable
fun Additional(viewModel: AdditionalViewModel = viewModel()){
    val loading = viewModel.loading.collectAsState()
    if(loading.value) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else {
        ScaffoldBody(viewModel=viewModel)
    }
}

@Composable
private fun ScaffoldBody(viewModel: AdditionalViewModel ){
    val state = remember{ viewModel.hostState }
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = state)
        },
        floatingActionButton = {
            ButtonsFloating(viewModel=viewModel)
        }
    ){
        Body(viewModel=viewModel,modifier = Modifier.padding(it))
    }


}

@Composable
private fun Body(viewModel: AdditionalViewModel ,modifier:Modifier = Modifier){
    Column (modifier=Modifier.padding(Dimensions.PADDING_SHORT)){
        Header(viewModel=viewModel)
        ListBody(viewModel=viewModel)
    }
}

@Composable
private fun Header(viewModel: AdditionalViewModel) {
    Row(modifier = Modifier.fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.onBackground)
        .padding(Dimensions.PADDING_SHORT)) {
        Text(
            text = stringResource(R.string.count),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${viewModel.list.count()}",
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(R.string.total_value),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = NumbersUtil.COPtoString(viewModel.list.sumOf { it.value }),
            textAlign = TextAlign.Right,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
private fun ListBody(viewModel: AdditionalViewModel){
    val list = remember { viewModel.list}
    DataTable(
        listHeader =  arrayListOf(Header(
            title = stringResource(R.string.title_name_additional_list),
            tooltip = stringResource(R.string.tooltip_name_additional_list),
            weight = 1f
        ),Header(
            title = stringResource(R.string.title_value_additional_list),
            tooltip = stringResource(R.string.tooltip_value_additional_list),
            weight = 1f
        ))  ,
        sizeBody = list.size,
        footer = { RowFooter(viewModel) },
        listBody = { RowListBody(it, viewModel) }
    )
}

@Composable
private  fun RowScope.RowListBody(index:Int,viewModel: AdditionalViewModel) {
    val moreStatus = remember { mutableStateOf(false) }
    val deleteStatus = remember { mutableStateOf(false) }
    val value = viewModel.list[index]

    Text(
        text = value.name,
        modifier = Modifier
            .weight(1f)
            .align(alignment = Alignment.CenterVertically)
    )

    Text(
        text = NumbersUtil.COPtoString(value.value),
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(1f)
            .align(alignment = Alignment.CenterVertically)
    )

    IconButton(
        imageVector = Icons.Rounded.MoreVert,
        descriptionContent = R.string.see_more,
        onClick={
            moreStatus.value = true
        }
    )
    if(moreStatus.value){
        MoreOptionsDialogPair(
            listOptions = arrayListOf<Pair<Int,String>>(
                Pair(1,stringResource(R.string.edit)),
                Pair(2,stringResource(R.string.delete))
            ),
            onDismiss = { moreStatus.value = false}
        ) { optSelected ->
            when(optSelected.first){
                1 -> viewModel.updateAdditional(value.id)
                2 -> deleteStatus.value = true
                else -> moreStatus.value = false
            }
            moreStatus.value = false
        }
    }

    if(deleteStatus.value){
        AlertDialogOkCancel(
            title = R.string.do_you_want_to_delete_additional,
            confirmNameButton = R.string.delete,
            onDismiss = { deleteStatus.value = false }
        ) {
            viewModel.deleteAdditional(value.id)
            deleteStatus.value = false
        }

    }
}


@Composable
private fun RowScope.RowFooter(viewModel: AdditionalViewModel){

}

@Composable
private fun ButtonsFloating(viewModel: AdditionalViewModel ){
    Column {
        FloatButton(imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.Add) {
            viewModel.addAdditional()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true,  backgroundColor = 0xFFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun AdditionalPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        Additional(vm)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AdditionalPreviewNight(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        Additional(vm)
    }
}

@Composable
private fun getViewModel(): AdditionalViewModel{
    val vm = AdditionalViewModel(LocalContext.current,0,null,null)
    vm.list.add(AdditionalCreditDTO(
        id = 1,
        name = "Test",
        value = 1000000000.toBigDecimal(),
        startDate = LocalDate.now(),
        creditCode = 2,
        endDate = LocalDate.now()
    ))
    vm.list.add(AdditionalCreditDTO(
        id = 1,
        name = "Test 2",
        value = 3200000000.toBigDecimal(),
        startDate = LocalDate.now(),
        creditCode = 2,
        endDate = LocalDate.now()
    ))
    return vm
}