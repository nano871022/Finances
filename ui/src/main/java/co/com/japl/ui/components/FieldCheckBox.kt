package co.com.japl.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.ui.theme.MaterialThemeComposeUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckBoxField(title:String, value:Boolean,callback:(Boolean)->Unit, modifier:Modifier = Modifier){
    val activeState = remember { mutableStateOf(false)}
    value?.let{
        activeState.value = value
    }
    Row (modifier = modifier){
        Text(text = title
            , modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically)
        ,color=MaterialTheme.colorScheme.onBackground)
        Checkbox(checked = activeState.value, onCheckedChange = {
            callback.invoke(it)
            activeState.value = it
        })
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CheckBoxPreview(){
    MaterialThemeComposeUI {
        CheckBoxField(title = "title", value =true , callback = {} )
    }
}