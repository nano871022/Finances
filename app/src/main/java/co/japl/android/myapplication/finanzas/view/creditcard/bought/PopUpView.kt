package co.japl.android.myapplication.finanzas.view.creditcard.bought

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.japl.android.myapplication.R
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
internal fun Popup(recap: RecapCreditCardBoughtListDTO, popupState: MutableState<Boolean>){

    co.com.japl.ui.components.Popup(R.string.recap_bought_cc, popupState) {

        BoxWithConstraints {
            val maxWidth = maxWidth
            Column {
                if (WindowWidthSize.MEDIUM.isEqualTo(maxWidth)) {
                    ContentCompact(recap)
                } else {
                    ContentLarge(recap, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}

@Composable
private fun ContentLarge(recap: RecapCreditCardBoughtListDTO,modifier: Modifier){
    Row(modifier=modifier){
        FieldView(name = R.string.total_bougth_item, value = "${recap.numBought}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.recurrent_item, value = "${recap.numRecurrentBought}", modifier = Modifier.weight(1f), isMoney = false)

        FieldView(name = R.string.quote_1_item, value = "${recap.num1QuoteBought}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.quote_item, value = "${recap.numQuoteBought}", modifier = Modifier.weight(1f), isMoney = false)
    }

    Row(modifier=modifier){
        FieldView(name = R.string.pending_to_pay, value = NumbersUtil.toString(recap.pendingToPay), modifier = Modifier.weight(1f))
        FieldView(name = R.string.quote_value, value = NumbersUtil.toString(recap.quoteValue), modifier = Modifier.weight(1f))
    }
    Row(modifier=modifier){
        FieldView(name = R.string.current_capital_value_short, value = NumbersUtil.toString(recap.currentCapital), modifier = Modifier.weight(1f))
        FieldView(name = R.string.quote_capital_value_short, value = NumbersUtil.toString(recap.quoteCapital), modifier = Modifier.weight(1f))
        FieldView(name = R.string.capital_value_short, value = NumbersUtil.toString(recap.totalCapital), modifier = Modifier.weight(1f))
    }
    Row(modifier=modifier){
        FieldView(name = R.string.current_interest_value_short, value = NumbersUtil.toString(recap.currentInterest), modifier = Modifier.weight(1f))
        FieldView(name = R.string.quote_interest_value_short, value = NumbersUtil.toString(recap.quoteInterest), modifier = Modifier.weight(1f))
        FieldView(name = R.string.interest_value_short, value = NumbersUtil.toString(recap.totalInterest), modifier = Modifier.weight(1f))
    }
    Row(modifier=modifier){
        FieldView(name = R.string.num_quote_end, value = "${recap.numQuoteEnd}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.total_quote_end, value = NumbersUtil.toString(recap.totalQuoteEnd), modifier = Modifier.weight(1f))

        FieldView(name = R.string.num_quote_next_end, value = "${recap.numNextQuoteEnd}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.total_quote_next_end, value = NumbersUtil.toString(recap.totalNextQuoteEnd), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ContentCompact(recap: RecapCreditCardBoughtListDTO) {
    Row {
        FieldView(
            name = R.string.total_bougth_item,
            value = "${recap.numBought}",
            modifier = Modifier.weight(1f),
            isMoney = false
        )
        FieldView(
            name = R.string.recurrent_item,
            value = "${recap.numRecurrentBought}",
            modifier = Modifier.weight(1f),
            isMoney = false
        )
    }

    Row {
        FieldView(
            name = R.string.quote_item,
            value = "${recap.numQuoteBought}",
            modifier = Modifier.weight(1f),
            isMoney = false
        )
        FieldView(
            name = R.string.quote_1_item,
            value = "${recap.num1QuoteBought}",
            modifier = Modifier.weight(1f),
            isMoney = false
        )
    }

    Row {
        FieldView(
            name = R.string.pending_to_pay,
            value = NumbersUtil.toString(recap.pendingToPay),
            modifier = Modifier.weight(1f)
        )
    }
    Row {
        FieldView(
            name = R.string.current_capital_value_short,
            value = NumbersUtil.toString(recap.currentCapital),
            modifier = Modifier.weight(1f)
        )
        FieldView(
            name = R.string.current_interest_value_short,
            value = NumbersUtil.toString(recap.currentInterest),
            modifier = Modifier.weight(1f)
        )
    }
    Row {
        FieldView(
            name = R.string.quote_capital_value_short,
            value = NumbersUtil.toString(recap.quoteCapital),
            modifier = Modifier.weight(1f)
        )
        FieldView(
            name = R.string.quote_interest_value_short,
            value = NumbersUtil.toString(recap.quoteInterest),
            modifier = Modifier.weight(1f)
        )
    }
    Row {
        FieldView(
            name = R.string.capital_value_short,
            value = NumbersUtil.toString(recap.totalCapital),
            modifier = Modifier.weight(1f)
        )
        FieldView(
            name = R.string.interest_value_short,
            value = NumbersUtil.toString(recap.totalInterest),
            modifier = Modifier.weight(1f)
        )
    }
    Row {
        FieldView(
            name = R.string.quote_value,
            value = NumbersUtil.toString(recap.quoteValue),
            modifier = Modifier.weight(1f)
        )
    }
    Row {
        FieldView(
            name = R.string.num_quote_end,
            value = "${recap.numQuoteEnd}",
            modifier = Modifier.weight(1f),
            isMoney = false
        )
        FieldView(
            name = R.string.total_quote_end,
            value = NumbersUtil.toString(recap.totalQuoteEnd),
            modifier = Modifier.weight(1f)
        )
    }
    Row {
        FieldView(
            name = R.string.num_quote_next_end,
            value = "${recap.numNextQuoteEnd}",
            modifier = Modifier.weight(1f),
            isMoney = false
        )
        FieldView(
            name = R.string.total_quote_next_end,
            value = NumbersUtil.toString(recap.totalNextQuoteEnd),
            modifier = Modifier.weight(1f)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupSetting(viewModel: SettingsViewModel,state: MutableState<Boolean>) {
    val simulatorState = remember { viewModel.simulatorState }
    val daysSmsRead = remember { viewModel.daysSmsRead }
    val errorDaysSmsRead = remember { viewModel.errorDaysSmsRead }
    val context = LocalContext.current
    co.com.japl.ui.components.Popup(title = R.string.settings_credit_card_boughts, state = state) {
        Scaffold(
            floatingActionButton = {
                FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
                    viewModel.save(context)
                    state.value = false
                }
            },
            modifier=Modifier.padding(Dimensions.PADDING_SHORT)
        ) {
       Column(modifier = Modifier.padding(it)) {

                Row() {
                    Text(
                        text = stringResource(id = R.string.simulator),
                        color=MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .weight(2f)
                    )
                    Switch(checked = simulatorState.value, onCheckedChange = {
                        simulatorState.value = it
                    })
                }
                Text(text = stringResource(id = R.string.description_simulator)
                        ,color=MaterialTheme.colorScheme.onPrimary
                ,modifier=Modifier.fillMaxWidth())

               FieldText(title = stringResource(id = R.string.days_sms_read),
                   value = "${daysSmsRead.value}",
                   hasErrorState = errorDaysSmsRead,
                   keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
                   icon = Icons.Rounded.Cancel,
                   validation = {viewModel.validation()},
                   callback = {
                       daysSmsRead.value = it
                   },modifier= Modifier.padding(top=Dimensions.PADDING_SHORT).fillMaxWidth())
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewPopUpSetting() {
    ApplicationInitial.prefs = Prefs(LocalContext.current)
    val viewModel = SettingsViewModel(ApplicationInitial.prefs)
    val state = remember { viewModel.state}
    state.value = true
    MaterialThemeComposeUI {
        PopupSetting(viewModel = viewModel, state)
    }
}