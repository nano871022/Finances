package co.com.japl.module.creditcard.views.paid

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
internal fun RecordBody(dateStart:String,dateEnd:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>) {
    BoxWithConstraints {
        when (WindowWidthSize.fromDp(maxWidth)) {
            WindowWidthSize.COMPACT -> RecordBodyCompact("$dateEnd \n $dateStart", capital, interest, moreOptionsStatus)
            WindowWidthSize.MEDIUM -> RecordBodyMedium("$dateEnd - $dateStart", capital, interest, moreOptionsStatus)
            else-> RecordBodyLarge("$dateEnd - $dateStart", capital, interest, moreOptionsStatus)
        }
    }
}

@Composable
private fun RecordBodyCompact(dateRange:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>){

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = dateRange)

                Text(text = stringResource(id = R.string.total_value), modifier = Modifier.weight(1f))

                Text(text = NumbersUtil.toString(capital + interest),Modifier.weight(1f))

                IconButton(onClick = { moreOptionsStatus.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vertical),
                        contentDescription = "See more"
                    )
                }
            }
                Row(modifier = Modifier.fillMaxWidth()) {

                    FieldView(name = R.string.interest_value,
                        value = NumbersUtil.toString(interest),
                        modifier = Modifier.weight(1f))

                    FieldView(name = R.string.interest_value,
                        value = NumbersUtil.toString(capital),
                        modifier = Modifier.weight(1f))
                }
        }
}

@Composable
private fun RecordBodyMedium(dateRange:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>){
    val cardState = remember {mutableStateOf(false)}


    Column{


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = dateRange)

            Text(
                text = stringResource(id = R.string.total_value), modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
            Text(text = NumbersUtil.COPtoString(capital + interest), modifier = Modifier.weight(1f))

            IconButton(onClick = { moreOptionsStatus.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.more_vertical),
                    contentDescription = "See more"
                )
            }
        }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.interest_value),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = NumbersUtil.COPtoString(interest),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )

                Text(
                    text = stringResource(id = R.string.capital_value), modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
                Text(text = NumbersUtil.COPtoString(capital), modifier = Modifier.weight(1f))
            }
    }
}

@Composable
private fun RecordBodyLarge(dateRange:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>){
    Column {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = dateRange)

            Text(text = stringResource(id = R.string.capital_value_short), modifier = Modifier.padding(start=10.dp,end=10.dp))
            Text(text = NumbersUtil.COPtoString(capital), modifier = Modifier.weight(1f))

            Text(text = stringResource(id = R.string.interest_value_short),modifier = Modifier.weight(1f))
            Text(text = NumbersUtil.COPtoString(interest), modifier = Modifier.weight(1f))

            Text(text = stringResource(id = R.string.total_quote_value_short), modifier = Modifier.padding(start=10.dp,end=10.dp))
            Text(text = NumbersUtil.COPtoString(capital + interest), modifier = Modifier.weight(1f))

            IconButton(onClick = { moreOptionsStatus.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.more_vertical),
                    contentDescription = "See more"
                )
            }
        }
    }
}