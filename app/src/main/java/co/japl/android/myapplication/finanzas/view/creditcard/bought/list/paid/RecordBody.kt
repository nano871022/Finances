package co.japl.android.myapplication.finanzas.view.creditcard.bought.list.paid

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
internal fun RecordBody(dateStart:String,dateEnd:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>) {
    BoxWithConstraints {
        when (WindowWidthSize.fromDp(maxWidth)) {
            WindowWidthSize.COMPACT -> RecordBodyCompact("$dateStart \n $dateEnd", capital, interest, moreOptionsStatus)
            WindowWidthSize.MEDIUM -> RecordBodyMedium("$dateStart - $dateEnd", capital, interest, moreOptionsStatus)
            else-> RecordBodyLarge("$dateStart - $dateEnd", capital, interest, moreOptionsStatus)
        }
    }
}

@Composable
private fun RecordBodyCompact(dateRange:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>){
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = dateRange)

            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.total_value))
                Text(
                    text = NumbersUtil.COPtoString(capital + interest),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IconButton(onClick = { moreOptionsStatus.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.more_vertical),
                    contentDescription = "See more"
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.interest_value))
                Text(
                    text = NumbersUtil.COPtoString(interest),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.capital_value))
                Text(
                    text = NumbersUtil.COPtoString(capital),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun RecordBodyMedium(dateRange:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>){
    Column {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = dateRange)

            Text(text = stringResource(id = R.string.total_value), modifier = Modifier.weight(1f))
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
            Text(text = NumbersUtil.COPtoString(interest), modifier = Modifier.weight(1f))

            Text(text = stringResource(id = R.string.capital_value), modifier = Modifier.weight(1f))
            Text(text = NumbersUtil.COPtoString(capital), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun RecordBodyLarge(dateRange:String, capital:Double, interest:Double, moreOptionsStatus: MutableState<Boolean>){
    Column {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = dateRange)

            Text(text = stringResource(id = R.string.total_value), modifier = Modifier.weight(1f))
            Text(text = NumbersUtil.COPtoString(capital + interest), modifier = Modifier.weight(1f))

            IconButton(onClick = { moreOptionsStatus.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.more_vertical),
                    contentDescription = "See more"
                )
            }
            Text(
                text = stringResource(id = R.string.interest_value),
                modifier = Modifier.weight(1f)
            )
            Text(text = NumbersUtil.COPtoString(interest), modifier = Modifier.weight(1f))

            Text(text = stringResource(id = R.string.capital_value), modifier = Modifier.weight(1f))
            Text(text = NumbersUtil.COPtoString(capital), modifier = Modifier.weight(1f))
        }
    }
}