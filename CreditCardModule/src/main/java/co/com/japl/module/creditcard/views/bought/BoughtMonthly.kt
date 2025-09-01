package co.com.japl.module.creditcard.views.bought

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.lists.BoughtMonthlyViewModel
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.HelpWikiButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVertical
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVerticalAndPaddingRightSpace
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndPaddintRightSpace
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.graphs.interfaces.IGraph
import co.japl.android.graphs.pieceofpie.PieceOfPie
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun BoughtMonthly(viewModel:BoughtMonthlyViewModel?=null) {
    var loaderStatus by remember {viewModel!!.loader}
    var progressStatus by remember {viewModel!!.progress}

    CoroutineScope(Dispatchers.IO).launch{
        viewModel?.mainCreditCard()
    }


       Column(modifier= Modifier
           .fillMaxHeight()
           ) {

            Header(creditCard = viewModel!!.creditCard,
                list = viewModel!!.creditCardList,
                changeSelect = {
                    it?.let{
                        viewModel.creditCard.value = it.second
                        viewModel.creditCardCode.value = it.first
                        CoroutineScope(Dispatchers.IO).launch{
                            viewModel?.main()
                        }
                    }
                })

           if(loaderStatus.not() ) {
               LinearProgressIndicator(
                   progress = { progressStatus },
                   modifier = Modifier.fillMaxWidth(),
               )
           }else{
                Loaded(viewModel = viewModel)
           }
    }
}

@Composable
private fun Loaded(viewModel:BoughtMonthlyViewModel){
    val modeWindow = LocalConfiguration.current
    var cutOffDay by remember{ viewModel!!.cutOff }
    var daysLeftCutoff by remember{ viewModel!!.daysLeftCutOff }
    var totalValue by remember{ viewModel!!.totalValue }
    var verticalScrollState = rememberScrollState()
    Scaffold(
        floatingActionButton = {
            viewModel?.let {
                FloatButtons(it)
            }
        },
        modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(it)
                .verticalScroll(verticalScrollState)
        ) {
            BoxWithConstraints {

                when (WindowWidthSize.fromDp(maxWidth)) {
                    WindowWidthSize.COMPACT ->
                        Body(
                            cutOffDay = cutOffDay.toLocalDate(),
                            daysLeftCutoff = daysLeftCutoff,
                            totalValue = totalValue
                        )

                    WindowWidthSize.LARGES ->
                        BodyLarge(
                            cutOffDay = cutOffDay.toLocalDate(),
                            daysLeftCutoff = daysLeftCutoff,
                            totalValue = totalValue
                        )

                    else ->
                        BodyMedium(
                            cutOffDay = cutOffDay.toLocalDate(),
                            daysLeftCutoff = daysLeftCutoff,
                            totalValue = totalValue
                        )
                }
            }

                BoxWithConstraints {
                    Log.d("TEST","MAXWidht: ${maxWidth}")
                    when (WindowWidthSize.fromDp(maxWidth)) {

                        WindowWidthSize.COMPACT -> Recap(viewModel = viewModel)
                        WindowWidthSize.LARGES -> RecapLarge(viewModel = viewModel)
                        else -> RecapMedium(viewModel = viewModel)
                    }

                }


            Graphs(list = viewModel.graphList,listPeriod = viewModel.graphListPeriod)
        }
    }
}

