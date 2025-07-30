package co.japl.android.credit.view.extavalue

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import co.japl.android.credit.R
import co.com.japl.ui.components.FieldText

@Composable
fun AddValueAmortizationDialog(
    onDismiss: () -> Unit,
    onSave: (Int, Double) -> Unit
) {
    val numQuotes = remember { mutableStateOf("") }
    val value = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.add_extra_value)) },
        text = {
            FieldText(
                value = numQuotes.value,
                onValueChange = { numQuotes.value = it },
                label = stringResource(id = R.string.number_of_quotes)
            )
            FieldText(
                value = value.value,
                onValueChange = { value.value = it },
                label = stringResource(id = R.string.value)
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val numQuotesInt = numQuotes.value.toIntOrNull() ?: 0
                val valueDouble = value.value.toDoubleOrNull() ?: 0.0
                onSave(numQuotesInt, valueDouble)
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
