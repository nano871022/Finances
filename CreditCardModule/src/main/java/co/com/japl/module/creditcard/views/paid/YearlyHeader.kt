package co.com.japl.module.creditcard.views.paid

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.module.creditcard.R
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.myapplication.utils.NumbersUtil


@Composable
internal fun YearlyHeader(year:Long, list:List<BoughtCreditCardPeriodDTO>){
    BoxWithConstraints (modifier=Modifier.padding(10.dp)){
        when (WindowWidthSize.fromDp(maxWidth)) {
            WindowWidthSize.COMPACT -> YearlyHeaderCompact(year, list)
            WindowWidthSize.MEDIUM -> YearlyHeaderMedium(year, list)
            else-> YearlyHeaderLarge(year, list)
        }
    }
}

@Composable
private fun YearlyHeaderLarge(year:Long, list:List<BoughtCreditCardPeriodDTO>){
    Row {
        Text(text = " $year ",modifier = Modifier.weight(1f))
        Text(text = stringResource(id = R.string.total_quote_value_short), modifier = Modifier.weight(1f))
        Text(text = NumbersUtil.COPtoString(list.sumOf { it.total }), modifier = Modifier.weight(1f))

        Text(text = stringResource(id = R.string.capital_value_short), modifier= Modifier.weight(1f))
        Text(text = NumbersUtil.COPtoString(list.sumOf { it.capital }), modifier= Modifier.weight(1f))

        Text(text = stringResource(id = R.string.interest_value_short), modifier= Modifier.weight(1f))
        Text(text = NumbersUtil.COPtoString(list.sumOf { it.interest }), modifier= Modifier.weight(1f))
    }
}



@Composable
private fun YearlyHeaderMedium(year:Long, list:List<BoughtCreditCardPeriodDTO>){
    Column {
        Row {
            Text(text = " $year ", modifier = Modifier.weight(1f))
            Text(text = stringResource(id = R.string.total_quote), modifier = Modifier.weight(1f))
            Text(
                text = NumbersUtil.COPtoString(list.sumOf { it.total }),
                modifier = Modifier.weight(1f)
            )
        }
        Row {
            Text(text = stringResource(id = R.string.capital_value), modifier = Modifier.weight(1f))
            Text(
                text = NumbersUtil.COPtoString(list.sumOf { it.capital }),
                modifier = Modifier.weight(1f)
                ,textAlign = TextAlign.End
            )

            Text(
                text = stringResource(id = R.string.interest_value),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
            Text(
                text = NumbersUtil.COPtoString(list.sumOf { it.interest }),
                modifier = Modifier.weight(1f)
                ,textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun YearlyHeaderCompact(year:Long, list:List<BoughtCreditCardPeriodDTO>){
    Column {
        Row {
            Text(text = " $year ", modifier = Modifier.weight(1f))
            Text(text = stringResource(id = R.string.total_quote), modifier = Modifier.weight(1f))
            Text(
                text = NumbersUtil.COPtoString(list.sumOf { it.total }),
                modifier = Modifier.weight(1f)
            )
        }
        Row {

            FieldView(name = R.string.capital_value,
                value = NumbersUtil.toString(list.sumOf { it.capital }),
                modifier = Weight1f())

            FieldView(name = R.string.interest_value,
                value = NumbersUtil.toString(list.sumOf { it.interest }),
                modifier = Weight1f())
        }
    }
}