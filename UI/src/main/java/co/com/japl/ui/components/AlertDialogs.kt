package co.com.japl.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
//import co.com.alameda181.ui.R
import co.com.japl.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogOkCancel(@StringRes title:Int,@StringRes confirmNameButton:Int,onDismiss: () -> Unit, onClick: () -> Unit) {
    androidx.compose.material3.AlertDialog(
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