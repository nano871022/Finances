package co.japl.android.myapplication.finanzas.view.creditcard.bought

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TooltipBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.boughtcreditcard.ListBoughtViewModel
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.view.creditcard.bought.list.BoughList
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            ListBought(listBoughtViewModel,isLoad)
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListBought(listBoughtViewModel:ListBoughtViewModel,loader:MutableState<Boolean>){
    val cashAdvanceState = remember { listBoughtViewModel.cashAdvance }
    val creditCardState = remember { listBoughtViewModel.creditCard }
    val popupState = remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            Column {
                if (cashAdvanceState.value) {

                    FloatingActionButton(
                        onClick = {
                            Log.d("FloatingButton", "cash advance button")
                            listBoughtViewModel.goToCashAdvance()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(10.dp),
                        backgroundColor=MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier
                    ) {
                        Icon(Icons.Filled.Add, stringResource(id = R.string.cash_advance))

                    }
                }

                if (creditCardState.value) {
                    FloatingActionButton(
                        onClick = {
                            Log.d("FloatingButton", "credit card button")
                            listBoughtViewModel.goToCreditCard()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(10.dp),
                        backgroundColor=MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier
                    ) {
                        Icon(Icons.Filled.AddBox, stringResource(id = R.string.credit_card))

                    }
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

            BoughList(data = listBoughtViewModel.boughtCreditCard,listBoughtViewModel.prefs,loader=loader)


        }
        Popup(listBoughtViewModel.boughtCreditCard.recap, popupState = popupState)


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainCompact(data:BoughtCreditCard,popupState: MutableState<Boolean>,modifier:Modifier=Modifier,modifierHeader:Modifier=Modifier,settingViewModel: SettingsViewModel = SettingsViewModel(ApplicationInitial.prefs)){
    val settingState = remember { settingViewModel.state }
        Header(data.recap.totalCapital, data.recap.totalInterest, data.recap.quoteValue,modifierHeader)

        Row(modifier=modifier) {


            OutlinedButton(
                onClick = { popupState.value = !popupState.value },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = modifier
                    .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                    .weight(2f)
            ) {
                Text(
                    text = stringResource(id = R.string.see_more),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            PlainTooltipBox(
                tooltip = {
                    Text(text = stringResource(id = R.string.settings_credit_card_boughts))
                }
            ) {
                IconButton(onClick = { settingState.value = true }) {
                    Icon(
                        imageVector = Icons.Rounded.SettingsSuggest,
                        contentDescription = stringResource(
                            id = R.string.settings_credit_card_boughts
                        )

                    )
                }
            }
        }
    if(settingState.value){
        PopupSetting(viewModel = settingViewModel, state = settingState)
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


