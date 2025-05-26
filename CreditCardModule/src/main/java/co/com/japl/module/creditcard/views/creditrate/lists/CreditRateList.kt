package co.com.japl.module.creditcard.views.creditrate.lists

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.creditrate.lists.CreditRateListViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsItemCreditRate
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.HelpWikiButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVertical
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime

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
        LinearProgressIndicator(
            progress = { progress.floatValue },
            modifier = Modifier.fillMaxWidth(),
        )
    }else{
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.add()
                },elevation=FloatingActionButtonDefaults.elevation(10.dp),
                    backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
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
    val mapState = remember {viewModel.creditCard}
    Column {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            HelpWikiButton(wikiUrl = R.string.wiki_credit_rate_url, descriptionContent = R.string.wiki_credit_rate_description)
        }
        mapState?.let { list ->
            Carousel(size = list.size, modifier = Modifier.fillMaxHeight()) {
                val value = list.entries.toList()[it]
                CreditCard(value = value, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun CreditCard(value:Map.Entry<CreditCardDTO?,List<TaxDTO>>,viewModel: CreditRateListViewModel){
    val listState = remember{ mutableListOf(value.value) }

    OutlinedCard(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(Dimensions.PADDING_SHORT)) {
                FieldView(
                    title = stringResource(id = R.string.credit_card),
                    value = value.key?.name ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.PADDING_SHORT),
                    suffix = {
                        IconButton(onClick = { viewModel.add(value.key?.id) }) {
                            Icon(
                                imageVector = Icons.Rounded.AddCircleOutline,
                                contentDescription = stringResource(id = R.string.add_credit_rate)
                            )
                        }
                    }
                )

        HorizontalDivider()
            Column(modifier=Modifier) {
                listState.forEach { list ->
                    LazyColumn () {
                        items(list.size){ pos ->
                            Rate(
                                rate = list[pos],
                                { viewModel.delete(it) },
                                { viewModel.enable(it) },
                                { viewModel.disable(it) },
                                { codeCreditCard, codeCreditRate ->
                                    viewModel.edit(
                                        codeCreditCard,
                                        codeCreditRate
                                    )
                                },{ viewModel.clone(it)})
                        }
                    }
                }
            }
    }
}


@Composable
private fun Rate(rate:TaxDTO,delete:(Int)->Unit, enable:(Int)->Unit, disable:(Int)->Unit,edit:(Int,Int)->Unit, clone:(Int)->Unit){
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
                    MoreOptionsItemCreditRate.EDIT->edit.invoke(rate.codCreditCard,rate.id)
                    MoreOptionsItemCreditRate.CLONE->clone.invoke(rate.id)
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun CreditRateListPreview(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditRateList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CreditRateListPreviewDark(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditRateList(viewModel)
    }
}

@Composable
private fun getViewModel():CreditRateListViewModel{
    val viewModel = CreditRateListViewModel(LocalContext.current, null,null, null)
    viewModel.showProgress.value = false
    viewModel.creditCard?.put(CreditCardDTO(0,"credit card 1",24,24,
        BigDecimal.ZERO, LocalDateTime.now(),false,false,false),
        arrayListOf(
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),

            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),

        ))
    viewModel.creditCard?.put(CreditCardDTO(0,"Credut card 2",24,24,
        BigDecimal.ZERO, LocalDateTime.now(),false,false,false),
        arrayListOf(
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
            TaxDTO(0,11,2023,1,1, LocalDateTime.now(),0.0,KindInterestRateEnum.CREDIT_CARD,21,KindOfTaxEnum.MONTHLY_EFFECTIVE),
        ))
    return viewModel
}