package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
internal fun HeaderMonthly(key: YearMonth, list:List<CreditCardBoughtItemDTO>, monthlyState:BoughtMonthlyViewModel) {
    BoxWithConstraints {
        val maxWidth = maxWidth
        Row(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
        ) {
            when(WindowWidthSize.fromDp(maxWidth)){
                WindowWidthSize.COMPACT -> HeaderCompact(key = key, list = list, monthlyState = monthlyState)
                WindowWidthSize.MEDIUM -> HeaderMedium(key = key, list = list, monthlyState = monthlyState)
                else -> HeaderLarge(key = key, list = list, monthlyState = monthlyState)
            }
        }
    }
}

@Composable
private fun RowScope.HeaderCompact(key: YearMonth, list:List<CreditCardBoughtItemDTO>, monthlyState:BoughtMonthlyViewModel) {

    Column (modifier=Modifier.weight(1f)){
        Text(text = key.format(DateTimeFormatter.ofPattern("MMM yyyy")))
        Text(text = "${list.size}")
    }
    Column(modifier=Modifier.weight(1f)) {
        Text(text = stringResource(id = R.string.total))
        Text(text = monthlyState.totalBought)
    }

    Column(modifier=Modifier.weight(1f)) {
        Text(text = stringResource(id = R.string.total_quote))
        Text(text = monthlyState.totalQuote)
    }
}

@Composable
private fun RowScope.HeaderMedium(key: YearMonth, list:List<CreditCardBoughtItemDTO>, monthlyState:BoughtMonthlyViewModel) {
    Column {
        Text(text = key.format(DateTimeFormatter.ofPattern("MMM yyyy")))
        Text(text = "${list.size}")
    }

    Text(text = stringResource(id = R.string.total),modifier= Modifier
        .weight(1f)
        .padding(2.dp))
    Text(text = monthlyState.totalBought,modifier= Modifier
        .weight(1f)
        .padding(2.dp))

    Text(text = stringResource(id = R.string.total_quote),modifier= Modifier
        .weight(1f)
        .padding(2.dp))
    Text(text = monthlyState.totalQuote,modifier= Modifier
        .weight(1f)
        .padding(2.dp))
}

@Composable
fun RowScope.HeaderLarge(key: YearMonth, list:List<CreditCardBoughtItemDTO>, monthlyState:BoughtMonthlyViewModel) {
    Text(text = key.format(DateTimeFormatter.ofPattern("MMM yyyy")),modifier= Modifier
        .weight(1f)
        .padding(2.dp))
    Text(text = "# ${list.size}",modifier= Modifier
        .weight(1f)
        .padding(2.dp))

    Text(text = stringResource(id = R.string.total),modifier= Modifier
        .weight(1f)
        .padding(2.dp))
    Text(text = monthlyState.totalBought,modifier= Modifier
        .weight(1f)
        .padding(2.dp))

    Text(text = stringResource(id = R.string.total_quote),modifier= Modifier
        .weight(1f)
        .padding(2.dp))
    Text(text = monthlyState.totalQuote,modifier= Modifier
        .weight(1f)
        .padding(2.dp))
}