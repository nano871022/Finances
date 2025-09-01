package co.com.japl.ui.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
            , modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        ,color=MaterialTheme.colorScheme.onBackground)
        Checkbox(checked = activeState.value, onCheckedChange = {
            callback.invoke(it)
            activeState.value = it
        })
    }
}

@Composable
fun CheckBoxField(customTitle:@Composable() (RowScope.(color: Color, modifier: Modifier) -> Unit),
                  description:@Composable() (Color,Modifier)->Unit = {color,modifier->},
                  value: Boolean,callback:(Boolean)->Unit,
                  customColor:Color = MaterialTheme.colorScheme.onBackground,
                  customModifier:Modifier = Modifier){
    val state = remember { mutableStateOf(false) }
    value.let{
        state.value = value
    }
    Column (modifier = Modifier.fillMaxWidth()){
        Row (modifier = customModifier, verticalAlignment = Alignment.CenterVertically) {
            customTitle(customColor,customModifier)

            Checkbox(checked = state.value,
                onCheckedChange = {
                state.value = it
                callback.invoke(it)
            })
        }

       description(customColor,customModifier)
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = false)
fun CheckBoxPreview(){
    MaterialThemeComposeUI {
        CheckBoxField(title = "title", value =true , callback = {} )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF111111)
fun CheckBoxPreviewDark(){
    MaterialThemeComposeUI {
        CheckBoxField(title = "title", value =true , callback = {} )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun CheckBoxFieldPreview(){
    val state= remember {
        mutableStateOf(true)
    }
    MaterialThemeComposeUI {
        CheckBoxField(customTitle = { color: Color, modifier: Modifier ->
            Row (modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "Title Test", modifier = modifier.weight(1f), color = color)

                Text(text = "$ 100.506.500,00", textAlign = TextAlign.End, modifier = modifier, color = color)
            }
        } , value = state.value, callback = {state.value = it}, customModifier=Modifier)
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xFF111111 ,  uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CheckBoxFieldPreviewDark(){
    val state= remember {
        mutableStateOf(true)
    }
    MaterialThemeComposeUI {
        CheckBoxField(customTitle = {color,modifier->
            Row (modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "Title Test", modifier = modifier.weight(1f), color = color)

                Text(text = "$ 100.506.500,00", textAlign = TextAlign.End, modifier = modifier, color = color)
            }
        }, value =state.value , callback = {
            state.value = it
        }, description = { color,modifier->
            Text(text = "Description Test", modifier = modifier, color = color)
        },
            customModifier=Modifier
        )
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xFF111111 ,  uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CheckBoxFieldPreviewDark2(){
    val state= remember {mutableStateOf(false)}
    MaterialThemeComposeUI {
        CheckBoxField(customTitle = {color,modifier->
            Row (modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "Title Test", modifier = modifier.weight(1f), color = color)

                Text(text = "$ 100.506.500,00", textAlign = TextAlign.End, modifier = modifier, color = color)
            }
        }, value =state.value , callback = {
            state.value = !it
        }, description = { color,modifier->
            Text(text = "Description Test", modifier = modifier, color = color)
        },
            customModifier=Modifier
        )
    }

}