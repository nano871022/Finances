package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun BoughList(data:BoughtCreditCard,prefs:Prefs,loader:MutableState<Boolean>, colorPendingValue:Color = Color.Unspecified){

    LazyColumn {

        items(data.group.size) {
            val key = data.group.keys.toList()[it]
            MonthlyBoughtCreditCard(key,data.group[key]!!,data.creditCard,data.differQuotes,data.cutOff,prefs,loader=loader, colorPendingValue = colorPendingValue)
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }

    }
}

@Composable
private fun MonthlyBoughtCreditCard(key:YearMonth,list:List<CreditCardBoughtItemDTO>,creditCard:CreditCardDTO,differQuotes:List<DifferInstallmentDTO>,cutOff:LocalDateTime,prefs:Prefs ,loader: MutableState<Boolean>, colorPendingValue:Color = Color.Unspecified) {
    val monthlyState = remember {
        BoughtMonthlyViewModel(key
            ,list
            ,NumbersUtil.COPtoString(list.sumOf{it.pendingToPay})
            ,NumbersUtil.COPtoString(list.sumOf{it.quoteValue})
            ,NumbersUtil.COPtoString(list.sumOf{it.interestValue}))
    }

    Surface(onClick={
        monthlyState.state.value = !monthlyState.state.value
    }
    ,modifier= Modifier
            .padding(top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp)
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
        Column {

            HeaderMonthly(key = key, list = list, monthlyState = monthlyState)

            if (monthlyState.state.value) {
                for (item in monthlyState.list) {
                    RecordBoughtCreditCard(item, creditCard, differQuotes, cutOff, prefs = prefs,loader=loader, colorPendingValue = colorPendingValue)
                }
            }
        }
    }
}

@Composable
fun getColor(model: BoughtViewModel): Color {
    return model.bought.recurrent.takeIf { it }?.let {
        Color.Blue
    }?:model.bought.settingCode.takeIf { it > 0 }?.let{
        Color.Cyan
    }?:model.bought.kind.takeIf { it == KindInterestRateEnum.WALLET_BUY }?.let{
        Color.Red
    }?:model.bought.kind.takeIf { it == KindInterestRateEnum.CASH_ADVANCE }?.let{
        Color.Yellow
    }?: model.bought.monthPaid.takeIf { it > 1 }?.let {
        Color.Gray
    }?:Color.Transparent
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun BoughListPreview(){
    val listCreditCard = arrayListOf(CreditCardBoughtItemDTO(
        codeCreditCard = 1,
        nameCreditCard = "Visa",
        nameItem = "Compra 1",
        valueItem = 1000.0,
        interest=3.0,
        month = 1,
        boughtDate = LocalDateTime.now(),
        cutOutDate = LocalDateTime.now(),
        createDate = LocalDateTime.now(),
        endDate = LocalDateTime.now(),
        id = 1,
        recurrent = true,
        kind=KindInterestRateEnum.CREDIT_CARD,
        kindOfTax=KindOfTaxEnum.MONTHLY_EFFECTIVE,
        capitalValue = 1000.0,
        interestValue = 300.0,
        quoteValue = 1300.0,
        settings = 0.0,
        monthPaid = 0,
        pendingToPay = 0.0,
        tagName = "Casa",
        settingCode = 1
    ),CreditCardBoughtItemDTO(
        codeCreditCard = 1,
        nameCreditCard = "Visa",
        nameItem = "Compra 1",
        valueItem = 1000.0,
        interest=3.0,
        month = 1,
        boughtDate = LocalDateTime.now().minusDays(1),
        cutOutDate = LocalDateTime.now(),
        createDate = LocalDateTime.now(),
        endDate = LocalDateTime.now(),
        id = 1,
        recurrent = false,
        kind=KindInterestRateEnum.CREDIT_CARD,
        kindOfTax=KindOfTaxEnum.MONTHLY_EFFECTIVE,
        capitalValue = 1000.0,
        interestValue = 300.0,
        quoteValue = 1300.0,
        settings = 0.0,
        monthPaid = 0,
        pendingToPay = 0.0,
        tagName = "Casa",
        settingCode = 1
    ),CreditCardBoughtItemDTO(
        codeCreditCard = 1,
        nameCreditCard = "Visa",
        nameItem = "Compra 1",
        valueItem = 1000.0,
        interest=3.0,
        month = 1,
        boughtDate = LocalDateTime.now().minusMonths(1).minusDays(2),
        cutOutDate = LocalDateTime.now(),
        createDate = LocalDateTime.now(),
        endDate = LocalDateTime.now(),
        id = 1,
        recurrent = false,
        kind=KindInterestRateEnum.CREDIT_CARD,
        kindOfTax=KindOfTaxEnum.MONTHLY_EFFECTIVE,
        capitalValue = 1000.0,
        interestValue = 300.0,
        quoteValue = 1300.0,
        settings = 0.0,
        monthPaid = 0,
        pendingToPay = 0.0,
        tagName = "Casa",
        settingCode = 1
    ),CreditCardBoughtItemDTO(
        codeCreditCard = 1,
        nameCreditCard = "Visa",
        nameItem = "Compra 1",
        valueItem = 1000.0,
        interest=3.0,
        month = 1,
        boughtDate = LocalDateTime.now().minusMonths(2),
        cutOutDate = LocalDateTime.now(),
        createDate = LocalDateTime.now(),
        endDate = LocalDateTime.now(),
        id = 1,
        recurrent = true,
        kind=KindInterestRateEnum.CREDIT_CARD,
        kindOfTax=KindOfTaxEnum.MONTHLY_EFFECTIVE,
        capitalValue = 1000.0,
        interestValue = 300.0,
        quoteValue = 1300.0,
        settings = 0.0,
        monthPaid = 0,
        pendingToPay = 0.0,
        tagName = "Casa",
        settingCode = 1
    ))

    val group = listCreditCard.sortedByDescending { it.boughtDate }.groupBy { YearMonth.of(it.boughtDate.year,it.boughtDate.month) }
    val boughtCreditCard = BoughtCreditCard(
        recap = RecapCreditCardBoughtListDTO(

        ),
        group = group,
        creditCard = CreditCardDTO(
            id = 1,
            name = "Visa",
            maxQuotes=1,
             cutOffDay=29,
         warningValue= 1_000.toBigDecimal(),
     create= LocalDateTime.now(),
     status= false,
     interest1Quote= false,
    interest1NotQuote= false
        ),
        differQuotes = listOf(),
        cutOff = LocalDateTime.now()
    )
    val prefs = Prefs(LocalContext.current)
    MaterialThemeComposeUI {
        BoughList(boughtCreditCard,prefs, loader = remember{ mutableStateOf(false) })
    }
}