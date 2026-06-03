package co.com.japl.module.credit.views

import android.content.res.Configuration
import android.os.Build
import androidx.compose.material3.Surface
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.list.ListViewModel
import co.com.japl.module.credit.enums.MoreOptionsItemCreditsEnum
import co.com.japl.module.credit.pojo.CreditPeriodGraceDTO
import co.com.japl.module.credit.views.popups.PeriodGrace
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.DataTable

import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.DateUtils
import co.japl.android.graphs.utils.NumbersUtil
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CreditList(viewModel:ListViewModel) {
    val progress by remember { viewModel.progress}

    if(progress){
        viewModel.execute()
        Column(modifier=Modifier.fillMaxWidth()) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(text=stringResource(R.string.load_data),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth())
        }
    }else {
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel:ListViewModel){
    val list = remember {viewModel.list}
    Column(modifier=Modifier.fillMaxWidth()){

            FieldViewCards(name = R.string.total_due,
                value = NumbersUtil.COPtoString( list.sumOf { it.credit.value }),
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth())

            Row(modifier=Modifier.fillMaxWidth()){
                FieldViewCards(name = R.string.cnt_short,
                    value = list.size.toString(),
                    modifier = Modifier.weight(1f))

                FieldViewCards(name = R.string.total_quote,
                    value = NumbersUtil.COPtoString( list.filter{!it.periodGrace}.sumOf { it.credit.quoteValue }),
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .weight(2f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground,
                            RoundedCornerShape(10.dp)
                        ))
            }

            Divider(color= MaterialTheme.colorScheme.onBackground,modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_BOTTOM))

            val  group =  list.groupBy { it.credit.date.year }
        for (item in group) {
            Yearly(
                item.key, item.value,
                delete = viewModel::delete,
                amortization = viewModel::amortization,
                periodGrace = { codeCredit,period,date ->
                    viewModel.periodGrace(codeCredit,period,date, date.plusMonths(period.toLong()))
                              },
                additional = viewModel::additional,
                deletePeriodGrace = viewModel::deletePeriodGrace,
                edit = viewModel::edit,
            )
        }
    }
}

@Composable
private fun Yearly(year:Int,items: List<CreditPeriodGraceDTO>,delete:(Int)->Unit,amortization:(Int)->Unit,periodGrace:(Int,Int,LocalDate)->Unit,additional:(Int)->Unit,deletePeriodGrace:(Int)->Unit, edit:(Int)->Unit){
    Surface(modifier=Modifier.padding(Dimensions.PADDING_SHORT)){
        Column(modifier=Modifier.border(1.dp, MaterialTheme.colorScheme.onBackground,
            RoundedCornerShape(10.dp)
        )) {
            var group = items.groupBy { it.credit.date.month }
            Text(text = year.toString(), modifier = Modifier.padding(Dimensions.PADDING_SHORT))
            for(item in group) {
                Monthly(item.key.name, item.value, delete,amortization,periodGrace,additional,deletePeriodGrace,edit)
            }
        }
    }
}

@Composable
private fun Monthly(month: String,items: List<CreditPeriodGraceDTO>,delete:(Int)->Unit,amortization:(Int)->Unit,periodGrace:(Int,Int,LocalDate)->Unit,additional:(Int)->Unit,deletePeriodGrace:(Int)->Unit, edit:(Int)->Unit){
    Surface(modifier=Modifier.padding(Dimensions.PADDING_SHORT)){
        Column(modifier=Modifier.border(1.dp, MaterialTheme.colorScheme.onBackground,
            RoundedCornerShape(10.dp)
        )) {
            Text(text = month, modifier=Modifier.padding(Dimensions.PADDING_SHORT))
            for(item in items) {
                Item(item, delete,amortization,periodGrace,additional,deletePeriodGrace,edit)
            }
        }
    }
}

