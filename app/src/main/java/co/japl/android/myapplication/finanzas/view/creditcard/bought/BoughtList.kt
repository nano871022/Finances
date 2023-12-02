package co.japl.android.myapplication.finanzas.view.creditcard.bought

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.boughtcreditcard.ListBoughtViewModel
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import co.japl.android.myapplication.finanzas.view.components.FieldView
import co.japl.android.myapplication.finanzas.view.creditcard.bought.list.BoughList
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun BoughtList(listBoughtViewModel:ListBoughtViewModel){


    val isLoad = remember {  listBoughtViewModel.isLoad }
    val progress = remember { listBoughtViewModel.progress }

    CoroutineScope(Dispatchers.Default).launch {
        listBoughtViewModel.main()
    }

        if (isLoad.value.not()) {
            LinearProgressIndicator(
                progress = progress.floatValue,
                modifier = Modifier.fillMaxWidth()
            )
        }else {
            ListBought(listBoughtViewModel)
        }
}

@Composable
private fun ListBought(listBoughtViewModel:ListBoughtViewModel){
    val cashAdvanceState = remember { listBoughtViewModel.cashAdvance }
    val creditCardState = remember { listBoughtViewModel.creditCard }
    val popupState = remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton ={

            if(cashAdvanceState.value) {
                FloatingActionButton(
                    onClick = {
                        Log.d("FloatingButton", "cash advance button")
                        listBoughtViewModel.goToCashAdvance()
                    },
                    modifier = Modifier
                ) {
                    Icon(Icons.Filled.Add, stringResource(id = R.string.cash_advance))

                }
            }

            if(creditCardState.value) {
                FloatingActionButton(
                    onClick = {
                        Log.d("FloatingButton", "credit card button")
                        listBoughtViewModel.goToCreditCard()
                    },
                    modifier = Modifier
                ) {
                    Icon(Icons.Filled.AddBox, stringResource(id = R.string.credit_card))

                }
            }
        }
    ) {


        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(it)) {
            BoxWithConstraints {
                if (WindowWidthSize.MEDIUM.isEqualTo(maxWidth)) {
                    Column {
                        MainCompact(listBoughtViewModel.boughtCreditCard, popupState)
                    }
                } else {
                    Row {
                        MainCompact(
                            listBoughtViewModel.boughtCreditCard,
                            popupState,
                            modifier = Modifier.weight(1f),
                            modifierHeader = Modifier.weight(2f)
                        )
                    }
                }
            }

            BoughList(data = listBoughtViewModel.boughtCreditCard)


        }
        Popup(listBoughtViewModel.boughtCreditCard.recap, popupState = popupState)


    }
}

@Composable
private fun MainCompact(data:BoughtCreditCard,popupState: MutableState<Boolean>,modifier:Modifier=Modifier,modifierHeader:Modifier=Modifier){
        Header(data.recap.totalCapital, data.recap.totalInterest, data.recap.quoteValue,modifierHeader)

        OutlinedButton(onClick = { popupState.value = !popupState.value },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)) {
            Text(
                text = stringResource(id = R.string.see_more),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
}


@Composable
private fun Header(capital:Double, interest:Double, quote:Double,modifier:Modifier=Modifier.fillMaxWidth()){
    Row(modifier=modifier) {

        FieldView(name = stringResource(id = R.string.capital_value_short)
            , value = NumbersUtil.toString(capital)
            , modifier = Modifier.weight(1f))

        FieldView(name = stringResource(id = R.string.interest_value_short)
            , value = NumbersUtil.toString(interest)
            , modifier = Modifier.weight(1f))

        FieldView(name = stringResource(id = R.string.total_quote)
            , value = NumbersUtil.toString(quote)
            , modifier = Modifier.weight(1f))
    }
}
