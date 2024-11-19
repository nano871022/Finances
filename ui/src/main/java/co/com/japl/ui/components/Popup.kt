package co.com.japl.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
//import co.com.alameda181.ui.R
import co.com.japl.ui.R
//import co.com.japl.ui.theme.MaterialThemeComposeUI

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

@OptIn(ExperimentalMaterial3Api::class)
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
                .align(alignment = Alignment.CenterVertically)
                .weight(2f)
        )
        TooltipBox(tooltip = { Text(text = stringResource(id = R.string.close)) }, state = rememberTooltipState(), positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()) {
            IconButton(onClick = {
                state.value = false
            }, modifier = Modifier) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(id = R.string.close)
                )
            }
        }
    }
    Divider()
}

