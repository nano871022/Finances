package co.com.japl.module.credit.views.popups

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import co.com.japl.module.credit.R
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.Popup
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodGrace(popUpState: MutableState<Boolean>, onClick:(Int,LocalDate)->Unit){
    var currentDate = LocalDate.now()
    var previousMonthState = remember { mutableStateOf(false) }
    var currentMonthState = remember { mutableStateOf(true) }
    var nextMonthState = remember { mutableStateOf(false) }
    var periodsState = remember { mutableIntStateOf(1) }
    var dateStartState = remember { mutableStateOf(currentDate.withDayOfMonth(1)) }
    var dateEndState = remember { mutableStateOf(currentDate.plusMonths(periodsState.intValue.toLong()).plusMonths(1).withDayOfMonth(1).minusDays(1)) }

        Popup(
            title = R.string.period_grace,
            state = popUpState
            ) {

            Column(modifier = Modifier.padding(5.dp)) {

                FieldText(title = stringResource(id = R.string.periods),
                    value = periodsState.intValue.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    validation = {
                        Log.d(this.javaClass.name, "val Periods ${periodsState.intValue}")
                    },
                    callback = {
                        it.takeIf { it.isNotBlank() && it.isDigitsOnly() }?.let {
                            periodsState.intValue = it.toInt()
                            dateEndState.value =
                                dateStartState.value.plusMonths(periodsState.intValue.toLong())
                        }
                    })
                Row {
                    OptRadioButton(
                        name = R.string.last_month,
                        selected = previousMonthState,
                        onClick = {
                            dateStartState.value = currentDate.minusMonths(1).withDayOfMonth(1)
                            dateEndState.value =
                                dateStartState.value.plusMonths(periodsState.intValue.toLong()).withDayOfMonth(1).plusMonths(1).minusDays(1)
                            previousMonthState.value = true
                            currentMonthState.value = false
                            nextMonthState.value = false
                        })
                }
                Row {
                    OptRadioButton(
                        name = R.string.current_month,
                        selected = currentMonthState,
                        onClick = {
                            dateStartState.value = currentDate.withDayOfMonth(1)
                            dateEndState.value =
                                dateStartState.value.plusMonths(periodsState.intValue.toLong()).withDayOfMonth(1).plusMonths(1).minusDays(1)
                            previousMonthState.value = false
                            currentMonthState.value = true
                            nextMonthState.value = false
                        })
                }
                Row {
                    OptRadioButton(
                        name = R.string.next_month,
                        selected = nextMonthState,
                        onClick = {
                            dateStartState.value = currentDate.plusMonths(1).withDayOfMonth(1)
                            dateEndState.value =
                                dateStartState.value.plusMonths(periodsState.intValue.toLong()).withDayOfMonth(1).plusMonths(1).minusDays(1)
                            previousMonthState.value = false
                            currentMonthState.value = false
                            nextMonthState.value = true
                        })
                }

                FieldText(title = stringResource(id = R.string.start_date),
                    value = dateStartState.value?.let { DateUtils.localDateToStringDB(it) } ?: "",
                    validation = { /*TODO*/ },
                    callback = {
                        it.takeIf { it.isNotBlank() && DateUtils.isDateValid(it) }
                            ?.let { dateStartState.value = DateUtils.toLocalDate(it) }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )

                FieldText(title = stringResource(id =  R.string.end_date),
                    value = dateEndState.value?.let { DateUtils.localDateToStringDB(it) } ?: "",
                    validation = { /*TODO*/ },
                    callback = {
                        it.takeIf { it.isNotBlank() && DateUtils.isDateValid(it) }
                            ?.let { dateEndState.value = DateUtils.toLocalDate(it) }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )

                Row {
                    Button(onClick = { popUpState.value = false }, modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }

                    Button(onClick = {
                        onClick.invoke(periodsState.intValue, dateStartState.value)
                        popUpState.value = false
                    }, modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = R.string.save),
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
}

@Composable
private fun RowScope.OptRadioButton(@StringRes name:Int,selected:MutableState<Boolean>,onClick:()->Unit){
    RadioButton(selected = selected.value, onClick = onClick)
    Text(text = stringResource(id = name),
        color = MaterialTheme.colorScheme.onBackground,
        modifier= Modifier
            .align(alignment = androidx.compose.ui.Alignment.CenterVertically)
            .clickable {
                onClick.invoke()
            })
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview(showBackground = true, showSystemUi = false)
fun PeriodGracePreviewLight(){
val state = remember{ mutableStateOf(false)}
    MaterialThemeComposeUI {

        PeriodGrace(state, onClick = {period,date->

        })
    }

}