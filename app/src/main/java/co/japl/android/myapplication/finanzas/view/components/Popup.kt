package co.japl.android.myapplication.finanzas.view.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R

@Composable
fun Popup(@StringRes title:Int, state: MutableState<Boolean>, content:@Composable() ()->Unit){
    if(state.value) {
        Dialog(onDismissRequest = { state.value = false }) {


            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Title(stringResource(id = title), state)

                content.invoke()
            }
        }
    }
}

@Composable
private fun Title(title:String,state: MutableState<Boolean>){
    Row(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )
        IconButton(onClick = {
            state.value = !state.value
        },modifier=Modifier) {
            Text(text = "X")
        }
    }
    Divider()
}

@Composable
@Preview
fun Preview(){
    MaterialThemeComposeUI {
        Popup(R.string.recap_bought_cc, remember { mutableStateOf(true) }) {
            Text(text = "hola")
        }
    }
}