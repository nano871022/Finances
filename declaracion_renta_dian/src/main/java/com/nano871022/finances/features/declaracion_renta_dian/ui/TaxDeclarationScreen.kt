package com.nano871022.finances.features.declaracion_renta_dian.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nano871022.finances.features.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
import com.nano871022.finances.iport.dto.*
import java.math.BigDecimal

@Composable
fun TaxDeclarationScreen(viewModel: TaxDeclarationViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val titles = listOf("Estimator", "History", "Projection")

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTab) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> EstimatorTab(viewModel)
                1 -> HistoryTab(viewModel)
                2 -> ProjectionTab(viewModel)
            }
        }
    }
}

@Composable
fun EstimatorTab(viewModel: TaxDeclarationViewModel) {
    val declaration by viewModel.declarationState.collectAsState()
    val assets by viewModel.assetsState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            declaration?.let {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Fiscal Year: ${it.fiscalYear}", style = MaterialTheme.typography.titleLarge)
                        Text("Tax Value (COP): $${it.taxValueCOP}", style = MaterialTheme.typography.headlineMedium)

                        val color = if (it.isObligatedToFile) Color.Red else Color.Green
                        Text(
                            text = if (it.isObligatedToFile) "Obligated to File" else "Not Obligated to File",
                            color = color,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        it.reasons.forEach { reason ->
                            Text("- $reason", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        item {
            Text("Manual Patrimony Assets", style = MaterialTheme.typography.titleMedium)
        }

        items(assets) { asset ->
            ListItem(
                headlineContent = { Text(asset.name) },
                supportingContent = { Text("$${asset.value}") },
                trailingContent = {
                    IconButton(onClick = { viewModel.deleteAsset(asset.id!!) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }

        item {
            Button(onClick = { viewModel.addAsset("House", BigDecimal("200000000"), "Real Estate") }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add Mock Asset")
            }
        }
    }
}

@Composable
fun HistoryTab(viewModel: TaxDeclarationViewModel) {
    val history by viewModel.historyState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(history) { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                ListItem(
                    headlineContent = { Text("Year ${item.fiscalYear}") },
                    supportingContent = { Text("Calculated on ${item.date}") },
                    trailingContent = { Text("$${item.taxValueCOP}") }
                )
            }
        }
    }
}

@Composable
fun ProjectionTab(viewModel: TaxDeclarationViewModel) {
    val projections by viewModel.projectionState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(projections) { proj ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Projection ${proj.year}", style = MaterialTheme.typography.titleLarge)
                    Text("YTD: $${proj.currentYTD}")
                    Text("Projected: $${proj.projectedEndOfYear}")
                    Text("Threshold: $${proj.threshold}")

                    val color = when (proj.limitStatus) {
                        LimitStatus.SAFE -> Color.Green
                        LimitStatus.WARNING -> Color.Yellow
                        LimitStatus.EXCEEDED -> Color.Red
                    }
                    LinearProgressIndicator(
                        progress = (proj.projectedEndOfYear.divide(proj.threshold, 2, java.math.RoundingMode.HALF_UP).toFloat()).coerceAtMost(1f),
                        color = color,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
