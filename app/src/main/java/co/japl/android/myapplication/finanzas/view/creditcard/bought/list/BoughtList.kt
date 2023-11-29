package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.TaxEnum
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun BoughList(data:BoughtCreditCard){

    LazyColumn {

        items(data.group.size) {
            val key = data.group.keys.toList()[it]
            MonthlyBoughtCreditCard(key,data.group[key]!!,data.creditCard,data.differQuotes,data.cutOff)
        }

    }
}

@Composable
private fun MonthlyBoughtCreditCard(key:YearMonth,list:List<CreditCardBoughtItemDTO>,creditCard:CreditCardDTO,differQuotes:List<DifferInstallmentDTO>,cutOff:LocalDateTime) {
    val monthlyState = remember {
        BoughtMonthlyViewModel(key,list,NumbersUtil.COPtoString(list.sumOf{it.pendingToPay}),NumbersUtil.COPtoString(list.sumOf{it.quoteValue}))
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

            Row(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = key.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                    Modifier.weight(1f)
                )

                Text(text = "Total: \n ${monthlyState.totalBought}", Modifier.weight(1f))

                Text(text = "Total Cuota: \n  ${monthlyState.totalQuote}", Modifier.weight(1f))

            }

            if (monthlyState.state.value)
                for (item in monthlyState.list) {
                   RecordBoughtCreditCard(item,creditCard,differQuotes,cutOff)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordBoughtCreditCard(bought:CreditCardBoughtItemDTO,creditCard: CreditCardDTO, differQuotes:List<DifferInstallmentDTO>,cutOff:LocalDateTime) {
    val context = LocalContext.current
    val application = context.applicationContext
    val view = LocalView.current
    val navController = Navigation.findNavController(view)
    val stateTooltip = remember { PlainTooltipState() }
    val state = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val model = remember {
         BoughtViewModel (application = application as Application)
            .setBought(bought)
            .setCreditCard(creditCard)
            .setDifferQuotes(differQuotes)
            .setNavController(navController)
            .setView(view)
            .setCutOff(cutOff)
    }
    val stateDialogOptionsMore = remember { mutableStateOf(false) }
    when{
        stateDialogOptionsMore.value -> {
            MoreOptionsDialog(model.bought.pendingToPay,model.getMoreOptionsList(),onDismiss = { stateDialogOptionsMore.value = false }, onClick = model::moreOption)
        }
    }

        Card(
            onClick = { state.value = !state.value }, modifier = Modifier
                .padding(top = 5.dp, start = 2.dp, end = 2.dp, bottom = 2.dp)
                .fillMaxWidth()
        , border = BorderStroke(2.dp, getColor(model))
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()
                , verticalAlignment = Alignment.CenterVertically) {
                    if(model.bought.tagName.isNotBlank()) {
                        PlainTooltipBox(tooltip = {
                            Text(text = model.bought.tagName)
                        }, tooltipState = stateTooltip) {
                            IconButton(onClick={scope.launch{stateTooltip.show()}}
                            ) {
                                Icon(   painter = painterResource(id = R.drawable.baseline_location_on_24),
                                contentDescription = model.bought.tagName)
                            }
                        }
                    }
                    Text(
                        text = model.bought.boughtDate.format(DateTimeFormatter.ofPattern("dd")),
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = model.bought.nameItem, modifier = Modifier.weight(2f))
                    Text(text = "${model.bought.monthPaid}/${model.bought.month}")

                    IconButton(onClick = {
                        stateDialogOptionsMore.value = true
                    }) {
                        Icon(painter = painterResource(id = R.drawable.more_vertical)
                        , contentDescription = stringResource(id = R.string.see_more))
                    }

                }
                if (state.value) {
                    Row(modifier = Modifier.fillMaxWidth()
                        , verticalAlignment = Alignment.CenterVertically) {
                        LabelValue(
                            label = R.string.product_value,
                            value = NumbersUtil.COPtoString(model.bought.valueItem),
                            modifier = Modifier.weight(1f)
                        )

                        LabelValue(
                            label = R.string.pending_to_pay,
                            value = NumbersUtil.COPtoString(model.bought.pendingToPay),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()
                        , verticalAlignment = Alignment.CenterVertically) {
                        LabelValue(
                            label = R.string.tax,
                            value = "${
                                (model.bought.interest * 100).toBigDecimal()
                                    .setScale(2, RoundingMode.CEILING)
                            }% ${model.bought.kindOfTax.getName()}",
                            modifier = Modifier.weight(1f)
                        )

                        LabelValue(
                            label = R.string.capital_value,
                            value = NumbersUtil.COPtoString(model.bought.capitalValue),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()
                        , verticalAlignment = Alignment.CenterVertically) {
                        LabelValue(
                            label = R.string.interest_value,
                            value = NumbersUtil.COPtoString(model.bought.interestValue),
                            modifier = Modifier.weight(1f)
                        )

                        LabelValue(
                            label = R.string.quote_value,
                            value = NumbersUtil.COPtoString(model.bought.quoteValue),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
}

@Composable
private fun LabelValue(@StringRes label:Int, value:String,modifier:Modifier){
    Text(text = stringResource(id = label), modifier = modifier)
    Text(text = value, modifier = modifier)
}

@Composable
fun getColor(model: BoughtViewModel): Color {
    return model.bought.recurrent.takeIf { it }?.let {
        MaterialTheme.colorScheme.primaryContainer
    }?:model.bought.settingCode.takeIf { it > 0 }?.let{
        MaterialTheme.colorScheme.onPrimary
    }?: model.bought.monthPaid.takeIf { it > 0 }?.let {
        MaterialTheme.colorScheme.onPrimaryContainer
    }?:MaterialTheme.colorScheme.surfaceVariant
}

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
        kind=TaxEnum.CREDIT_CARD,
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
        kind=TaxEnum.CREDIT_CARD,
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
        kind=TaxEnum.CREDIT_CARD,
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
        kind=TaxEnum.CREDIT_CARD,
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
    MaterialThemeComposeUI {
        BoughList(boughtCreditCard)
    }
}