@Composable
private fun Item(item: CreditPeriodGraceDTO,delete:(Int)->Unit,amortization:(Int)->Unit,periodGrace:(Int,Int,LocalDate)->Unit,additional:(Int)->Unit,deletePeriodGrace:(Int)->Unit, edit:(Int)->Unit){
    val itemMore = remember {mutableStateOf(false)}
    val itemExpand = remember {mutableStateOf(false)}
    Card (modifier=Modifier.padding(Dimensions.PADDING_SHORT),
        onClick = {itemExpand.value=!itemExpand.value},
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)){
        Column {
            Row() {
                Text(
                    text = "${item.credit.date.dayOfMonth}",
                    modifier = Modifier
                        .padding(
                            start = Dimensions.PADDING_SHORT,
                            end = Dimensions.PADDING_SHORT
                        )
                        .align(alignment = Alignment.CenterVertically)
                )
                Text(
                    text = "${item.credit.name}",
                    modifier = Modifier
                        .weight(1f)
                        .align(alignment = Alignment.CenterVertically)
                )
                Text(
                    text = "${
                        DateUtils.getMonths(
                            item.credit.date,
                            LocalDateTime.now()
                        )
                    }/${item.credit.periods}",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                IconButton(onClick = { itemMore.value = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.see_more)
                    )
                }
            }
            if (itemExpand.value) {
                FieldViewCards(
                    name = R.string.value,
                    value = co.com.japl.ui.utils.NumbersUtil.COPtoString(item.credit.value),
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    FieldViewCards(
                        name = R.string.interest,
                        value = "${co.com.japl.ui.utils.NumbersUtil.toString(item.credit.tax)} ${item.credit.kindOfTax.value}",
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f)
                    )

                    FieldViewCards(
                        name = R.string.quote,
                        value = co.com.japl.ui.utils.NumbersUtil.COPtoString(item.credit.quoteValue),
                        textAlign = TextAlign.Right,
                        color = if (item.periodGrace) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

            }
        }
    }
    if(itemMore.value){
        Options(item,itemMore,delete,amortization,periodGrace,additional,deletePeriodGrace,edit)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Options(dto:CreditPeriodGraceDTO,state:MutableState<Boolean>,delete:(Int)->Unit,amortization:(Int)->Unit,periodGrace:(Int,Int,LocalDate)->Unit,additional:(Int)->Unit,deletePeriodGrace:(Int)->Unit, edit:(Int)->Unit) {
    val stateDelete = remember { mutableStateOf(false) }
    val stateDeletePeriodGrace = remember { mutableStateOf(false) }
    val periodGraceStatus = remember { mutableStateOf(false) }

    MoreOptionsDialog(
        listOptions = MoreOptionsItemCreditsEnum.entries
                    .filter {
                        (!dto.periodGrace && it != MoreOptionsItemCreditsEnum.DELETE_PERIOD_GRACE) ||
                        (dto.periodGrace && it != MoreOptionsItemCreditsEnum.PERIOD_GRACE  && it != MoreOptionsItemCreditsEnum.DELETE)
                            },
        onDismiss = { state.value = false }) {
        when (it) {
            MoreOptionsItemCreditsEnum.EDIT -> edit.invoke(dto.credit.id)
            MoreOptionsItemCreditsEnum.DELETE -> stateDelete.value = true
            MoreOptionsItemCreditsEnum.PERIOD_GRACE -> periodGraceStatus.value = true
            MoreOptionsItemCreditsEnum.ADDITIONAL -> additional.invoke(dto.credit.id)
            MoreOptionsItemCreditsEnum.AMOTIZATION -> amortization.invoke(dto.credit.id)
            MoreOptionsItemCreditsEnum.DELETE_PERIOD_GRACE -> stateDeletePeriodGrace.value = true
        }

    }

    if (stateDelete.value) {
        AlertDialogOkCancel(title = R.string.do_you_want_to_delete_this_record,
            confirmNameButton = R.string.delete,
            onDismiss = {
                stateDelete.value = false
                state.value = false
            }) {
            delete(dto.credit.id)
            stateDelete.value = false
            state.value = false
        }
    }

    if (stateDeletePeriodGrace.value) {
        AlertDialogOkCancel(title = R.string.do_you_want_to_delete_period_grace,
            confirmNameButton = R.string.delete,
            onDismiss = {
                stateDeletePeriodGrace.value = false
                state.value = false
            }) {
            deletePeriodGrace(dto.credit.id)
            stateDeletePeriodGrace.value = false
            state.value = false
        }
    }
    if (periodGraceStatus.value) {
        PeriodGrace(periodGraceStatus) { period, date ->
            periodGrace.invoke(dto.credit.id, period, date)
            periodGraceStatus.value = false
            state.value = false
        }
    }
}

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true, showBackground = true)
    fun MainListPreview(){
        val viewModel = getViewModel()
        viewModel.progress.value = false
        MaterialThemeComposeUI {
            CreditList(viewModel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true, showBackground = true, backgroundColor = 0x111000)
    fun MainListPreviewDark(){
        val viewModel = getViewModel()
        viewModel.progress.value = false
        MaterialThemeComposeUI {
            CreditList(viewModel)
        }
    }

@Composable
fun getViewModel():ListViewModel{
    return ListViewModel(
        creditsSvc = null,
        periodGraceSvc = null,
        savedStateHandle = SavedStateHandle()
    ).apply {
        list.add(CreditPeriodGraceDTO(CreditDTO(1,"test",LocalDate.now(),1.2,36,(1500000).toBigDecimal(),(50000).toBigDecimal(),
            KindPaymentsEnums.MONTHLY,
            KindOfTaxEnum.MONTHLY_EFFECTIVE),false))
        list.add(CreditPeriodGraceDTO(CreditDTO(2,"test",LocalDate.now().minusMonths(2),1.2,36,(1400000).toBigDecimal(),(60000).toBigDecimal(),KindPaymentsEnums.MONTHLY,KindOfTaxEnum.MONTHLY_EFFECTIVE),false))
        list.add(CreditPeriodGraceDTO(CreditDTO(3,"test",LocalDate.now().minusYears(1),1.2,36,(1700000).toBigDecimal(),(70000).toBigDecimal(),KindPaymentsEnums.MONTHLY,KindOfTaxEnum.MONTHLY_EFFECTIVE),true))
        list.add(CreditPeriodGraceDTO(CreditDTO(3,"test",LocalDate.now().plusDays(3).minusYears(1),1.2,36,(1700000).toBigDecimal(),(70000).toBigDecimal(),KindPaymentsEnums.MONTHLY,KindOfTaxEnum.MONTHLY_EFFECTIVE),true))
    }
}




