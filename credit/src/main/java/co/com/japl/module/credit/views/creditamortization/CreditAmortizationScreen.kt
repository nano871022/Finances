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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreditAmortizationScreen(viewModel:CreditAmortizationViewModel,
                             goToExtraValues: (Int) -> Unit,
                             goToAdditional: (Int) -> Unit){
    val state by viewModel.state.collectAsState()
    Scaffold (modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
        if(state.isLoading){
            LinearProgressIndicator()
        }else {
            Column(modifier = Modifier.padding(it)) {
                FieldView(title = stringResource(id = R.string.date_credit),
                    value = state.credit?.date.toString(),
                    modifier = Modifier.fillMaxWidth())

                Row (modifier = Modifier.fillMaxWidth()){
                    FieldView(title = stringResource(id = R.string.interest_value_short),
                        value = state.credit?.tax.toString(),
                        modifier = Modifier.weight(1f))
                    FieldView(title = stringResource(id = R.string.periods),
                        value = state.credit?.periods.toString(),
                        modifier = Modifier.weight(1f))
                }

                Row (modifier = Modifier.fillMaxWidth()){
                    FieldView(title = stringResource(id = R.string.interest_to_pay_value),
                        value = NumbersUtil.COPtoString(state.credit?.quoteValue?:BigDecimal.ZERO),
                        modifier = Modifier.weight(1f))

                    FieldView(title = stringResource(id = R.string.additional_monthly),
                        value = NumbersUtil.COPtoString(state.additional?: BigDecimal.ZERO),
                        modifier = Modifier.weight(1f))
                }

                FieldView(title = stringResource(id = R.string.quote_value),
                    value = NumbersUtil.COPtoString(state.credit?.quoteValue?:BigDecimal.ZERO),
                    modifier = Modifier.fillMaxWidth())

                Row (modifier = Modifier.fillMaxWidth()){
                    Buttons(
                        title = stringResource(id = R.string.extra_value_list),
                        onClick = { goToExtraValues.invoke(state.credit!!.id) },
                        modifier = Modifier.weight(1f))

                    Buttons(
                        title = stringResource(id = R.string.additional_to_pay),
                        onClick = { goToAdditional.invoke(state.credit!!.id) },
                        modifier = Modifier.weight(1f))

                }

                val listHeader = listOf(
                    Header(id=1, title = "#", weight = 1f),
                    Header(id=2, title = stringResource(id = R.string.value), weight = 3f),
                    Header(id=3, title = stringResource(id = R.string.capital_value), weight = 3f),
                    Header(id=4, title = stringResource(id = R.string.interest_value), weight = 3f)
                )

                DataTable(
                    listHeader = {listHeader},
                    sizeBody = state.amortization?.size?:0)
                    { pos, _ ->
                        val record = state.amortization!![pos]
                        Row(record)
                    }
            }
        }
    }
}

@Composable
private fun Row(row:AmortizationRowDTO){
    androidx.compose.material.Text(text = row.id.toString(), modifier = Modifier.weight(1f))
    androidx.compose.material.Text(text = NumbersUtil.COPtoString(row.amortizatedValue), modifier = Modifier.weight(3f))
    androidx.compose.material.Text(text = NumbersUtil.COPtoString(row.capitalValue), modifier = Modifier.weight(3f))
    androidx.compose.material.Text(text = NumbersUtil.COPtoString(row.interestValue), modifier = Modifier.weight(3f))
}
