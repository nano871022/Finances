package co.com.japl.module.credit.views.creditamortization

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Addchart
import androidx.compose.material.icons.rounded.PlusOne
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.model.datatable.Header
import java.math.BigDecimal
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreditAmortizationScreen(viewModel: CreditAmortizationViewModel){
    val state by viewModel.state.collectAsState()

    if(state.isLoading){
        LinearProgressIndicator(modifier=Modifier.fillMaxWidth())
    }else {
        Body(viewModel )
    }
}

@Composable
private fun Body(viewModel: CreditAmortizationViewModel) {
    val state by viewModel.state.collectAsState()
    val dateCredit = state.credit?.date?:LocalDate.now()
    val yearMonth = YearMonth.of(dateCredit.year,dateCredit.month)
    Scaffold (floatingActionButton = {FloatButtons(viewModel)},
        modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        Column(modifier = Modifier.fillMaxWidth().padding(it)) {
            Header(viewModel)
            Table(viewModel.state.value.quotesPaid?:0,
                state.amortization ?: emptyList(),
                yearMonth
                )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun Header(viewModel: CreditAmortizationViewModel) {
    BoxWithConstraints {
        val size = WindowWidthSize.fromDp(maxWidth)
        Column {
            val state by viewModel.state.collectAsState()
            FieldView(
                title = stringResource(id = R.string.date_credit),
                value = state.credit?.date.toString(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                FieldView(
                    title = stringResource(id = R.string.periods),
                    value = state.credit?.periods.toString(),
                    modifier = Modifier.weight(1f)
                )
                FieldView(
                    title = stringResource(id = R.string.interest_value_short),
                    value = "${state.credit?.tax} ${state.credit?.kindOfTax}",
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
            if(size == WindowWidthSize.COMPACT) {
                FieldView(
                    title = stringResource(id = R.string.quote_value),
                    value = NumbersUtil.COPtoString(state.credit?.quoteValue ?: BigDecimal.ZERO),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FloatButtons(viewModel: CreditAmortizationViewModel) {
    Column(modifier = Modifier) {

        FloatButton(
            descriptionIcon =  R.string.extra_value_list,
            imageVector= Icons.Rounded.PlusOne,
            onClick = { viewModel.goToExtraValues() },
        )
        FloatButton(
            imageVector = Icons.Rounded.Addchart,
            descriptionIcon =  R.string.additional_to_pay,
            onClick = { viewModel.goToAdditional() },
        )
    }
}

@Composable
private fun Table(quotesPaid:Int,amortization: List<AmortizationRowDTO>,yearMonth: YearMonth) {
    val titleValor = stringResource(id = R.string.value)
    val titleCapital = stringResource(id = R.string.capital_value_short)
    val titleInterest = stringResource(id = R.string.interest_value_short)
    val titleQuote = stringResource(id=R.string.quote_value_short)
    val tooltipValor = stringResource(id = R.string.amortization_value)
    val tooltipCapital = stringResource(id = R.string.capital_value)
    val tooltipInterest = stringResource(id = R.string.interest_value)

    val listHeader = remember {
        listOf(
            Header(id = 2, title = titleValor, tooltip = tooltipValor, weight = 3f),
            Header(id = 3, title = titleCapital, tooltip = tooltipCapital, weight = 3f),
            Header(id = 4, title = titleInterest, tooltip = tooltipInterest, weight = 3f),
            Header(id = 5, title = titleQuote, tooltip = tooltipInterest, weight = 3f)
        )
    }

    DataTable(
        listHeader = {
            val size = WindowWidthSize.fromDp(it)
            listHeader.filter { (size == WindowWidthSize.COMPACT && it.id != 5) || size != WindowWidthSize.COMPACT}
                     },
        sizeBody = amortization.size,
        splitPos = 12,
        highlightPos = quotesPaid,
        split = { pos,size ->
            val year =  yearMonth.plusMonths(pos.toLong()).year
            val record = amortization.subList(pos,pos+12)
            val interest = record.sumOf { it.interestValue }
            val capital = record.sumOf{ it.capitalValue }
            Split(year,capital,interest, WindowWidthSize.fromDp(size))
        },
        footer={
            val size = WindowWidthSize.fromDp(it)
            Footer(amortization,size)
        }
    ) { pos, width ->
        val size = WindowWidthSize.fromDp(width)
        val record = amortization[pos]
        Row(record,size)
    }
}

@Composable
private fun Split(year:Int,capital:BigDecimal,interest:BigDecimal,size: WindowWidthSize){
    Row{
        Text(text="$year",
            color=MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Left,
            modifier=Modifier.weight(0.5f).align(alignment = Alignment.CenterVertically))
        Text(text=stringResource(R.string.capital_value),
            color=MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier=Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
        Text(text=NumbersUtil.COPtoString(capital),
            color=MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier=Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
        Text(text=stringResource(R.string.interest_value),
            color=MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier=Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
        Text(text=NumbersUtil.COPtoString(interest),
            color=MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier=Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
    }
}

@Composable
private fun RowScope.Footer(amortization: List<AmortizationRowDTO>,size: WindowWidthSize){
    Text(
        text=stringResource(R.string.total),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(1.1f).align (alignment = Alignment.CenterVertically)
    )
    Text(
        text=NumbersUtil.COPtoString(amortization.sumOf{it.capitalValue}),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(1f).align (alignment = Alignment.CenterVertically)
    )
    Text(
        text=NumbersUtil.COPtoString(amortization.sumOf{it.interestValue}),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(1f).align (alignment = Alignment.CenterVertically)
    )
    if(size != WindowWidthSize.COMPACT) {
        Text(
            text = NumbersUtil.COPtoString(amortization.sumOf { it.quoteValue }),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically)
        )
    }
}

@Composable
private fun RowScope.Row(row: AmortizationRowDTO,size:WindowWidthSize) {

    Text(
        text = NumbersUtil.COPtoString(row.amortizatedValue),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(3f)
    )
    Text(
        text = NumbersUtil.COPtoString(row.capitalValue),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(3f)
    )
    Text(
        text = NumbersUtil.COPtoString(row.interestValue),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Right,
        modifier = Modifier.weight(3f)
    )
    if(size != WindowWidthSize.COMPACT) {
        Text(
            text = NumbersUtil.COPtoString(row.quoteValue),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(3f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CreditAmortizationScreenPreviewLight() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditAmortizationScreen(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO,device=Devices.FOLDABLE)
@Composable
private fun CreditAmortizationScreenPreviewLightFold() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditAmortizationScreen(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES )
@Composable
private fun CreditAmortizationScreenPreviewDark() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditAmortizationScreen(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device= Devices.PIXEL_TABLET )
@Composable
private fun CreditAmortizationScreenPreviewDarkTablet() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditAmortizationScreen(viewModel)
    }
}

private fun getViewModel():CreditAmortizationViewModel{
    val creditCode = 1
    val lastDate = LocalDate.now()
    val viewModel = CreditAmortizationViewModel(creditCode, lastDate)
    viewModel.state.value.isLoading=false
    viewModel.state.value.credit = CreditDTO(
        id = 1,
        name = "Test",
        date = LocalDate.now(),
        tax = 25.2,
        periods = 6,
        value = 10000.toBigDecimal(),
        quoteValue = 800.toBigDecimal(),
        kindOf = KindPaymentsEnums.ANNUAL,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE
    )
    viewModel.state.value.additional = 100.toBigDecimal()
    viewModel.state.value.amortization = listOf(
        AmortizationRowDTO(
            id = 1,
            periods = 6,
            creditRate = 25.5,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 10_000.toBigDecimal(),
            amortizatedValue = 10_000.toBigDecimal(),
            capitalValue = 800.toBigDecimal(),
            interestValue = 20.toBigDecimal(),
            quoteValue = 820.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 2,
            periods = 6,
            creditRate = 25.5,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 10_000.toBigDecimal(),
            amortizatedValue = 9_200.toBigDecimal(),
            capitalValue = 802.toBigDecimal(),
            interestValue = 18.toBigDecimal(),
            quoteValue = 820.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 3,
            periods = 6,
            creditRate = 25.5,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 10_000.toBigDecimal(),
            amortizatedValue = 8_398.toBigDecimal(),
            capitalValue = 808.toBigDecimal(),
            interestValue = 16.toBigDecimal(),
            quoteValue = 820.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 4,
            periods = 6,
            creditRate = 25.5,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 10_000.toBigDecimal(),
            amortizatedValue = 7_390.toBigDecimal(),
            capitalValue = 810.toBigDecimal(),
            interestValue = 10.toBigDecimal(),
            quoteValue = 820.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 5,
            periods = 6,
            creditRate = 25.5,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 10_000.toBigDecimal(),
            amortizatedValue = 6_380.toBigDecimal(),
            capitalValue = 815.toBigDecimal(),
            interestValue = 5.toBigDecimal(),
            quoteValue = 820.toBigDecimal()
        ),
        AmortizationRowDTO(
            id = 6,
            periods = 6,
            creditRate = 25.5,
            kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
            creditValue = 10_000.toBigDecimal(),
            amortizatedValue = 800.toBigDecimal(),
            capitalValue = 818.toBigDecimal(),
            interestValue = 2.toBigDecimal(),
            quoteValue = 820.toBigDecimal()
        )

    )

    return viewModel
}
