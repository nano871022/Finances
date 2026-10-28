package co.com.japl.module.creditcard.views.bought

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.lists.AllBoughtRecordsViewModel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.utils.NumbersUtil
import java.time.format.DateTimeFormatter

@Composable
fun AllBoughtRecordsList(viewModel: AllBoughtRecordsViewModel) {
    val loader by remember { viewModel.loader }
    val searchQuery by remember { viewModel.searchQuery }
    val list = viewModel.filteredList

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            label = { Text(text = stringResource(id = R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (loader) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (list.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_data),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Text(
                text = "${stringResource(id = R.string.total_records)}: ${list.size}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(items = list, key = { it.id }) { item ->
                    RecordCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun RecordCard(item: CreditCardBoughtItemDTO) {
    val remainingMonths = (item.month - item.monthPaid).coerceAtLeast(0)
    val colorPending = if (item.pendingToPay > 0) Color(0xFFFFA500) else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.nameItem,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (item.nameCreditCard.isNotBlank()) {
                    Text(
                        text = item.nameCreditCard,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Text(
                text = item.boughtDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FieldView(
                    name = stringResource(id = R.string.original_value),
                    value = NumbersUtil.COPtoString(item.valueItem),
                    modifier = Modifier.weight(1f)
                )

                FieldView(
                    name = stringResource(id = R.string.period_months),
                    value = "${item.monthPaid}/${item.month}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FieldView(
                    name = stringResource(id = R.string.pending_value),
                    value = NumbersUtil.COPtoString(item.pendingToPay),
                    modifier = Modifier.weight(1f)
                )

                FieldView(
                    name = stringResource(id = R.string.months_remaining),
                    value = "$remainingMonths",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
