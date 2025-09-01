package co.com.japl.ui.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
//import co.com.alameda181.ui.R
//import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.R
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun FieldView(@StringRes name:Int, value:String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onPrimaryContainer,isMoney:Boolean = true,alignment:Alignment = Alignment.BottomEnd,fontSize:TextUnit = TextUnit.Unspecified,onClick:() -> Unit = {}){
    FieldView(name = stringResource(id = name  ), value = value , modifier = modifier, color = color,isMoney,alignment,fontSize,onClick)
}


@Composable
fun FieldView(name:String, value:String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onPrimaryContainer,isMoney:Boolean = true,alignment:Alignment = Alignment.BottomEnd,fontSize:TextUnit = TextUnit.Unspecified,onClick:() -> Unit = {}) {
    Box(
        modifier = modifier
            .padding(top = 2.dp, start = 3.dp, end = 3.dp, bottom = 2.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(top = 2.dp, start = 5.dp, end = 5.dp, bottom = 2.dp)
            .clickable { onClick.invoke() }
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            softWrap = false,
            modifier = Modifier
        )

        if (isMoney) {
            Text(
                text = "$", color = color,
                fontSize = fontSize,
                modifier = Modifier
                    .padding(top = 22.dp, start = 1.dp, end = 5.dp, bottom = 2.dp)
            )
        }

        Text(
            text = "$value", color = color,
            fontSize = fontSize,modifier = Modifier
                .align(alignment = alignment)
                .padding(top = 22.dp, start = 10.dp, end = 1.dp, bottom = 2.dp)
        )

    }

}


@Composable fun FieldViewCards(@StringRes name:Int, value:String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onPrimaryContainer){

    Row (modifier = modifier.fillMaxWidth()) {
        Text(text= stringResource(id = name), color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier
            .padding(Dimensions.PADDING_SHORT)
            .weight(1f))
        Text(text=value, color = color, modifier = Modifier
            .padding(Dimensions.PADDING_SHORT)
            .weight(1f))
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true)
fun FieldView1Preview() {
    MaterialThemeComposeUI {
        FieldView(
            name = "Name",
            value = "value",
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( uiMode = Configuration.UI_MODE_NIGHT_YES)
fun FieldView11Preview() {
    MaterialThemeComposeUI {
        FieldView(
            name = "Name",
            value = "value",
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true)
fun FieldView2Preview() {
    MaterialThemeComposeUI {
        FieldView(
            name = R.string.see_more,
            value = "value",
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(uiMode=Configuration.UI_MODE_NIGHT_YES)
fun FieldView21Preview() {
    MaterialThemeComposeUI {
        FieldView(
            name = R.string.see_more,
            value = "0,000.00",
            alignment = Alignment.Center,
            fontSize = TextUnit(48f, TextUnitType.Sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(uiMode=Configuration.UI_MODE_NIGHT_YES)
internal fun FieldViewPreviewCardsDark() {
    MaterialThemeComposeUI {
        FieldViewCards(
            name = R.string.clear, value = "10000", modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(uiMode=Configuration.UI_MODE_NIGHT_NO)
internal fun FieldViewPreviewCards() {
    MaterialThemeComposeUI {
        FieldViewCards(
            name = R.string.see_more, value = "10000", modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}

