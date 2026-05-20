package co.japl.android.myapplication.finanzas.view.google

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.model.datatable.Header

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsSpace(viewModel:GoogleAuthBackupRestoreViewModel){
    val progress by remember {viewModel.statsLocalProgess}
    val stateStats = remember {viewModel.statsLocal }
    val color = MaterialTheme.colorScheme.onBackground
    val columnNameWeight = 2f
    val columnCountWeight = 1f
    val listHeader = listOf(
        Header(title = "Nombre", tooltip = "Nombre de la tabla", weight = columnNameWeight),
        Header(title = "Cantidad", tooltip = "Cantidad de registros en la tabla",weight = columnCountWeight)
    )

    viewModel.onload()

    if(progress) {
        LinearProgressIndicator()
    }else {
        DataTable(listHeader = {_->listHeader},
            sizeBody = stateStats.size,
            footer = {_->
                Text(
                    text = "Total Registros",
                    color = color,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(columnNameWeight)
                )
                Text(
                    text = DecimalFormat("#,###").format(
                        stateStats.sumOf { it.second }),
                    color = color,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(columnCountWeight)
                )
            }) { index,_ ->
            Text(
                text = stateStats[index].first,
                color = color,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .weight(columnNameWeight)
                    .padding(end = 5.dp)
            )
            Text(
                text = DecimalFormat("#,###").format(stateStats[index].second),
                color = color,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(columnCountWeight)
            )
        }
    }
}