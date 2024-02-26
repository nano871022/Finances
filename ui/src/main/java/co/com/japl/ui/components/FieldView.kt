package co.com.japl.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.ui.theme.MaterialThemeComposeUI

@Composable
fun FieldView(@StringRes name:Int, value:String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onPrimaryContainer,isMoney:Boolean = true){
    FieldView(name = stringResource(id = name  ), value = value , modifier = modifier, color = color,isMoney)
}


@Composable
fun FieldView(name:String, value:String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onPrimaryContainer,isMoney:Boolean = true) {
    Box(
        modifier = modifier
            .padding(top = 2.dp, start = 3.dp, end = 3.dp, bottom = 2.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(top = 2.dp, start = 5.dp, end = 5.dp, bottom = 2.dp)
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            softWrap = false,
            modifier = Modifier
        )

        if (isMoney) {
            Text(
                text = "$", color = color, modifier = Modifier
                    .padding(top = 22.dp, start = 1.dp, end = 5.dp, bottom = 2.dp)
            )
        }

        Text(
            text = "$value", color = color, modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(top = 22.dp, start = 10.dp, end = 1.dp, bottom = 2.dp)
        )

    }

}

