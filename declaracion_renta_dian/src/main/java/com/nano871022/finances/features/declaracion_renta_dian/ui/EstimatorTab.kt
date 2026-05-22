package com.nano871022.finances.features.declaracion_renta_dian.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.japl.android.myapplication.utils.NumbersUtil
import com.nano871022.finances.features.declaracion_renta_dian.R
import com.nano871022.finances.features.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
import com.nano871022.finances.iport.dto.TaxDeclarationDTO
import java.math.BigDecimal

@Composable
fun EstimatorTab(viewModel: TaxDeclarationViewModel) {
    val declaration by viewModel.declarationState.collectAsState()
    val assets by viewModel.assetsState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            declaration?.let { CardRecap(it) }
        }

        item {
            Text(text=stringResource(R.string.manual_patrimony_assets),
                color=MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium)
        }

        items(assets) { asset ->
            ListItem(
                headlineContent = { Text(text=asset.name, color= MaterialTheme.colorScheme.onBackground) },
                supportingContent = { Text(text=NumbersUtil.COPtoString(asset.value),color= MaterialTheme.colorScheme.onBackground) },
                trailingContent = {
                    IconButton(onClick = { viewModel.deleteAsset(asset.id!!) }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                    }
                }
            )
        }

        item {
            Button(onClick = {
                viewModel.addAsset("House", BigDecimal(200000000), "Real Estate") }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(text=stringResource(R.string.add_mock_asset))
            }
        }
    }
}

@Composable
private fun CardRecap(declaration: TaxDeclarationDTO){
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.fiscal_year,declaration.fiscalYear),
                color= MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.tax_value, NumbersUtil.COPtoString(declaration.taxValueCOP)),
                color= MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium)

            val color = if (declaration.isObligatedToFile) MaterialTheme.colorScheme.onError
                        else MaterialTheme.colorScheme.onSecondary
            Text(
                text = if (declaration.isObligatedToFile) stringResource(R.string.obligated_file)
                        else stringResource(R.string.not_oblicated_file),
                color = color,
                style = MaterialTheme.typography.bodyLarge
            )

            declaration.reasons.forEach { reason ->
                Text("- $reason",
                    color= MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}