package co.com.japl.ui.components

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import co.com.japl.ui.theme.values.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatButton(imageVector:ImageVector,@StringRes descriptionIcon: Int,onClick: () -> Unit) {
    TooltipBox(tooltip = {
        Text(text = stringResource(id = descriptionIcon))
    },state = rememberTooltipState(), positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()) {
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
    TooltipBox(tooltip = {
        Text(text = stringResource(id = descriptionContent))
    },state = rememberTooltipState()
        , positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
        ) {
        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(
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
    TooltipBox(tooltip = {
        Text(text = stringResource(id = descriptionContent))
    },state = rememberTooltipState()
        , positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
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

@Composable
fun HelpWikiButton(@StringRes wikiUrl:Int,@StringRes descriptionContent:Int,tint:Color= LocalContentColor.current) {
    val wikiUrl = stringResource(id = wikiUrl).toUri()
    val context = LocalContext.current
    IconButton(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, wikiUrl)
        ContextCompat.startActivity(context,intent,null)
    }) {
        Icon(imageVector = Icons.Rounded.HelpOutline, contentDescription = stringResource(id = descriptionContent),tint=tint)
    }
}
