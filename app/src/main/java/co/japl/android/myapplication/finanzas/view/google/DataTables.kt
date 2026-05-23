package co.japl.android.myapplication.finanzas.view.google

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.NumbersUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataTables(viewModel:GoogleAuthBackupRestoreViewModel){
    val progress by remember {viewModel.statsLocalProgess}

    viewModel.onload()

    if(progress) {
        LinearProgressIndicator( modifier=Modifier.fillMaxWidth())
        Text(text= stringResource(R.string.loading_data),
            textAlign = TextAlign.Center,
            modifier=Modifier.fillMaxWidth())
    }else {
        DTBody(viewModel)
    }
}

@Composable
private fun DTBody(viewModel:GoogleAuthBackupRestoreViewModel){
    val columnNameWeight = 2f
    val columnCountWeight = 1f
    val stateStats = remember {viewModel.statsLocal }

    val listHeader = listOf(
        Header(title = "Nombre", tooltip = "Nombre de la tabla", weight = columnNameWeight),
        Header(title = "Cantidad", tooltip = "Cantidad de registros en la tabla",weight = columnCountWeight)
    )
    DataTable(listHeader = {_->listHeader},
        sizeBody = stateStats.size,
        splitEnable = false,
        footer = {_->
            BottomSum(stateStats)
        }) { index,_ ->
            Item(index,stateStats[index].first,stateStats[index].second)
    }
}

@Composable
private fun Item(id:Int,name:String, count:Long){
    Card(modifier=Modifier.padding(Dimensions.PADDING_SHORT).fillMaxWidth()){
        Row(modifier=Modifier.padding(Dimensions.PADDING_TOP)) {
            Text(
                text = "${NumbersUtil.toStringLong((id+1).toLong())}. ",
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Justify,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = NumbersUtil.toStringLong(count),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun BottomSum(list: MutableList<Pair<String,Long>>){
    val color = MaterialTheme.colorScheme.onBackground
    Card{
        Row(modifier=Modifier.padding(Dimensions.PADDING_TOP)) {
            Text(
                text = stringResource(R.string.total_records),
                color = color,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = NumbersUtil.toStringLong(list.sumOf { it.second }),
                color = color,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal  fun DataTablesPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        DataTables(vm)
    }
}

@Composable
private fun getViewModel():GoogleAuthBackupRestoreViewModel{
    val vm = GoogleAuthBackupRestoreViewModel(null, null, null, null, null)
    vm.statsLocalProgess.value = false
    vm.statsLocal.add(Pair("tb_tb1",10))
    vm.statsLocal.add(Pair("tb_tb2",20))
    return vm
}