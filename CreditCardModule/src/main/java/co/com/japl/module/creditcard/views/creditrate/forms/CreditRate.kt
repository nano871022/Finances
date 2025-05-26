package co.com.japl.module.creditcard.views.creditrate.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.creditrate.forms.CreateRateViewModel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreditRate(viewModel:CreateRateViewModel){
    var statePogress = remember {
        viewModel.loader
    }
    var progress = remember {
        viewModel.progress
    }
    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(statePogress.value){
        LinearProgressIndicator(
            progress = { progress.floatValue },
        )
    }else {

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.save() },
                    elevation=FloatingActionButtonDefaults.elevation(10.dp),
                    backgroundColor= MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircleOutline,
                        contentDescription = stringResource(id = R.string.add_credit_rate)
                    )
                }
            }
        ) {
            Body(viewModel, Modifier.padding(it))
        }
    }
}

@Composable
private fun Body(viewModel: CreateRateViewModel,modifier:Modifier){
    val creditCard = remember { viewModel.creditCard}
    val creditCardError = remember { viewModel.creditCardError}
    val kindCreditRate = remember { viewModel.creditCardKind}
    val kindCreditRateError = remember { viewModel.creditCardKindError}
    val year = remember { viewModel.year}
    val yearError = remember { viewModel.yearError}
    val month = remember { viewModel.month}
    val monthError = remember { viewModel.monthError}
    val rate = remember { viewModel.rate}
    val rateError = remember { viewModel.rateError}
    val status = remember { viewModel.status}
    val period = remember { viewModel.period}
    val periodError = remember { viewModel.periodError}
    val kindRate = remember { viewModel.creditRateKind}
    val kindRateError = remember { viewModel.creditRateKindError}
    val periodShow = remember { viewModel.periodShow}

    Column (modifier = modifier.fillMaxWidth()) {

        FieldSelect(value = viewModel.creditCards.firstOrNull { creditCard.value.isNotEmpty() && it.id == creditCard.value.toInt()}?.name?:"",
            callable = { creditCard.value =  it?.let{it.first.toString()}?:""}
            , title = stringResource(id = R.string.credit_card)
            , list = viewModel.creditCards.map { Pair(it.id,it.name) }
            , isError = creditCardError
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))

       var kindCreditRateV = getKind(value = kindCreditRate.value)
        if(isCacheAdvance(kindCreditRate.value)){
            periodShow.value = true
        }else{
            periodShow.value = false
        }

        FieldSelect(value = kindCreditRateV,
            callable = {
                kindCreditRate.value = it?.let{it.first.toString()}?:""
                       }
            , cleanTitle = R.string.clean_kind_credit_rate
            , title = stringResource(id = R.string.kind_rate)
            , isError = kindCreditRateError
            , list = KindInterestRateEnum.values().map { Pair(it.getCode().toInt(), getKind(value = it.name)) }
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))

        FieldText(value = year.value,
            callback = {year.value = it}
                , clearTitle = R.string.clean_year
            , title = stringResource(id = R.string.year)
            , validation = {viewModel.validate()}
            , hasErrorState = yearError

            , icon = Icons.Rounded.Cancel
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))

        val months = stringArrayResource(id = R.array.Months).mapIndexed { index, value -> Pair(index,value)}

        FieldSelect(value = months.firstOrNull { month.value.isNotEmpty() && it.first == month.value.toInt() }?.second ?:"" ,
            callable = {
                    month.value = it?.let{ it.first.toString()} ?: ""
                       }
            , title = stringResource(id = R.string.month)
            , cleanTitle = R.string.clean_month
            , list = months
            , isError = monthError
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))


        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PADDING_SHORT)){
            FieldText(value = rate.value,
                callback = { rate.value = it },
                title = stringResource(id = R.string.credit_rate_),
                clearTitle = R.string.clear_credit_rate,
                icon = Icons.Rounded.Cancel,
                validation = { viewModel.validate() },
                hasErrorState = rateError,
                modifier = Weight1f().padding(end=Dimensions.PADDING_SHORT))


            FieldSelect(value = kindRate.value,
                callable = { kindRate.value = it?.let{it.second} ?: ""},
                title = stringResource(id = R.string.kind_rate),
                cleanTitle = R.string.clear_kind_credit_rate,
                isError = kindRateError,
                list= KindOfTaxEnum.values().map { Pair(it.ordinal,it.getName()) },
                modifier = Weight1f()
                    )
        }

        if(periodShow.value) {

            FieldText(value = period.value,
                callback = { period.value = it },
                title = stringResource(id = R.string.period),
                validation = { viewModel.validate() },
                hasErrorState = periodError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.PADDING_SHORT))
        }

        CheckBoxField(value = status.value,
            callback = {status.value = it}
            , title = stringResource(id = R.string.status)
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))


    }

}

private fun isCacheAdvance(value:String):Boolean{
    return value == KindInterestRateEnum.CASH_ADVANCE.toString() || value == KindInterestRateEnum.CASH_ADVANCE.getCode().toString()
}

@Composable
private fun getKind(value:String):String{
    return when(value){
        KindInterestRateEnum.CREDIT_CARD.toString()-> stringResource(id = R.string.CREDIT_CARD)
        KindInterestRateEnum.CASH_ADVANCE.toString()-> stringResource(id =R.string.CASH_ADVANCE)
        KindInterestRateEnum.WALLET_BUY.toString()-> stringResource(id =R.string.WALLET_BUY)
        KindInterestRateEnum.CREDIT_CARD.getCode().toString()-> stringResource(id = R.string.CREDIT_CARD)
        KindInterestRateEnum.CASH_ADVANCE.getCode().toString()-> stringResource(id =R.string.CASH_ADVANCE)
        KindInterestRateEnum.WALLET_BUY.getCode().toString()-> stringResource(id =R.string.WALLET_BUY)
        else-> value
    }
}
@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun CreditRatePreview(){
    val viewModel = CreateRateViewModel(null,null,null,null,null)
    viewModel.loader.value = false
    MaterialThemeComposeUI {
        CreditRate(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CreditRatePreviewDark(){
    val viewModel = CreateRateViewModel(null,null,null,null,null)
    viewModel.loader.value = false
    MaterialThemeComposeUI {
        CreditRate(viewModel)
    }
}