@Composable
private fun FloatButtons(viewModel:BoughtMonthlyViewModel){
    var showList by remember { viewModel.showList }
    var showPeriod by remember { viewModel.showPeriodList }
    var showAddBought by remember { viewModel.showBought }
    var showAddAdvance by remember { viewModel.showAdvance }
    var showAddBoughtWallet by remember { viewModel.showWallet }
    var showAddCreditRate by remember { viewModel.showCreditRate }

    Column {
        if(showList) {
            FloatingActionButton(onClick = { viewModel.goToPaidList() },
                elevation=FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                Icon(
                    imageVector = Icons.Rounded.RemoveRedEye,
                    contentDescription = stringResource(
                        id = R.string.paids
                    )
                )
            }
        }

        if(showPeriod) {
            FloatingActionButton(onClick = { viewModel.goToPeriodList() },
                elevation=FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = stringResource(
                        id = R.string.periods
                    )
                )
            }
        }

        if(showAddBought) {
            FloatingActionButton(onClick = { viewModel.goToAddBought() },
                elevation=FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                Icon(
                    imageVector = Icons.Rounded.AddCircleOutline,
                    contentDescription = stringResource(
                        id = R.string.add_bought
                    )
                )
            }
        }

        if(showAddAdvance) {
            FloatingActionButton(onClick = { viewModel.goToAddAdvance() },
            elevation=FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = stringResource(
                        id = R.string.add_advance
                    )
                )
            }
        }

        if(showAddBoughtWallet) {
            FloatingActionButton(onClick = { viewModel.goToAddBoughtWallet() },
                elevation=FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(
                        id = R.string.add_bought_wallet
                    )
                )
            }
        }

        if(showAddCreditRate) {
            FloatingActionButton(onClick = { viewModel.goToAddCreditRate() },
                elevation=FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                Icon(
                    imageVector = Icons.Rounded.Percent,
                    contentDescription = stringResource(
                        id = R.string.add_credit_rate
                    )
                )
            }
        }
    }
}

