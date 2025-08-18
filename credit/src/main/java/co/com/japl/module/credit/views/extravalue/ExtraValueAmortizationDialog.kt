package co.com.japl.module.credit.views.extravalue

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.credit.R
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.theme.MaterialThemeComposeUI

@Composable
fun ExtraValueAmortizationDialog(
    onDismiss: () -> Unit,
    onSave: (Int, Double) -> Unit
) {
    val numQuotes = remember { mutableStateOf("") }
    val value = remember { mutableStateOf("") }
    val errorNumQuotes = remember { mutableStateOf(false) }
    val errorValue = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.add_extra_value)) },
        text = {
            Column {
                FieldText(
                    value = numQuotes.value,
                    hasErrorState = errorNumQuotes,
                    keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
                    callback = { numQuotes.value = it },
                    title = stringResource(id = R.string.number_of_quotes)
                )
                FieldText(
                    value = value.value,
                    hasErrorState = errorValue.value,
                    currency=true,
                    keyboardType = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    callback = { value.value = it },
                    title = stringResource(id = R.string.value)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val numQuotesInt = numQuotes.value.toIntOrNull() ?: 0
                val valueDouble = value.value.toDoubleOrNull() ?: 0.0
                if(numQuotesInt == 0){
                    errorNumQuotes.value = true
                }
                if(valueDouble == 0.0){
                    errorValue.value = true
                }
                if(errorValue.value.not() && errorNumQuotes.value.not()) {
                    onSave(numQuotesInt, valueDouble)
                }
            }) {
                Text(text = stringResource(id = R.string.save)
                ,color= MaterialTheme.colorScheme.onSurface)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel)
                    ,color= MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = false, showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun PreviewLight(){
    MaterialThemeComposeUI {
        ExtraValueAmortizationDialog(
            onDismiss = {},
            onSave = { _, _ -> }
        )

    }
}