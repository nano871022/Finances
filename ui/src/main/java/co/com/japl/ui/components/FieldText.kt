package co.com.japl.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.ui.R
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.CurrencyVisualTransformation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldText( title:String
              ,value:String=""
              ,icon:ImageVector? = null
               ,keyboardType: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
              ,modifier:Modifier=Modifier
              ,validation:()->Unit = {}
              ,hasErrorState:MutableState<Boolean> = mutableStateOf(false)
              ,callback:(String)->Unit={}){
    val valueState = remember { mutableStateOf("") }
    value?.takeIf { value.isNotEmpty() }?.let{valueState.value = it}

    TextField(value = valueState.value , onValueChange = {
        valueState.value = it
        callback.invoke(it)
        validation.invoke()
    }, isError = hasErrorState.value, label = {
        Text(text = title)
    }, trailingIcon = {
        valueState.value.takeIf { it.isNotEmpty() }?.let {
            IconButton(onClick = {
                valueState.value= ""
                callback.invoke(valueState.value)
            }) {
                icon?.let {
                    Icon(
                        imageVector = icon, contentDescription = stringResource(
                            id = R.string.clear
                        )
                    )
                }
            }
        }
    }, modifier = modifier
        , keyboardOptions = keyboardType
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldText( title:String
               ,value:String=""
               ,icon:ImageVector? = null
               ,keyboardType: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
               , currency:Boolean = false
               ,modifier:Modifier=Modifier
               ,validation:()->Unit = {}
               ,hasErrorState:MutableState<Boolean> = mutableStateOf(false)
               ,callback:(String)->Unit={}){
    val valueState = remember { mutableStateOf("") }
    value?.takeIf { value.isNotEmpty() }?.let{valueState.value = it}

    TextField(value = valueState.value , onValueChange = {
        valueState.value = it
        callback.invoke(it)
        validation.invoke()
    }, isError = hasErrorState.value, label = {
        Text(text = title)
    }, trailingIcon = {
        valueState.value.takeIf { it.isNotEmpty() }?.let {
            IconButton(onClick = {
                valueState.value= ""
                callback.invoke(valueState.value)
            }) {
                icon?.let {
                    Icon(
                        imageVector = icon, contentDescription = stringResource(
                            id = R.string.clear
                        )
                    )
                }
            }
        }
    }, modifier = modifier
        , visualTransformation = CurrencyVisualTransformation(valueState.value)
        , prefix = {
            if (valueState.value.isNotEmpty() && currency)
                Text(text = "$")
        }
        , keyboardOptions = keyboardType
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview
fun FieldTextPreview(){
    val hasError = remember {mutableStateOf(true)}
    val value = remember { mutableStateOf("1000000") }
    MaterialThemeComposeUI {
        FieldText("Title Test"
            , value=value.value
            ,hasErrorState=hasError
            , callback = {value.value = it}
            , currency = true
            ,validation = { hasError.value = true}
            ,modifier=Modifier.fillMaxWidth().padding(5.dp)
            ,icon= Icons.Rounded.Cancel)

        Text(text = value.value)
    }

}