@Composable
private fun Header(creditCard:MutableState<String>,list:List<Pair<Int,String>>?, changeSelect: (Pair<Int, String>?) -> Unit) {
    var valueState by remember { creditCard }


    Row {
        FieldSelect(
            title = stringResource(id = R.string.credit_card),
            value = valueState,
            list = list,
            modifier = Modifier
                .padding(bottom = Dimensions.PADDING_SHORT)
                .weight(1f),
            callable = changeSelect
        )

        HelpWikiButton(wikiUrl = R.string.wiki_bought_url,
            descriptionContent = R.string.help_bought_description,
            tint = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun Body(cutOffDay:LocalDate,daysLeftCutoff:Int,totalValue: Double) {
    Column(modifier=Modifier.padding(bottom=Dimensions.PADDING_SHORT)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT)) {
            FieldView(
                name =  R.string.cut_off_day,
                value = DateUtils.localDateToStringDate(cutOffDay),
                isMoney = false,
                modifier = Weight1fAndAlightCenterVerticalAndPaddingRightSpace()
            )

            FieldView(
                name = R.string.days_left_until_cutoff_date,
                isMoney = false,
                value = daysLeftCutoff.toString(),
                modifier = Weight1f()
            )
        }

        FieldView(
            name =  R.string.total_quote,
            value = NumbersUtil.toString(totalValue),
            alignment = Alignment.Center,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BodyMedium(cutOffDay:LocalDate,daysLeftCutoff:Int,totalValue: Double) {
    Column(modifier=Modifier.padding(bottom=Dimensions.PADDING_SHORT)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT)) {
            FieldView(
                name = R.string.total_quote,
                value = NumbersUtil.toString(totalValue),
                alignment = Alignment.Center,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
            Column(modifier=Modifier.weight(1f)) {

                FieldView(
                    name = R.string.cut_off_day,
                    value = DateUtils.localDateToStringDate(cutOffDay),
                    isMoney = false,
                    modifier = Modifier.fillMaxWidth()
                )

                FieldView(
                    name = R.string.days_left_until_cutoff_date,
                    isMoney = false,
                    value = daysLeftCutoff.toString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }


        }
    }
}

@Composable
private fun BodyLarge(cutOffDay:LocalDate,daysLeftCutoff:Int,totalValue: Double) {
    Column(modifier=Modifier.padding(bottom=Dimensions.PADDING_SHORT)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT)) {
            FieldView(
                name = R.string.total_quote,
                value = NumbersUtil.toString(totalValue),
                alignment = Alignment.Center,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                modifier = Modifier.weight(2f)
            )

                FieldView(
                    name = R.string.cut_off_day,
                    value = DateUtils.localDateToStringDate(cutOffDay),
                    isMoney = false,
                    modifier = Weight1f().align(alignment = Alignment.CenterVertically)
                )

                FieldView(
                    name = R.string.days_left_until_cutoff_date,
                    isMoney = false,
                    value = daysLeftCutoff.toString(),
                    modifier = Weight1f().align(alignment = Alignment.CenterVertically)
                )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun Graphs(list:List<Pair<String,Double>>,listPeriod:List<Pair<String,Double>>) {

    Carousel(size = 2) {

        if(it == 0) {
            GraphList(list = list)
        }else {
            GraphMonth(list = listPeriod)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GraphList(list:List<Pair<String,Double>>){
    val context = LocalContext.current
    val piecePie : IGraph = PieceOfPie(context)
    piecePie.clear()
    var invalidations by remember { mutableIntStateOf(0) }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
        .pointerInteropFilter {
            when (it.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    piecePie.validateTouch(it.x, it.y)
                    invalidations++
                    true
                }

                else -> false
            }
        }) {
        invalidations.let {
            piecePie.drawBackground()

            for (values in list) {
                piecePie.addPiece(title = values.first, value = values.second)
            }

            piecePie.drawing(this.drawContext.canvas.nativeCanvas)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GraphMonth(list:List<Pair<String,Double>>){
    val context = LocalContext.current
    val piecePie : IGraph = PieceOfPie(context)
    piecePie.clear()
    var invalidations by remember { mutableIntStateOf(0) }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
        .pointerInteropFilter {
            when (it.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    piecePie.validateTouch(it.x, it.y)
                    invalidations++
                    true
                }

                else -> false
            }
        }) {
        invalidations.let {
            piecePie.drawBackground()

            for (values in list) {
                piecePie.addPiece(title = values.first, value = values.second)
            }

            piecePie.drawing(this.drawContext.canvas.nativeCanvas)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Recap(viewModel:BoughtMonthlyViewModel){
    var capitalQuotesState by remember {viewModel.capitalValue}
    var interestValueState by remember {viewModel.interestValue}
    var warningValueState by remember {viewModel.warningValue}
    var toQuotesState by remember {viewModel.toQuotes}
    var toOneQuoteState by remember {viewModel.toOneQuote}
    var limitValueState by remember {viewModel.limitvalue}
    var lastMonthPaidState by remember {viewModel.lastMonthPaid}
    var totalValueLastMonthState by remember {viewModel.totalValueLastMonth}
    var capitalWithoutQuotesLastMonthState by remember {viewModel.capitalWithoutQuotesLastMonth}
    var capitalQuotesLastMonthState by remember {viewModel.capitalQuotesLastMonth}
    var interestValueLastMonthState by remember {viewModel.interestValueLastMonth}

    Carousel(size = 3) {
        Surface (modifier=Modifier.padding(bottom=Dimensions.PADDING_BOTTOM_CAROUSEL_SPACE)){

            if (it == 0) {
                RecapCurrentMoney(
                    capitalValue = capitalQuotesState,
                    interestValue = interestValueState,
                    warningValue = warningValueState
                )
            } else if (it == 1) {

                RecapCurrentQuotesAndLimit(toQuotes = toQuotesState,
                    toOneQuote = toOneQuoteState,
                    limitvalue = limitValueState)
            } else {
                RecapLastMonth(
                    lastMonthPaid = lastMonthPaidState.toLocalDate(),
                    totalValue = totalValueLastMonthState,
                    capitalWithoutQuotes = capitalWithoutQuotesLastMonthState,
                    capitalQuotes = capitalQuotesLastMonthState,
                    interestValue = interestValueLastMonthState
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecapMedium(viewModel:BoughtMonthlyViewModel){
    var capitalQuotesState by remember {viewModel.capitalValue}
    var interestValueState by remember {viewModel.interestValue}
    var warningValueState by remember {viewModel.warningValue}
    var toQuotesState by remember {viewModel.toQuotes}
    var toOneQuoteState by remember {viewModel.toOneQuote}
    var limitValueState by remember {viewModel.limitvalue}
    var lastMonthPaidState by remember {viewModel.lastMonthPaid}
    var totalValueLastMonthState by remember {viewModel.totalValueLastMonth}
    var capitalWithoutQuotesLastMonthState by remember {viewModel.capitalWithoutQuotesLastMonth}
    var capitalQuotesLastMonthState by remember {viewModel.capitalQuotesLastMonth}
    var interestValueLastMonthState by remember {viewModel.interestValueLastMonth}

    Carousel(size = 2) {
        Surface (modifier=Modifier.padding(bottom=Dimensions.PADDING_BOTTOM_CAROUSEL_SPACE)){

            if (it == 0) {
                Row {
                    RecapCurrentMoney(
                        capitalValue = capitalQuotesState,
                        interestValue = interestValueState,
                        warningValue = warningValueState,
                        modifier = Modifier.weight(2f)
                    )

                    RecapCurrentQuotesAndLimit(
                        toQuotes = toQuotesState,
                        toOneQuote = toOneQuoteState,
                        limitvalue = limitValueState,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                RecapLastMonthMedium(
                    lastMonthPaid = lastMonthPaidState.toLocalDate(),
                    totalValue = totalValueLastMonthState,
                    capitalWithoutQuotes = capitalWithoutQuotesLastMonthState,
                    capitalQuotes = capitalQuotesLastMonthState,
                    interestValue = interestValueLastMonthState
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecapLarge(viewModel:BoughtMonthlyViewModel){
    var capitalQuotesState by remember {viewModel.capitalValue}
    var interestValueState by remember {viewModel.interestValue}
    var warningValueState by remember {viewModel.warningValue}
    var toQuotesState by remember {viewModel.toQuotes}
    var toOneQuoteState by remember {viewModel.toOneQuote}
    var limitValueState by remember {viewModel.limitvalue}
    var lastMonthPaidState by remember {viewModel.lastMonthPaid}
    var totalValueLastMonthState by remember {viewModel.totalValueLastMonth}
    var capitalWithoutQuotesLastMonthState by remember {viewModel.capitalWithoutQuotesLastMonth}
    var capitalQuotesLastMonthState by remember {viewModel.capitalQuotesLastMonth}
    var interestValueLastMonthState by remember {viewModel.interestValueLastMonth}

    Carousel(size = 1) {
        Surface (modifier=Modifier.padding(bottom=Dimensions.PADDING_BOTTOM_CAROUSEL_SPACE)) {

            if (it == 0) {
                Row {
                    RecapCurrentMoney(
                        capitalValue = capitalQuotesState,
                        interestValue = interestValueState,
                        warningValue = warningValueState,
                        modifier = Modifier.weight(2f)
                    )

                    RecapCurrentQuotesAndLimit(
                        toQuotes = toQuotesState,
                        toOneQuote = toOneQuoteState,
                        limitvalue = limitValueState,
                        modifier = Modifier.weight(1f)
                    )

                    RecapLastMonthMedium(
                        lastMonthPaid = lastMonthPaidState.toLocalDate(),
                        totalValue = totalValueLastMonthState,
                        capitalWithoutQuotes = capitalWithoutQuotesLastMonthState,
                        capitalQuotes = capitalQuotesLastMonthState,
                        interestValue = interestValueLastMonthState,
                        modifier = Modifier.weight(2f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecapCurrentMoney(capitalValue:Double,interestValue:Double,warningValue:Double,modifier:Modifier=Modifier){
    Card(modifier=modifier.padding(Dimensions.PADDING_SHORT)){
        Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            Row(modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
                FieldView(
                    name = R.string.capital_value,
                    value = NumbersUtil.toString(capitalValue),
                    modifier = Weight1fAndPaddintRightSpace()
                )

                FieldView(
                    name = R.string.interest_value,
                    value = NumbersUtil.toString(interestValue),
                    modifier = Weight1f()
                )
            }

            FieldView(
                name = R.string.warning_quote_value,
                value = NumbersUtil.toString(warningValue),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RecapCurrentQuotesAndLimit(toQuotes:Int,toOneQuote:Int,limitvalue:Double,modifier: Modifier=Modifier) {
    Card(modifier=modifier.padding(Dimensions.PADDING_SHORT)){
    Column (modifier=Modifier.padding( Dimensions.PADDING_SHORT)) {
        Row(modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
            FieldView(
                name = R.string.to_quotes,
                value = toQuotes.toString(),
                isMoney = false,
                modifier = Weight1fAndPaddintRightSpace()
            )

            FieldView(
                name = R.string.to_one_quote,
                value = toOneQuote.toString(),
                isMoney = false,
                modifier = Weight1f()
            )
        }

        FieldView(
            name = R.string.limit,
            value = NumbersUtil.toString(limitvalue),
            color=if(limitvalue<0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

    }   }
}

@Composable
private fun RecapLastMonth(lastMonthPaid:LocalDate,
                           totalValue:Double,
                           capitalWithoutQuotes:Double,
                           capitalQuotes:Double,
                           interestValue:Double) {
    Card (
        modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ) {


        Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            Row(modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
                FieldView(
                    name = R.string.last_month_paid,
                    value = DateUtils.localDateToStringDate(lastMonthPaid),
                    isMoney = false,
                    modifier = Weight1fAndPaddintRightSpace()
                )

                FieldView(
                    name = R.string.total_value,
                    value = NumbersUtil.toString(totalValue),
                    modifier = Weight1f()
                )
            }
            Row {
                FieldView(
                    name = R.string.capital_without_quotes,
                    value = NumbersUtil.toString(capitalWithoutQuotes),
                    modifier = Weight1fAndPaddintRightSpace()
                )

                FieldView(
                    name = R.string.capital_quotes,
                    value = NumbersUtil.toString(capitalQuotes),
                    modifier = Weight1fAndPaddintRightSpace()
                )
            }
                FieldView(
                    name = R.string.interest_value,
                    value = NumbersUtil.toString(interestValue),
                    modifier = Modifier.fillMaxWidth()
                )

        }
    }

}

@Composable
private fun RecapLastMonthMedium(lastMonthPaid:LocalDate,
                           totalValue:Double,
                           capitalWithoutQuotes:Double,
                           capitalQuotes:Double,
                           interestValue:Double,
                                 modifier: Modifier=Modifier) {
    Card (
        modifier = modifier.padding(Dimensions.PADDING_SHORT)
    ) {


            Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
                Row(modifier = Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
                    FieldView(
                        name = R.string.last_month_paid,
                        value = DateUtils.localDateToStringDate(lastMonthPaid),
                        isMoney = false,
                        modifier = Weight1fAndPaddintRightSpace()
                    )

                    FieldView(
                        name = R.string.total_value,
                        value = NumbersUtil.toString(totalValue),
                        modifier = Weight1f()
                    )

                }
                Row {
                    FieldView(
                        name = R.string.capital_without_quotes,
                        value = NumbersUtil.toString(capitalWithoutQuotes),
                        modifier = Weight1fAndPaddintRightSpace()
                    )

                    FieldView(
                        name = R.string.capital_quotes,
                        value = NumbersUtil.toString(capitalQuotes),
                        modifier = Weight1fAndPaddintRightSpace()
                    )

                    FieldView(
                        name = R.string.interest_value,
                        value = NumbersUtil.toString(interestValue),
                        modifier = Weight1fAndAlightCenterVertical()
                    )

                }
            }


    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
fun BoughtMonthlyPreviewNight(){
    val viewModel = getViewMoel(LocalContext.current)
    MaterialThemeComposeUI {

        BoughtMonthly(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun BoughtMonthlyPreview(){
    val viewModel = getViewMoel(LocalContext.current)
    MaterialThemeComposeUI {

        BoughtMonthly(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.AUTOMOTIVE_1024p
)
fun BoughtMonthlyPreviewLandScape(){
    val viewModel = getViewMoel(LocalContext.current)
    MaterialThemeComposeUI {

        BoughtMonthly(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.FOLDABLE
)
fun BoughtMonthlyPreviewFold(){
    val viewModel = getViewMoel(LocalContext.current)
    MaterialThemeComposeUI {

        BoughtMonthly(viewModel)
    }
}

private fun getViewMoel(context:Context):BoughtMonthlyViewModel{
    val prefs = Prefs(context)
    val viewModel = BoughtMonthlyViewModel(null,null,null,null,prefs,null,null)
    viewModel.loader.value = true
    viewModel.graphList.addAll(arrayListOf(Pair("Issue 1",1000.0),Pair("Issue 2",2000.0)))
    viewModel.graphListPeriod.addAll(arrayListOf(Pair("Issue 3",500.0),Pair("Issue 4",300.0)))
    viewModel.totalValue.value = 3000.0
    viewModel.totalValueLastMonth.value = 2000.0
    viewModel.showList.value = true
    viewModel.showAdvance.value = true
    viewModel.showBought.value = true
    viewModel.showWallet.value = true
    viewModel.showCreditRate.value = true
    return viewModel
}

