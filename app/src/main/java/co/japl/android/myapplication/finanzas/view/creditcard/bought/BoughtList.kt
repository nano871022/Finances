package co.japl.android.myapplication.finanzas.view.creditcard.bought

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import co.japl.android.myapplication.finanzas.view.components.FieldView
import co.japl.android.myapplication.finanzas.view.creditcard.bought.list.BoughList
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun BoughtList(data:BoughtCreditCard){
    val popupState = remember { mutableStateOf(false) }

    Column(modifier=Modifier.fillMaxWidth()) {
        BoxWithConstraints {
            if(WindowWidthSize.MEDIUM.isEqualTo(maxWidth)){
                Column {
                    MainCompact(data, popupState)
                }
            }else{
                Row {
                    MainCompact(data, popupState,modifier=Modifier.weight(1f), modifierHeader = Modifier.weight(2f))
                }
            }
        }

        BoughList(data = data)
    }
    Popup(data.recap,popupState = popupState)

}

@Composable
private fun MainCompact(data:BoughtCreditCard,popupState: MutableState<Boolean>,modifier:Modifier=Modifier,modifierHeader:Modifier=Modifier){
        Header(data.recap.totalCapital, data.recap.totalInterest, data.recap.quoteValue,modifierHeader)

        OutlinedButton(onClick = { popupState.value = !popupState.value },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)) {
            Text(
                text = stringResource(id = R.string.see_more),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
}


@Composable
private fun Header(capital:Double, interest:Double, quote:Double,modifier:Modifier=Modifier.fillMaxWidth()){
    Row(modifier=modifier) {

        FieldView(name = stringResource(id = R.string.capital_value_short)
            , value = NumbersUtil.toString(capital)
            , modifier = Modifier.weight(1f))

        FieldView(name = stringResource(id = R.string.interest_value_short)
            , value = NumbersUtil.toString(interest)
            , modifier = Modifier.weight(1f))

        FieldView(name = stringResource(id = R.string.total_quote)
            , value = NumbersUtil.toString(quote)
            , modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewBoughList(){
    val map = mapOf<YearMonth,List<CreditCardBoughtItemDTO>>(
        YearMonth.now() to listOf(
            CreditCardBoughtItemDTO(
                id = 1,
                valueItem = 1_000.00,
                interest = 1.00,
                pendingToPay = 1_000.00,
                capitalValue = 1_000.00,
                interestValue = 100.00,
                quoteValue = 1_100.00,
                settings = 1_000.00,
                monthPaid = 1,
                recurrent = true,
                codeCreditCard = 1,
                nameCreditCard = "Visa",
                nameItem = "Compra 1",
                cutOutDate = LocalDateTime.now(),
                endDate = LocalDateTime.now(),
                createDate = LocalDateTime.now(),
                month = 1,
                kind = KindInterestRateEnum.CREDIT_CARD,
                kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                boughtDate = LocalDateTime.now(),
                tagName = "Casa",
                settingCode = 1
            )),
        YearMonth.now().minusMonths(1) to listOf(CreditCardBoughtItemDTO(
                id = 1,
                valueItem = 1_000.00,
                interest = 1.00,
                pendingToPay = 1_000.00,
                capitalValue = 1_000.00,
                interestValue = 200.00,
                quoteValue = 1_100.00,
                settings = 1_000.00,
                monthPaid = 1,
                recurrent = true,
                codeCreditCard = 1,
                nameCreditCard = "Visa",
                nameItem = "Compra 1",
                cutOutDate = LocalDateTime.now(),
                endDate = LocalDateTime.now(),
                createDate = LocalDateTime.now(),
                month = 1,
                kind = KindInterestRateEnum.CREDIT_CARD,
                kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                boughtDate = LocalDateTime.now().minusMonths(1),
                tagName = "Casa",
            settingCode = 1
            )
        )
    )
    val recap = RecapCreditCardBoughtListDTO(
        currentCapital = 1_000.00,
        currentInterest = 1_000.00,
        totalCapital = 1_000.00,
        totalInterest = 1_000.00,
        quoteCapital = 1_000.00,
        quoteInterest = 1_000.00,
        quoteValue = 1_000.00,
        numQuoteEnd = 1,
        totalQuoteEnd = 1_000.00,
        numNextQuoteEnd = 1,
        totalNextQuoteEnd = 1_000.00,
        num1QuoteBought = 1,
        numBought =  1,
        numQuoteBought = 1,
        numRecurrentBought = 1,
        pendingToPay = 1_000.00
    )

    val data = BoughtCreditCard(
        recap,
        map,
        CreditCardDTO(
            id=0,
        name="test",
    maxQuotes=24,
    cutOffDay=28,
    warningValue=1_000.toBigDecimal(),
    create= LocalDateTime.now(),
     status=true,
     interest1Quote=true,
    interest1NotQuote=true
    )
        ,listOf()
        ,LocalDateTime.now()
    )
    MaterialThemeComposeUI {
        BoughtList(data)
    }
}