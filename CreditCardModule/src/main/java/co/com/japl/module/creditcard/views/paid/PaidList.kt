package co.com.japl.module.creditcard.views.paid

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.paid.BoughtCreditCardViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsItemPaidPeriodEnum
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.values.Dimensions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@Composable
fun PaidList(viewModel: BoughtCreditCardViewModel){
    val loaderState = remember {viewModel.progress}
    val loader = remember {viewModel.loader}
    val scope = rememberCoroutineScope()
    scope.launch {
        withContext(Dispatchers.IO) {
            viewModel.main()
        }
    }

    if(loader.value.not() ){
        LinearProgressIndicator(
            progress = { loaderState.floatValue },
        )
    }else {
        Yearly(viewModel = viewModel)
    }

}

@Composable
private fun Yearly(viewModel:BoughtCreditCardViewModel){
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(viewModel.periodList.size){item->
            val key = viewModel.periodList.keys.toList()[item]
            val list = viewModel.periodList[key]!!
            Yearly(key = key, list = list, viewModel = viewModel)
        }
    }
}

@Composable
private fun Yearly(key:Long,list:List<BoughtCreditCardPeriodDTO>,viewModel:BoughtCreditCardViewModel){
    val drowdownState = remember { mutableStateOf(false) }
    Surface(
        border = BorderStroke(1.dp,color= MaterialTheme.colorScheme.onPrimaryContainer),
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        onClick = { drowdownState.value = !drowdownState.value },
        modifier=Modifier.padding(bottom = Dimensions.PADDING_SHORT)) {
        Column {
            YearlyHeader(year = key, list = list)
            if (drowdownState.value) {
                Monthly(list = list, goto = { cutoffDay, cutoff ->
                    viewModel.goToListDetail(cutoffDay, cutoff)
                })
            }
        }
    }
}



@Composable
private fun Monthly(list:List<BoughtCreditCardPeriodDTO>,goto:(Short,LocalDateTime)->Unit){
    val months = stringArrayResource(id = R.array.months_short)
    Column(modifier = Modifier.fillMaxWidth()) {
        for(record in list) {
            Record(dateStart = "${months[record.periodStart.monthValue]} ${record.periodStart.dayOfMonth}"
                , dateEnd = "${months[record.periodEnd.monthValue]} ${record.periodEnd.dayOfMonth}"
                , capital = record.capital.toDouble()
                , interest = record.interest.toDouble()
                , goto = {
                    goto(record.cutoffDay,record.periodEnd)
                })
        }
    }
}

@Composable
private fun Record(dateStart:String,dateEnd:String,capital:Double = 0.0, interest:Double = 0.0, goto:()->Unit){
    val moreOptionsStatus= remember { mutableStateOf(false) }
    if(moreOptionsStatus.value){
        MoreOptionsDialog(listOptions =  MoreOptionsItemPaidPeriodEnum.values().toList()
            , onDismiss = { moreOptionsStatus.value = false }
            , onClick = {
                goto.invoke()
                moreOptionsStatus.value = false
            })
    }


    Card ( modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)){
            Column(modifier=Modifier.padding(10.dp)){
                RecordBody(
                    dateStart = dateStart,
                    dateEnd = dateEnd,
                    capital = capital,
                    interest = interest,
                    moreOptionsStatus = moreOptionsStatus
                )
            }
    }
}
