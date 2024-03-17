package co.com.japl.ui.components

import android.graphics.drawable.PaintDrawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.com.japl.ui.theme.values.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatButton(imageVector:ImageVector,@StringRes descriptionIcon: Int,onClick: () -> Unit) {
    PlainTooltipBox(tooltip = {
        Text(text = stringResource(id = descriptionIcon))
    }) {
        FloatingActionButton(
            onClick = onClick,
            elevation = FloatingActionButtonDefaults.elevation(10.dp),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ,modifier=Modifier.padding(Dimensions.PADDING_SHORT)
        ) {
            Icon(
                imageVector = imageVector, contentDescription = stringResource(
                    id = descriptionIcon
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButton(imageVector:ImageVector,@StringRes descriptionContent:Int , onClick:()->Unit,modifier:Modifier=Modifier){
    PlainTooltipBox(tooltip = {
        Text(text = stringResource(id = descriptionContent))
    },tooltipState = PlainTooltipState()
        ) {
        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            androidx.compose.material.Icon(
                imageVector = imageVector,
                contentDescription = stringResource(
                    id = descriptionContent
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButton(@DrawableRes painter:Int,@StringRes descriptionContent:Int , onClick:()->Unit,modifier:Modifier=Modifier){
    PlainTooltipBox(tooltip = {
        Text(text = stringResource(id = descriptionContent))
    },tooltipState = PlainTooltipState()
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            androidx.compose.material.Icon(
                painter = painterResource(id = painter),
                contentDescription = stringResource(
                    id = descriptionContent
                )
            )
        }
    }
}

