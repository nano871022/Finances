package co.com.japl.module.creditcard.views.bought

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.pojo.BoughtCreditCard
import co.com.japl.module.creditcard.controllers.bought.lists.BoughtMonthlyListViewModel
import co.com.japl.module.creditcard.controllers.bought.lists.BoughtViewModel
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.NumbersUtil

import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun BoughList(data: BoughtCreditCard, prefs:Prefs, loader:MutableState<Boolean>, colorPendingValue:Color = Color.Unspecified, boughtCCSvc: IBoughtListPort,simulatorSvc: ISimulatorCreditVariablePort){

    val map = data.group.entries.groupBy { it.key.year }



    LazyColumn {

        items(map.size) {
            if(map.isNotEmpty() && map?.entries?.elementAt(it)?.value?.isNotEmpty()?:false) {
                YearlyBoughtCreditCard(
                    map?.entries?.elementAt(it)?.key!!,
                    map.entries.elementAt(it).value!!,
                    data.creditCard,
                    data.differQuotes,
                    data.cutOff,
                    prefs,
                    loader = loader,
                    colorPendingValue = colorPendingValue,
                    boughtCCSvc = boughtCCSvc,
                    simulatorSvc = simulatorSvc
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }

    }
}

@Composable
private fun YearlyBoughtCreditCard(year:Int, list:List<Map.Entry<YearMonth,List<CreditCardBoughtItemDTO>>>, creditCard:CreditCardDTO, differQuotes:List<DifferInstallmentDTO>, cutOff:LocalDateTime, prefs:Prefs, loader: MutableState<Boolean>, colorPendingValue:Color = Color.Unspecified, boughtCCSvc: IBoughtListPort,simulatorSvc: ISimulatorCreditVariablePort) {


    Surface(onClick={

    }
        ,modifier= Modifier
            .padding(top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp)
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
        Column {

            Row {
                Text(text = "${year}", modifier = Modifier.padding(Dimensions.PADDING_TOP))
                Text(
                    text = NumbersUtil.COPtoString(list.sumOf { it.value.sumOf { it.quoteValue } }),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(Dimensions.PADDING_TOP).weight(1f)
                )
            }

            for(monthly in list) {

                MonthlyBoughtCreditCard(
                    monthly.key,
                    monthly.value,
                    creditCard,
                    differQuotes,
                    cutOff,
                    prefs,
                    loader,
                    colorPendingValue,
                    boughtCCSvc = boughtCCSvc,
                    simulatorSvc = simulatorSvc
                )
            }


        }
    }
}
@Composable
private fun MonthlyBoughtCreditCard(key:YearMonth,list:List<CreditCardBoughtItemDTO>,creditCard:CreditCardDTO,differQuotes:List<DifferInstallmentDTO>,cutOff:LocalDateTime,prefs:Prefs ,loader: MutableState<Boolean>, colorPendingValue:Color = Color.Unspecified, boughtCCSvc: IBoughtListPort,simulatorSvc: ISimulatorCreditVariablePort) {
    val monthlyState = remember {
        BoughtMonthlyListViewModel(key
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
                    RecordBoughtCreditCard(
                        item, creditCard, differQuotes, cutOff, prefs = prefs,
                        loader = loader,
                        colorPendingValue = colorPendingValue,
                        boughtCCSvc = boughtCCSvc,
                        simulatorSvc = simulatorSvc
                    )
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
        BoughList(
            boughtCreditCard, prefs, loader = remember { mutableStateOf(false) },
            colorPendingValue = MaterialTheme.colorScheme.onBackground,
            boughtCCSvc = object: IBoughtListPort{
                override fun getBoughtList(
                    creditCardDTO: CreditCardDTO,
                    cutOff: LocalDateTime,
                    cache: Boolean
                ): CreditCardBoughtListDTO {
                    TODO("Not yet implemented")
                }

                override fun getBoughtPeriodList(
                    idCreditCard: Int,
                    cache: Boolean
                ): List<BoughtCreditCardPeriodDTO> {
                    TODO("Not yet implemented")
                }

                override fun delete(codeBought: Int, cache: Boolean): Boolean {
                    TODO("Not yet implemented")
                }

                override fun restore(
                    codeBought: Int,
                    cache: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun endingRecurrentPayment(
                    codeBought: Int,
                    cutOff: LocalDateTime
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun endingPayment(
                    codeBought: Int,
                    message: String,
                    cutOff: LocalDateTime
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun updateRecurrentValue(
                    codeBought: Int,
                    value: Double,
                    cutOff: LocalDateTime,
                    cache: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun differntInstallment(
                    codeBought: Int,
                    value: Long,
                    cutOff: LocalDateTime,
                    cache: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun clone(codeBought: Int, cache: Boolean): Boolean {
                    TODO("Not yet implemented")
                }

                override fun getAllRecurrent(idCreditCard: Int): List<CreditCardBoughtItemDTO> {
                    TODO("Not yet implemented")
                }

                override fun reactivateRecurrent(codeBought: Int): Boolean {
                    TODO("Not yet implemented")
                }
            },
            simulatorSvc = object: ISimulatorCreditVariablePort{
                override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
                    TODO("Not yet implemented")
                }

                override fun save(
                    dto: SimulatorCreditDTO,
                    cache: Boolean
                ): Long {
                    TODO("Not yet implemented")
                }

                override fun update(
                    dto: SimulatorCreditDTO,
                    cache: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun setSimulation(dto: SimulatorCreditDTO): Boolean {
                    TODO("Not yet implemented")
                }

                override fun getList(): List<SimulatorCreditDTO> {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}