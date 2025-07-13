package co.com.japl.credit.view.amortization

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.com.japl.credit.controller.amortization.AmortizationViewModel
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.credit.R
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal

@Composable
fun AmortizationScreen(viewModel: AmortizationViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        LinearProgressIndicator()
    } else {
        Body(state.amortization)
    }
}

@Composable
private fun Body(amortization: List<AmortizationRowDTO>) {
    Scaffold(
        modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ) {
        Column(modifier = Modifier.padding(it)) {
            Header(amortization)
            Table(amortization)
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun Header(amortization: List<AmortizationRowDTO>) {
    BoxWithConstraints {
        val isCompact = WindowWidthSize.MEDIUM.isEqualTo(maxWidth)
        Column {
            if (isCompact) {
                HeaderCompact(amortization)
            } else {
                HeaderLarge(amortization)
            }
        }
    }
}

@Composable
private fun HeaderCompact(amortization: List<AmortizationRowDTO>) {
    FieldView(
        title = stringResource(R.string.credit_value),
        NumbersUtil.COPtoString(amortization.firstOrNull()?.creditValue ?: BigDecimal.ZERO),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT)
    )
    Row {
        FieldView(
            title = stringResource(R.string.months),
            "${amortization.firstOrNull()?.periods}",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = Dimensions.PADDING_TOP)
        )
        FieldView(
            title = stringResource(R.string.interest),
            "${amortization.firstOrNull()?.creditRate} % ${amortization.firstOrNull()?.kindRate?.value}",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
    FieldView(
        title = stringResource(R.string.quote_value),
        NumbersUtil.COPtoString(amortization.firstOrNull()?.quoteValue ?: BigDecimal.ZERO),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimensions.PADDING_SHORT)
    )
}

@Composable
private fun HeaderLarge(amortization: List<AmortizationRowDTO>) {
    Row {
        FieldView(
            title = stringResource(R.string.credit_value),
            NumbersUtil.COPtoString(amortization.firstOrNull()?.creditValue ?: BigDecimal.ZERO),
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(end = Dimensions.PADDING_TOP)
        )
        FieldView(
            title = stringResource(R.string.months),
            "${amortization.firstOrNull()?.periods}",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = Dimensions.PADDING_TOP)
        )
        FieldView(
            title = stringResource(R.string.interest),
            "${amortization.firstOrNull()?.creditRate} % ${amortization.firstOrNull()?.kindRate?.value}",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun Table(amortization: List<AmortizationRowDTO>) {
    val monthsPerYear = 12
    val list = remember { amortization }
    Log.d("Table", "<<<=== Table:: $list")
    val listHeader = listOf(
        Header(id = 1, title = stringResource(R.string.value), tooltip = stringResource(R.string.amortization_value), weight = 3f),
        Header(id = 2, title = stringResource(R.string.capital), tooltip = stringResource(R.string.capital_value), weight = 3f),
        Header(id = 3, title = stringResource(R.string.interest), tooltip = stringResource(R.string.interest_value), weight = 3f),
        Header(id = 4, title = stringResource(R.string.quote), tooltip = stringResource(R.string.quote_value), weight = 3f)
    )

    DataTable(
        listHeader = {
            val isCompact = WindowWidthSize.MEDIUM.isEqualTo(it)
            listHeader.filter { (isCompact && it.id != 2) or !isCompact }
        },
        splitPos = if (list.size > monthsPerYear) monthsPerYear else 0,
        split = { pos, width ->
            Row {
                SplitterYearly(monthsPerYear, pos, width, list)
            }
        },
        sizeBody = list.size,
        footer = { maxWidth -> Footer(list, WindowWidthSize.MEDIUM.isEqualTo(maxWidth)) }
    ) { pos, maxWidth ->
        val isCompact = WindowWidthSize.MEDIUM.isEqualTo(maxWidth)
        val records = list.first { it.id.toInt() == pos + 1 }
        Row(records, isCompact)
    }
}

@Composable
private fun RowScope.SplitterYearly(monthsPerYear: Int, pos: Int, width: Dp, list: List<AmortizationRowDTO>) {
    val isCompact = WindowWidthSize.MEDIUM.isEqualTo(width)
    val listSeg = list.takeIf { it.size > pos + monthsPerYear }?.let { list.subList(pos, pos + monthsPerYear) } ?: list.subList(pos, list.size)
    Text(
        text = stringResource(R.string.year),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
    )
    Text(
        text = " ${(pos / monthsPerYear) + 1}    ",
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
    )
    Text(
        text = stringResource(R.string.capital),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.weight(1f)
    )
    Text(
        text = NumbersUtil.COPtoString(listSeg.sumOf { it.capitalValue }),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(1f)
            .padding(end = Dimensions.PADDING_SHORT)
    )
    Text(
        text = stringResource(R.string.interest),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.weight(1f)
    )
    Text(
        text = NumbersUtil.COPtoString(listSeg.sumOf { it.interestValue }),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(1f)
    )
    if (!isCompact) {
        Text(
            text = stringResource(R.string.quote),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(start = Dimensions.PADDING_SHORT)
        )
        Text(
            text = NumbersUtil.COPtoString(listSeg.sumOf { it.quoteValue }),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RowScope.Footer(list: List<AmortizationRowDTO>, isCompact: Boolean) {
    Text(text = " ", modifier = Modifier.padding(7.dp))
    Text(
        text = stringResource(R.string.total),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(3f)
            .align(alignment = Alignment.CenterVertically)
    )
    if (isCompact.not()) {
        Text(
            text = NumbersUtil.COPtoString(list.sumOf { it.capitalValue }),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .weight(3f)
                .align(alignment = Alignment.CenterVertically)
        )
    }
    Text(
        text = NumbersUtil.COPtoString(list.sumOf { it.interestValue }),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(3f)
            .align(alignment = Alignment.CenterVertically)
    )
    Text(
        text = NumbersUtil.COPtoString(list.sumOf { it.quoteValue }),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(3f)
            .align(alignment = Alignment.CenterVertically)
    )
}

@Composable
private fun RowScope.Row(row: AmortizationRowDTO, isCompact: Boolean) {
    Text(
        text = NumbersUtil.COPtoString(row.amortizatedValue),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(3f)
            .align(alignment = Alignment.CenterVertically)
    )
    if (isCompact.not()) {
        Text(
            text = NumbersUtil.COPtoString(row.capitalValue),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .weight(3f)
                .align(alignment = Alignment.CenterVertically)
        )
    }
    Text(
        text = NumbersUtil.COPtoString(row.interestValue),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(3f)
            .align(alignment = Alignment.CenterVertically)
    )
    Text(
        text = NumbersUtil.COPtoString(row.quoteValue),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier
            .weight(3f)
            .align(alignment = Alignment.CenterVertically)
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun AmortizationLight() {
    val amortization = getAmortization()
    MaterialThemeComposeUI {
        Body(amortization)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0x00000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AmortizationDark() {
    val amortization = getAmortization()
    MaterialThemeComposeUI {
        Body(amortization)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=821dp,height=421dp", backgroundColor = 0xFFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun AmortizationLightVertical() {
    val amortization = getAmortization()
    MaterialThemeComposeUI {
        Body(amortization)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=821dp,height=421dp", backgroundColor = 0x00000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AmortizationDarkVertical() {
    val amortization = getAmortization()
    MaterialThemeComposeUI {
        Body(amortization)
    }
}

private fun getAmortization(): List<AmortizationRowDTO> {
    return listOf(
        AmortizationRowDTO(
            id = 1,
            periods = 4,
            creditRate = 13.0,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 1_000_000.toBigDecimal(),
            amortizatedValue = 1_000_000.toBigDecimal(),
            capitalValue = 250_000.toBigDecimal(),
            interestValue = 10_000.toBigDecimal(),
            quoteValue = 260_000.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 2,
            periods = 4,
            creditRate = 13.0,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 1_000_000.toBigDecimal(),
            amortizatedValue = 750_000.toBigDecimal(),
            capitalValue = 250_000.toBigDecimal(),
            interestValue = 7_500.toBigDecimal(),
            quoteValue = 257_500.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 3,
            periods = 4,
            creditRate = 13.0,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 1_000_000.toBigDecimal(),
            amortizatedValue = 500_000.toBigDecimal(),
            capitalValue = 250_000.toBigDecimal(),
            interestValue = 5_000.toBigDecimal(),
            quoteValue = 255_000.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 4,
            periods = 4,
            creditRate = 13.0,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 1_000_000.toBigDecimal(),
            amortizatedValue = 250_000.toBigDecimal(),
            capitalValue = 250_000.toBigDecimal(),
            interestValue = 2_500.toBigDecimal(),
            quoteValue = 252_500.toBigDecimal()
        )
    )
}
