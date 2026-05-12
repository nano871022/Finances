package co.japl.android.myapplication.finanzas.view.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.com.japl.finances.iports.enums.LLMType
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun LLMConnectionList(onNavigateToForm: (LLMType) -> Unit) {
    val providers = LLMType.values()

    Scaffold { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(providers) { provider ->
                ListItem(
                    headlineContent = { Text(provider.name) },
                    modifier = Modifier.clickable { onNavigateToForm(provider) }
                )
                HorizontalDivider()
            }
        }
    }
}
