package co.japl.android.myapplication.finanzas.view.creditcard.bought

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
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
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.TaxEnum
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.finanzas.view.components.FieldView
import co.japl.android.myapplication.finanzas.view.creditcard.bought.list.BoughList
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun BoughtList(data:BoughtCreditCard){
    val popupState = remember { mutableStateOf(false) }

    Column(modifier=Modifier.fillMaxWidth()) {
        Header(data.recap.totalCapital,data.recap.totalInterest,data.recap.quoteValue)

        OutlinedButton(onClick = { popupState.value = !popupState.value }
            , border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
        ,modifier = Modifier.padding(top=5.dp,start=10.dp, end=10.dp, bottom = 5.dp)) {
            Text(text = stringResource(id = R.string.see_more),color=MaterialTheme.colorScheme.onPrimaryContainer)
        }

        BoughList(data = data)
    }
    Popup(data.recap,popupState = popupState)

}

@Composable
private fun Popup(recap: RecapCreditCardBoughtListDTO,popupState: MutableState<Boolean>){

    co.japl.android.myapplication.finanzas.view.components.Popup(R.string.recap_bought_cc,popupState) {

        Row{
            FieldView(name = R.string.total_bougth_item, value = "${recap.numBought}", modifier = Modifier.weight(1f), isMoney = false)
            FieldView(name = R.string.recurrent_item, value = "${recap.numRecurrentBought}", modifier = Modifier.weight(1f), isMoney = false)
        }

        Row{
            FieldView(name = R.string.quote_item, value = "${recap.numQuoteBought}", modifier = Modifier.weight(1f), isMoney = false)
            FieldView(name = R.string.quote_1_item, value = "${recap.num1QuoteBought}", modifier = Modifier.weight(1f), isMoney = false)
        }

        Row{
            FieldView(name = R.string.pending_to_pay, value = NumbersUtil.toString(recap.pendingToPay), modifier = Modifier.weight(1f))
        }
        Row{
            FieldView(name = R.string.current_capital_value_short, value = NumbersUtil.toString(recap.currentCapital), modifier = Modifier.weight(1f))
            FieldView(name = R.string.current_interest_value_short, value = NumbersUtil.toString(recap.currentInterest), modifier = Modifier.weight(1f))
        }
        Row{
            FieldView(name = R.string.quote_capital_value_short, value = NumbersUtil.toString(recap.quoteCapital), modifier = Modifier.weight(1f))
            FieldView(name = R.string.quote_interest_value_short, value = NumbersUtil.toString(recap.quoteInterest), modifier = Modifier.weight(1f))
        }
        Row{
            FieldView(name = R.string.capital_value_short, value = NumbersUtil.toString(recap.totalCapital), modifier = Modifier.weight(1f))
            FieldView(name = R.string.interest_value_short, value = NumbersUtil.toString(recap.totalInterest), modifier = Modifier.weight(1f))
        }
        Row{
            FieldView(name = R.string.quote_value, value = NumbersUtil.toString(recap.quoteValue), modifier = Modifier.weight(1f))
        }
        Row{
            FieldView(name = R.string.num_quote_end, value = "${recap.numQuoteEnd}", modifier = Modifier.weight(1f), isMoney = false)
            FieldView(name = R.string.total_quote_end, value = NumbersUtil.toString(recap.totalQuoteEnd), modifier = Modifier.weight(1f))
        }
        Row{
            FieldView(name = R.string.num_quote_next_end, value = "${recap.numNextQuoteEnd}", modifier = Modifier.weight(1f), isMoney = false)
            FieldView(name = R.string.total_quote_next_end, value = NumbersUtil.toString(recap.totalNextQuoteEnd), modifier = Modifier.weight(1f))
        }
        
    }
}

@Composable
private fun Header(capital:Double, interest:Double, quote:Double){
    Row(modifier=Modifier.fillMaxWidth()) {

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
                kind = TaxEnum.CREDIT_CARD,
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
                kind = TaxEnum.CREDIT_CARD,
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