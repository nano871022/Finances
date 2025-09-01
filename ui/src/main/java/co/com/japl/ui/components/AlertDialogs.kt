package co.com.japl.ui.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import co.com.alameda181.ui.R
import co.com.japl.ui.R
import co.com.japl.ui.theme.MaterialThemeComposeUI


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogOkCancel(@StringRes title:Int,@StringRes confirmNameButton:Int,onDismiss: () -> Unit, onClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel), color= MaterialTheme.colorScheme.onSurface)
            }
        },
        confirmButton = {
            TextButton(onClick = onClick) {
                Text(text = stringResource(id = confirmNameButton), color= MaterialTheme.colorScheme.onSurface)
            }
        }
        , title = { Text(text = stringResource(id = title)) })
}

@Composable
fun AlertDialogInputValue(@StringRes title:Int,@StringRes message:Int,@StringRes confirmNameButton:Int,onDismiss: () -> Unit, onClick: (String) -> Unit){
    var value by remember {mutableStateOf("")}
    var hasErrorState = remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel), color= MaterialTheme.colorScheme.onSurface)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                hasErrorState.value = value.isEmpty()
                if(!hasErrorState.value) {
                    onClick.invoke(value)
                }
            }) {
                Text(text = stringResource(id = confirmNameButton),
                    color= MaterialTheme.colorScheme.onSurface)
            }
        },
        title = {Text(text = stringResource(id = title))},
        text = {
            FieldText(
                title = stringResource(id = message),
                callback = {
                    value = it
                },
                hasErrorState = hasErrorState,
                clearTitle = R.string.clear
                )
        }

    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun previewDialogOkCancel(){
    MaterialThemeComposeUI {
        AlertDialogOkCancel(title = R.string.any_data_to_show,
            confirmNameButton = R.string.ok,
            onDismiss = { /*TODO*/ }) {
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun previewDialogInputValue(){
    MaterialThemeComposeUI {
        AlertDialogInputValue(
            title = R.string.any_data_to_show,
            message = R.string.see_more,
            confirmNameButton = R.string.ok,
            onDismiss = { /*TODO*/ },
            onClick = {})

    }
}