package co.com.japl.module.credit.views.creditamortization

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationViewModel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.android.myapplication.utils.NumbersUtil
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.ui.components.Buttons
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.model.datatable.Header
import java.math.BigDecimal
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreditAmortizationScreen(
    creditId: Int,
    lastDate: String,
    goToExtraValues: (Int) -> Unit,
    goToAdditional: (Int) -> Unit
){
    val viewModel: CreditAmortizationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = creditId) {
        if (creditId != 0) {
            viewModel.loadData(creditId, DateUtils.toLocalDate(lastDate))
        }
    }

    Scaffold (modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
        if(state.isLoading){
            LinearProgressIndicator(modifier=Modifier.fillMaxWidth())
        }else {
            Body(state, goToExtraValues, goToAdditional)
        }
    }
}

@Composable
private fun Body(
    state: co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationState,
    goToExtraValues: (Int) -> Unit,
    goToAdditional: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(state)
        Buttons(
            goToExtraValues,
            goToAdditional,
            state.credit?.id ?: 0
        )
        Table(state.amortization ?: emptyList())
    }
}

@Composable
private fun Header(state: co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationState) {
    FieldView(
        title = stringResource(id = R.string.date_credit),
        value = state.credit?.date.toString(),
        modifier = Modifier.fillMaxWidth()
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        FieldView(
            title = stringResource(id = R.string.interest_value_short),
            value = state.credit?.tax.toString(),
            modifier = Modifier.weight(1f)
        )
        FieldView(
            title = stringResource(id = R.string.periods),
            value = state.credit?.periods.toString(),
            modifier = Modifier.weight(1f)
        )
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        FieldView(
            title = stringResource(id = R.string.interest_to_pay_value),
            value = NumbersUtil.COPtoString(state.credit?.quoteValue ?: BigDecimal.ZERO),
            modifier = Modifier.weight(1f)
        )

        FieldView(
            title = stringResource(id = R.string.additional_monthly),
            value = NumbersUtil.COPtoString(state.additional ?: BigDecimal.ZERO),
            modifier = Modifier.weight(1f)
        )
    }

    FieldView(
        title = stringResource(id = R.string.quote_value),
        value = NumbersUtil.COPtoString(state.credit?.quoteValue ?: BigDecimal.ZERO),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Buttons(
    goToExtraValues: (Int) -> Unit,
    goToAdditional: (Int) -> Unit,
    creditId: Int
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Buttons(
            title = stringResource(id = R.string.extra_value_list),
            onClick = { goToExtraValues.invoke(creditId) },
            modifier = Modifier.weight(1f)
        )

        Buttons(
            title = stringResource(id = R.string.additional_to_pay),
            onClick = { goToAdditional.invoke(creditId) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Table(amortization: List<AmortizationRowDTO>) {
    val listHeader = remember {
        listOf(
            Header(id = 1, title = "#", weight = 1f),
            Header(id = 2, title = stringResource(id = R.string.value), weight = 3f),
            Header(id = 3, title = stringResource(id = R.string.capital_value), weight = 3f),
            Header(id = 4, title = stringResource(id = R.string.interest_value), weight = 3f)
        )
    }

    DataTable(
        listHeader = { listHeader },
        sizeBody = amortization.size
    ) { pos, _ ->
        val record = amortization[pos]
        Row(record)
    }
}

@Composable
private fun Row(row: AmortizationRowDTO) {
    androidx.compose.material.Text(text = row.id.toString(), modifier = Modifier.weight(1f))
    androidx.compose.material.Text(
        text = NumbersUtil.COPtoString(row.amortizatedValue),
        modifier = Modifier.weight(3f)
    )
    androidx.compose.material.Text(
        text = NumbersUtil.COPtoString(row.capitalValue),
        modifier = Modifier.weight(3f)
    )
    androidx.compose.material.Text(
        text = NumbersUtil.COPtoString(row.interestValue),
        modifier = Modifier.weight(3f)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreditAmortizationScreenPreview() {
    val viewModel: CreditAmortizationViewModel = hiltViewModel()
    MaterialThemeComposeUI {
        CreditAmortizationScreen(viewModel, goToExtraValues = {}, goToAdditional = {})
    }
}
