package co.com.japl.module.declaracion_renta_dian.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.com.japl.module.declaracion_renta_dian.R
import co.com.japl.module.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel

@Composable
fun HistoryTab(viewModel: TaxDeclarationViewModel) {
    val history by viewModel.historyState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(history) { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                ListItem(
                    headlineContent = { Text(text= stringResource(R.string.fiscal_year,item.fiscalYear),color= MaterialTheme.colorScheme.onBackground) },
                    supportingContent = { Text(text=stringResource(R.string.calculated_on,item.date),color= MaterialTheme.colorScheme.onBackground) },
                    trailingContent = { Text(text="$${item.taxValueCOP}",color= MaterialTheme.colorScheme.onBackground) }
                )
            }
        }
    }
}