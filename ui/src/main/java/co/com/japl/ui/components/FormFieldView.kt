package co.com.japl.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.MaterialThemeComposeUI

@Composable
fun FieldView(title:String,value:String = "", prefix:@Composable ()->Unit={}
              ,modifier: Modifier = Modifier
){
    TextField(value = value,
        onValueChange = {},
        label = { Text(text = title) },
        prefix = prefix,
        readOnly = true,
        modifier = modifier)
}

@Composable
fun FieldView(title:String,value:String = "", prefix:@Composable ()->Unit={}, suffix:@Composable ()->Unit={}
              ,modifier: Modifier = Modifier
){
    TextField(value = value,
        onValueChange = {},
        label = { Text(text = title) },
        prefix = prefix,
        suffix = suffix,
        readOnly = true,
        modifier = modifier)
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun FieldViewPreview(){
    MaterialThemeComposeUI {
            FieldView(
                title = "title", value="value", modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                , suffix = {
                    Icon(imageVector = Icons.Rounded.AddCircleOutline, contentDescription = "")
                }
            )
    }
}