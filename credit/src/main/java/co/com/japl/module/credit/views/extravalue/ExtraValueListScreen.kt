package co.com.japl.module.credit.views.extravalue

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.extravalue.ExtraValueListViewModel
import co.com.japl.module.credit.views.fakesSvc.ExtraValueAmortizationCreditFake
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.graphs.utils.NumbersUtil
import kotlinx.coroutines.flow.update

import java.text.NumberFormat
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExtraValueListScreen(viewModel: ExtraValueListViewModel) {
    val extraValues by viewModel.extraValues.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val numQuoteTitle = stringResource(id = R.string.number_of_quotes)
    val valueTitle = stringResource(id = R.string.value)
    val listHeader = listOf(
        Header(id=1,title=numQuoteTitle, tooltip = numQuoteTitle, weight=1f),
        Header(id=2,title=valueTitle, tooltip = valueTitle ,weight=1f))

    if (showDialog) {
        ExtraValueAmortizationDialog(
            onDismiss = { showDialog = false },
            onSave = { numQuotes, value ->
                viewModel.create( numQuotes, value)
                showDialog = false
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatButton(
                imageVector = Icons.Rounded.Add,
                descriptionIcon = R.string.add_extra_value,
                onClick = { showDialog = true }
            )
        }
    ) {
        Column {
            Text(
                text = stringResource(R.string.extra_values),
                fontSize= 20.sp,
                modifier = Modifier.padding(top = Dimensions.PADDING_TOP,start =Dimensions.PADDING_VIEW_SPACE)
            )
            DataTable(
                listHeader = { _ -> listHeader },
                sizeBody = extraValues.size,
                footer = {
                    Text(
                        text = stringResource(R.string.total),
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = NumbersUtil.COPtoString(extraValues.sumOf { it.value }),
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f).padding(end = Dimensions.PADDING_SHORT)
                    )
                },
            ) { index, dp ->
                val item = extraValues[index]
                val quoteText = stringResource(R.string.quote_num)
                Text(
                    text = "${quoteText} ${item.numQuote}", modifier = Modifier.weight(1f)
                )
                Text(
                    text = NumberFormat.getCurrencyInstance().format(item.value),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun PreviewViewLight(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        ExtraValueListScreen(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun PreviewViewDark(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        ExtraValueListScreen(viewModel = viewModel)
    }
}

@Composable
private fun getViewModel():ExtraValueListViewModel{
    val id = 0
    val saveStateBundle = SavedStateHandle()
    val viewModel = ExtraValueListViewModel(ExtraValueAmortizationCreditFake(),saveStateBundle)
    viewModel._extraValues.update {
        listOf(
            ExtraValueAmortizationDTO(
                id = 1,
                code=1,
                numQuote = 1,
                value = 1.0,
                create = LocalDate.now()
            ),
            ExtraValueAmortizationDTO(
                id = 2,
                code=1,
                numQuote = 2,
                value = 2.0,
                create = LocalDate.now()
            )
        )
    }
    return viewModel
}