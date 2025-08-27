package co.com.japl.module.credit.views

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
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
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.DateUtils
import co.japl.android.graphs.utils.NumbersUtil
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CreditList(viewModel:ListViewModel) {
    val progress by remember { viewModel.progress}

    if(progress){
        viewModel.execute()
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else {
        Credit(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Credit(viewModel:ListViewModel){
    val list = remember {viewModel.list}
    val listHeader = arrayListOf(
            Header(title= stringResource(id = R.string.quote_payment_date_short), tooltip = stringResource(id = R.string.quote_payment_date),weight=1f),
            Header(title=stringResource(id = R.string.quote_payment_name_short), tooltip = stringResource(id = R.string.quote_payment_name),weight=1f) ,
            Header(title=stringResource(id = R.string.quote_payment_value_short), tooltip = stringResource(id = R.string.quote_payment_value),weight=1f)
        )


    Column (modifier = Modifier.padding(10.dp)){

        FieldView(name = R.string.total_due,
                  value = NumbersUtil.toString( list.sumOf { it.credit.value }),
                  alignment = Alignment.Center,
                  modifier = Modifier.fillMaxWidth())
        Row(modifier=Modifier.fillMaxWidth()){
            FieldView(name = R.string.credit_cnt_short,
                      value = list.size.toString(),
                      isMoney = false,
                      alignment = Alignment.Center,
                      modifier = Modifier.weight(1f))

            FieldView(name = R.string.total_quote,
                      value = NumbersUtil.toString( list.filter{!it.periodGrace}.sumOf { it.credit.quoteValue }),
                      alignment = Alignment.Center,
                      modifier = Modifier.weight(2f))
        }

        DataTable(listHeader = {_->  listHeader },
            sizeBody = list.size,
            footer = {_->}) {pos,_->
            Item(dto = list[pos],
                delete = {viewModel.delete(it)},
                amortization = {viewModel.amortization(it)},
                periodGrace = {id,period,date-> viewModel.periodGrace(id,period,date,date.plusMonths(period.toLong()))},
                additional = {viewModel.additional(it)},
                deletePeriodGrace = {viewModel.deletePeriodGrace(it)})
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun RowScope.Item(dto:CreditPeriodGraceDTO,delete:(Int)->Unit,amortization:(Int)->Unit,periodGrace:(Int,Int,LocalDate)->Unit,additional:(Int)->Unit,deletePeriodGrace:(Int)->Unit){
    val states = remember{ mutableStateOf(false) }

    Text(text=DateUtils.localDateToStringDate(dto.credit.date),
        color=MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
    Text(text=dto.credit.name,
        color=MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
    Text(text=NumbersUtil.COPtoString(dto.credit.quoteValue),
        textAlign = TextAlign.Right,
        color= dto.periodGrace.takeIf { !it }?.let{MaterialTheme.colorScheme.onBackground}?:MaterialTheme.colorScheme.error,
        modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
    IconButton(onClick = { states.value = true }) {
        Icon( imageVector = Icons.Rounded.MoreVert,
            tint=MaterialTheme .colorScheme.onBackground,
            contentDescription = "More")
    }
    if(states.value){
        Options(dto,states,delete,amortization,periodGrace,additional,deletePeriodGrace)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Options(dto:CreditPeriodGraceDTO,state:MutableState<Boolean>,delete:(Int)->Unit,amortization:(Int)->Unit,periodGrace:(Int,Int,LocalDate)->Unit,additional:(Int)->Unit,deletePeriodGrace:(Int)->Unit) {
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


    @Composable
    fun getViewModel():ListViewModel{
        return ListViewModel(YearMonth.now(),null,null,null).apply {
            list.add(CreditPeriodGraceDTO(CreditDTO(1,"test",LocalDate.now(),1.2,36,(1500000).toBigDecimal(),(50000).toBigDecimal(),
                KindPaymentsEnums.MONTHLY,
                KindOfTaxEnum.MONTHLY_EFFECTIVE),false))
            list.add(CreditPeriodGraceDTO(CreditDTO(2,"test",LocalDate.now(),1.2,36,(1400000).toBigDecimal(),(60000).toBigDecimal(),KindPaymentsEnums.MONTHLY,KindOfTaxEnum.MONTHLY_EFFECTIVE),false))
            list.add(CreditPeriodGraceDTO(CreditDTO(3,"test",LocalDate.now(),1.2,36,(1700000).toBigDecimal(),(70000).toBigDecimal(),KindPaymentsEnums.MONTHLY,KindOfTaxEnum.MONTHLY_EFFECTIVE),false))
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





