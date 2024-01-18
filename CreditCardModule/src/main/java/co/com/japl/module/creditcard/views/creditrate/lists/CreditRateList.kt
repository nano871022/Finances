package co.com.japl.module.creditcard.views.creditrate.lists

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.creditrate.lists.CreditRateListViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsItemCreditRate
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVertical
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreditRateList(viewModel: CreditRateListViewModel){
    val showProgress = remember {
        viewModel.showProgress
    }
    val progress = remember {
        viewModel.progress
    }
    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(showProgress.value){
        LinearProgressIndicator( progress = progress.floatValue)
    }else{
        Scaffold(
            floatingActionButton = {
                IconButton(onClick = {
                    viewModel.add()
                }) {
                    Icon(imageVector = Icons.Rounded.AddCircleOutline, contentDescription = stringResource(id = R.string.add_credit_rate))
                }
            }
        ) {
            Body(viewModel,Modifier.padding(it))
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Body(viewModel: CreditRateListViewModel,modifier:Modifier=Modifier){
    viewModel.creditCard?.let {list->
        Carousel(size = list.size) {
            val value = list.entries.toList()[it]
            CreditCard(value = value, viewModel = viewModel)
        }
    }
}

@Composable
private fun CreditCard(value:Map.Entry<CreditCardDTO?,List<TaxDTO>>,viewModel: CreditRateListViewModel){
    OutlinedCard(modifier = Modifier
        .fillMaxWidth()
        .padding(Dimensions.PADDING_SHORT)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            FieldView(
                title = stringResource(id = R.string.credit_card),
                value = value.key?.name ?: "", modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.PADDING_SHORT),
                suffix = {
                    IconButton(onClick = { viewModel.add(value.key?.id) }) {
                        Icon(imageVector = Icons.Rounded.AddCircleOutline
                            , contentDescription = stringResource(id = R.string.add_credit_rate))
                    }
                }
            )

            Divider()

            value.value.forEach { rate ->
                Rate(
                    rate = rate,
                    { viewModel.delete(it) },
                    { viewModel.enable(it) },
                    { viewModel.disable(it) })
            }
        }
    }
}

@Composable
private fun Rate(rate:TaxDTO,delete:(Int)->Unit, enable:(Int)->Unit, disable:(Int)->Unit){
    val context = LocalContext.current
    val state = remember {
        mutableStateOf(false)
    }
    val stateOptions = remember {
        mutableStateOf(false)
    }
    Card ( modifier = Modifier
        .fillMaxWidth()
        .padding(Dimensions.PADDING_SHORT)
        .border(
            width = if (rate.status.toInt() == 0) {
                1.dp
            } else {
                0.dp
            },
            color = if (rate.status.toInt() == 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
        )){
        Column ( modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimensions.PADDING_SHORT, end = Dimensions.PADDING_SHORT)) {
            Row ( modifier = Modifier.fillMaxWidth()){
                Text(text =
                stringArrayResource(id = R.array.Months).let {
                    it[rate.month.toInt()]
                }, modifier=Weight1fAndAlightCenterVertical())

                Text(text = rate.year.toString(), modifier=Weight1fAndAlightCenterVertical())

                Text(text = "${rate.value} % ${rate.kindOfTax?.getName()}", modifier=Weight1fAndAlightCenterVertical())

                IconButton(onClick = { state.value = true }) {
                    Icon(painter = painterResource(id = R.drawable.more_vertical), contentDescription = stringResource(
                        id = R.string.see_more
                    ))
                }
            }
            if(rate.kind != KindInterestRateEnum.CREDIT_CARD) {
                Row {
                    Text(text = stringResource(id = R.string.kind_rate))

                    Text(text = getKindInterestRate(rate.kind, context))

                    Text(text = stringResource(id = R.string.months))

                    Text(text = rate.period.toString())
                }
            }
        }
    }

    if(state.value){
        MoreOptionsDialog(listOptions = MoreOptionsItemCreditRate.values().toList().filter {
            (rate.status.toInt() == 1 && it != MoreOptionsItemCreditRate.ENABLED)||
                    (rate.status.toInt() == 0 && it != MoreOptionsItemCreditRate.DISABLED)
      }
            , onDismiss = { state.value = false }
            , onClick = {
                when(it){
                    MoreOptionsItemCreditRate.DISABLED->disable.invoke(rate.id)
                    MoreOptionsItemCreditRate.ENABLED->enable.invoke(rate.id)
                    MoreOptionsItemCreditRate.DELETE->stateOptions.value = true
                }
                state.value = false
            })
    }

    if(stateOptions.value) {
        AlertDialogOkCancel(
            title = R.string.do_you_want_to_delete_this_record,
            confirmNameButton = R.string.delete,
            onDismiss = { stateOptions.value = false }
            , onClick = {
                delete.invoke(rate.id)
                stateOptions.value = false
            })
    }
}

private fun getKindInterestRate(kind:KindInterestRateEnum,context: Context):String{
    return when(kind){
        KindInterestRateEnum.CASH_ADVANCE -> context.getString(R.string.cash_advance)
        KindInterestRateEnum.WALLET_BUY -> context.getString(R.string.wallet_buy)
        else -> context.getString(R.string.credit_card)
    }

}
