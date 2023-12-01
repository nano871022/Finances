package co.japl.android.myapplication.finanzas.view.creditcard.bought

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import co.japl.android.myapplication.finanzas.view.components.FieldView
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
internal fun Popup(recap: RecapCreditCardBoughtListDTO, popupState: MutableState<Boolean>){

    co.japl.android.myapplication.finanzas.view.components.Popup(R.string.recap_bought_cc,popupState) {

        BoxWithConstraints {
            val maxWidth = maxWidth
            Column{
            if(WindowWidthSize.MEDIUM.isEqualTo(maxWidth)){
                ContentCompact(recap )
            }else{
                ContentLarge(recap , modifier=Modifier.padding(top=10.dp))
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
private fun ContentCompact(recap: RecapCreditCardBoughtListDTO){
    Row{
        FieldView(name = R.string.total_bougth_item, value = "${recap.numBought}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.recurrent_item, value = "${recap.numRecurrentBought}", modifier = Modifier.weight(1f), isMoney = false)
    }

    Row{
        FieldView(name = R.string.quote_item, value = "${recap.numQuoteBought}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.quote_1_item, value = "${recap.num1QuoteBought}", modifier = Modifier.weight(1f), isMoney = false)
    }

    Row{
        FieldView(name = R.string.pending_to_pay, value = NumbersUtil.toString(recap.pendingToPay), modifier = Modifier.weight(1f))
    }
    Row{
        FieldView(name = R.string.current_capital_value_short, value = NumbersUtil.toString(recap.currentCapital), modifier = Modifier.weight(1f))
        FieldView(name = R.string.current_interest_value_short, value = NumbersUtil.toString(recap.currentInterest), modifier = Modifier.weight(1f))
    }
    Row{
        FieldView(name = R.string.quote_capital_value_short, value = NumbersUtil.toString(recap.quoteCapital), modifier = Modifier.weight(1f))
        FieldView(name = R.string.quote_interest_value_short, value = NumbersUtil.toString(recap.quoteInterest), modifier = Modifier.weight(1f))
    }
    Row{
        FieldView(name = R.string.capital_value_short, value = NumbersUtil.toString(recap.totalCapital), modifier = Modifier.weight(1f))
        FieldView(name = R.string.interest_value_short, value = NumbersUtil.toString(recap.totalInterest), modifier = Modifier.weight(1f))
    }
    Row{
        FieldView(name = R.string.quote_value, value = NumbersUtil.toString(recap.quoteValue), modifier = Modifier.weight(1f))
    }
    Row{
        FieldView(name = R.string.num_quote_end, value = "${recap.numQuoteEnd}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.total_quote_end, value = NumbersUtil.toString(recap.totalQuoteEnd), modifier = Modifier.weight(1f))
    }
    Row{
        FieldView(name = R.string.num_quote_next_end, value = "${recap.numNextQuoteEnd}", modifier = Modifier.weight(1f), isMoney = false)
        FieldView(name = R.string.total_quote_next_end, value = NumbersUtil.toString(recap.totalNextQuoteEnd), modifier = Modifier.weight(1f))
    }
}