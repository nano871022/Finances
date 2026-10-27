package co.com.japl.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import co.com.japl.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldEditValue(
    initialValue: String,
    title: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    prefix: @Composable (() -> Unit)? = { Text(text = "$") },
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    var isFocused by remember { mutableStateOf(false) }
    var hasTyped by remember { mutableStateOf(false) }

    TextField(
        value = if (isFocused && !hasTyped) "" else text,
        onValueChange = { input ->
            if (input.all { it.isDigit() || it == '.' || it == ',' }) {
                text = input
                hasTyped = true
                onValueChange(input)
            }
        },
        placeholder = {
            if (isFocused && !hasTyped) {
                Text(text = initialValue, color = Color.Gray)
            }
        },
        textStyle = TextStyle(
            color = if (hasTyped) MaterialTheme.colorScheme.onSurface else Color.Gray
        ),
        label = { Text(text = title) },
        keyboardOptions = keyboardOptions,
        prefix = prefix,
        singleLine = true,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = {
                    text = ""
                    hasTyped = true
                    onValueChange("")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(id = R.string.clear)
                    )
                }
            }
        }
    )
}
