package co.com.japl.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.com.japl.ui.R
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.DateUtils

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldDatePicker(@StringRes title:Int,value:String,isError:MutableState<Boolean> = mutableStateOf(false),validation:()->Unit,callable:(String)->Unit,modifier:Modifier = Modifier) {
    val state = rememberDatePickerState()
    val stateDatePicker = remember { mutableStateOf(false) }
    val stateValue = remember { mutableStateOf(value) }

    Box(modifier = modifier
        .background(color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
        .border(1.dp, Color.Unspecified, shape = ShapeDefaults.ExtraLarge)
        .clickable {
            stateDatePicker.value = true
        }) {
        Column {

            Text(
                text =   stringResource(id = title)
                , fontSize = if(stateValue.value.isNotEmpty()){12.sp}else{
                    TextUnit.Unspecified}
                ,color = if(isError.value){
                    MaterialTheme.colorScheme.error}else{
                    MaterialTheme.colorScheme.onSurface}
                ,modifier = modifier
            )

            if(stateValue.value.isNotEmpty()){
                Row {

                    Text(text = stateValue.value
                        ,modifier = modifier.weight(1f).align(alignment = Alignment.CenterVertically))

                    IconButton(onClick = { stateValue.value = "" }) {
                        Icon(imageVector = Icons.Rounded.Cancel, contentDescription = "Clear")
                    }
                }
            }
        }
    }
    if(stateDatePicker.value){
        DatePickerDialog(
            onDismissRequest = {
                stateDatePicker.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    stateValue.value = state.selectedDateMillis?.let {
                        DateUtils.localDateToStringDate(DateUtils.toLocalDate(it))
                    }?:""
                    stateDatePicker.value = false
                    callable.invoke(stateValue.value)
                    validation.invoke()
                }){
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { stateDatePicker.value = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }

            }
        ){
            DatePicker(state = state)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun FieldDatePickerPreview(){
    MaterialThemeComposeUI {
        FieldDatePicker(title = androidx.compose.material3.R.string.date_picker_headline
            ,value = "2022-10-01"
            , callable = {}
            , isError = mutableStateOf(true)
            , validation = {}
            , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))
    }
}