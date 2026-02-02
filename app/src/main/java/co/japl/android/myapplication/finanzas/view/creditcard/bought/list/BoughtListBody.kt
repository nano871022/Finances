package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordBoughtCreditCard(bought: CreditCardBoughtItemDTO, creditCard: CreditCardDTO, differQuotes:List<DifferInstallmentDTO>, cutOff: LocalDateTime, view:View= LocalView.current, navController: NavController? = Navigation.findNavController(view),prefs:Prefs,loader:MutableState<Boolean>, colorPendingValue:Color = Color.Unspecified) {
    val context = LocalContext.current
    val application = context.applicationContext

    val state = remember { mutableStateOf(false) }
    val model = remember {
        BoughtViewModel (application = application as Application,prefs, loader = loader)
            .setBought(bought)
            .setCreditCard(creditCard)
            .setDifferQuotes(differQuotes)
            .setNavController(navController!!)
            .setView(view)
            .setCutOff(cutOff)
    }


    Card(
        onClick = { state.value = !state.value }
        , modifier = Modifier
            .padding(top = 1.dp, start = 2.dp, end = 2.dp, bottom = 2.dp)
            .fillMaxWidth()
        , border = BorderStroke(2.dp, getColor(model))
    ) {
        Column(
            modifier = Modifier
                .padding(start=5.dp,end=5.dp, bottom=3.dp)
        ) {
            MainRow(model = model)
            if (state.value) {
                Content(model, colorPendingValue = colorPendingValue)
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun Content(model:BoughtViewModel, colorPendingValue:Color = Color.Unspecified){
    BoxWithConstraints {
        if(WindowWidthSize.MEDIUM.isEqualTo(maxWidth)){
            ContentCompact(model, colorPendingValue = colorPendingValue)
        }else{
            ContentLarge(model, colorPendingValue = colorPendingValue)
        }
    }
}

@Composable
private fun ContentLarge(model: BoughtViewModel,paddingEnd:Dp=10.dp, colorPendingValue:Color = Color.Unspecified){
    Row (verticalAlignment = Alignment.CenterVertically) {
        LabelValue(
            label = R.string.product_value_short,
            value = NumbersUtil.COPtoString(model.bought.valueItem),
            modifier = Modifier
                .weight(1f)
                .padding(end = paddingEnd)
        )

        LabelValue(
            label = R.string.pending_to_pay_short,
            value = NumbersUtil.COPtoString(model.bought.pendingToPay),
            modifier = Modifier
                .weight(1f)
                .padding(end = paddingEnd),
            color = colorPendingValue
        )

        LabelValue(
            label = R.string.capital_value_short,
            value = NumbersUtil.COPtoString(model.bought.capitalValue),
            modifier = Modifier
                .weight(1f)
                .padding(end = paddingEnd)
        )

        LabelValue(
            label = R.string.interest_value_short,
            value = NumbersUtil.COPtoString(model.bought.interestValue),
            modifier = Modifier
                .weight(1f)
                .padding(end = paddingEnd)
        )
    }
}


@Composable
private fun ContentCompact(model:BoughtViewModel,paddingEnd:Dp=10.dp, colorPendingValue:Color = Color.Unspecified){
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            LabelValue(
                label = R.string.product_value,
                value = NumbersUtil.COPtoString(model.bought.valueItem),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = paddingEnd)
            )

            LabelValue(
                label = R.string.pending_to_pay,
                value = NumbersUtil.COPtoString(model.bought.pendingToPay),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = paddingEnd),
                color = colorPendingValue
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            LabelValue(
                label = R.string.tax,
                value = "${
                    (model.bought.interest * 100).toBigDecimal()
                        .setScale(2, RoundingMode.CEILING)
                }% ${model.bought.kindOfTax.getName()}",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = paddingEnd)
            )

            LabelValue(
                label = R.string.capital_value,
                value = NumbersUtil.COPtoString(model.bought.capitalValue),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = paddingEnd)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            LabelValue(
                label = R.string.interest_value,
                value = NumbersUtil.COPtoString(model.bought.interestValue),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = paddingEnd)
            )

            LabelValue(
                label = R.string.quote_value,
                value = NumbersUtil.COPtoString(model.bought.quoteValue),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = paddingEnd)
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainRow(model:BoughtViewModel){
    val tooltip = rememberTooltipState()
    val scope = rememberCoroutineScope()
    val stateDialogOptionsMore = remember { mutableStateOf(false) }
    when{
        stateDialogOptionsMore.value -> {
            var rediferirValue = model.bought.pendingToPay //+ model.bought.capitalValue
            if(rediferirValue > model.bought.valueItem){
               rediferirValue =model.bought.valueItem
            }
            MoreOptionsDialog(rediferirValue,
                model.bought.recurrent,
                model.bought.interest,
                model.getMoreOptionsList(),
                onDismiss = { stateDialogOptionsMore.value = false },
                onClick = model::moreOption)
        }
    }
    Row(modifier = Modifier.fillMaxWidth()
        , verticalAlignment = Alignment.CenterVertically) {
        if(model.bought.tagName.isNotBlank()) {
            TooltipBox(tooltip = {
                Text(text = model.bought.tagName)
            }, state = tooltip
            , positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()) {
                IconButton(onClick={scope.launch{tooltip.show()}}
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

        BoxWithConstraints {
            val maxWidth = maxWidth
            Row {
                when(WindowWidthSize.fromDp(maxWidth)){
                    WindowWidthSize.COMPACT->MainRowCompact(model = model)
                    WindowWidthSize.MEDIUM->MainRowMedium(model = model)
                    else->MainRowLarge(model  )
                }
            }
        }

        IconButton(onClick = {
            stateDialogOptionsMore.value = true
        }) {
            Icon(painter = painterResource(id = R.drawable.more_vertical)
                , contentDescription = stringResource(id = R.string.see_more)
            )
        }

    }
}

@Composable
private fun RowScope.MainRowCompact(model:BoughtViewModel){
    Text(text = "${model.bought.monthPaid}/${model.bought.month}")
}

@Composable
private fun RowScope.MainRowMedium(model:BoughtViewModel){
    Log.w(javaClass.name,"=== MainRowMedium ")
    Text(text = "${model.bought.monthPaid}/${model.bought.month}")

    Text(text = NumbersUtil.COPtoString(model.bought.quoteValue),modifier=Modifier.padding(start=5.dp))
}

@Composable
private fun RowScope.MainRowLarge(model:BoughtViewModel){

    Text(text = "${(model.bought.interest * 100).toBigDecimal().setScale(2, RoundingMode.CEILING)}% ${model.bought.kindOfTax.getName()}",modifier=Modifier.padding(end=10.dp))

    Text(text = "${model.bought.monthPaid}/${model.bought.month}")

    Text(text = stringResource(id = R.string.quote_value),modifier=Modifier.padding(start=10.dp,end=3.dp))

    Text(text = NumbersUtil.COPtoString(model.bought.quoteValue))
}

@Composable
private fun LabelValue(@StringRes label:Int, value:String, modifier:Modifier, color:Color = Color.Unspecified){
    Text(text = stringResource(id = label), modifier = modifier)
    Text(text = value, modifier = modifier, textAlign = TextAlign.End, color = color)
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview(){

    val prefs = Prefs(LocalContext.current)
    MaterialThemeComposeUI {
        RecordBoughtCreditCard(CreditCardBoughtItemDTO(
            id = 0,
            nameItem = "Credito 1",
            valueItem = 10000.0,
            interest = 3.0,
            month = 1,
            interestValue = 3000.0,
            quoteValue = 13000.0,
            capitalValue = 10000.0,
            settings = 0.0,
            monthPaid = 0,
            pendingToPay = 0.0,
            tagName = "Casa",
            settingCode = 1,
            boughtDate = LocalDateTime.now(),
            cutOutDate = LocalDateTime.now(),
            createDate = LocalDateTime.now(),
            endDate = LocalDateTime.now(),
            codeCreditCard = 1,
            nameCreditCard = "Visa",
            recurrent = true,
            kind = KindInterestRateEnum.CREDIT_CARD,
            kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,

        ),CreditCardDTO(
            cutOffDay = 1,
            id = 1,
            name = "Visa",
            create = LocalDateTime.now(),
            interest1Quote = true,
            interest1NotQuote = false,
            maxQuotes = 0,
            warningValue = 0.toBigDecimal(),
            status = true
        ), listOf(), LocalDateTime.now(), LocalView.current,null,prefs, remember{mutableStateOf(false)}
            , colorPendingValue = Color(0xFFFFA500)
        )
    }
}