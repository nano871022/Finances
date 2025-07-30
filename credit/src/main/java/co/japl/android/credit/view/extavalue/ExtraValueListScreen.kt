package co.japl.android.credit.view.extavalue

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import co.japl.android.credit.R
import co.japl.android.finances.services.dto.AddAmortizationDTO
import co.japl.android.finances.services.dto.ExtraValueAmortizationCreditDTO
import co.japl.android.finances.services.dto.ExtraValueAmortizationQuoteCreditCardDTO
import co.japl.android.finances.services.enums.AmortizationKindOfEnum
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.TopBar
import co.com.japl.ui.model.datatable.Header
import java.text.NumberFormat

@Composable
fun ExtraValueListScreen(viewModel: ExtraValueListViewModel = hiltViewModel(), creditId: Int, kindOf: AmortizationKindOfEnum) {
    viewModel.loadExtraValues(creditId, kindOf)
    val extraValues by viewModel.extraValues.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddValueAmortizationDialog(
            onDismiss = { showDialog = false },
            onSave = { numQuotes, value ->
                viewModel.create(creditId, numQuotes, value, kindOf)
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.extra_values),
                actions = {
                    // Actions can be added here
                }
            )
        },
        floatingActionButton = {
            FloatButton(
                imageVector = Icons.Rounded.Add,
                descriptionIcon = R.string.add_extra_value,
                onClick = { showDialog = true }
            )
        }
    ) { paddingValues ->
        DataTable(
            listHeader = {
                listOf(
                    Header(stringResource(id = R.string.number_of_quotes), 1f),
                    Header(stringResource(id = R.string.value), 1f)
                )
            },
            sizeBody = extraValues.size,
            listBody = { index, _ ->
                val item = extraValues[index]
                val (nbrQuote, value) = when (item) {
                    is AddAmortizationDTO -> item.nbrQuote.toString() to item.value
                    is ExtraValueAmortizationCreditDTO -> item.nbrQuote.toString() to item.value
                    is ExtraValueAmortizationQuoteCreditCardDTO -> item.nbrQuote.toString() to item.value
                    else -> "" to 0.0
                }
                Text(text = nbrQuote)
                Text(text = NumberFormat.getCurrencyInstance().format(value))
            },
            footer = {
                // Footer can be added here
            },
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        )
    }
}
