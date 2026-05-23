package co.com.japl.module.declaracion_renta_dian.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.com.japl.module.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
import co.com.japl.module.declaracion_renta_dian.R

@Composable
fun TaxDeclarationScreen(viewModel: TaxDeclarationViewModel) {
    val selectedTab = remember { mutableStateOf(0) }


    Scaffold(
        topBar = {TopBar(selectedTab)}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab.value) {
                0 -> EstimatorTab(viewModel)
                1 -> HistoryTab(viewModel)
                2 -> ProjectionTab(viewModel)
            }
        }
    }
}

@Composable
private fun TopBar(selectedTab: MutableState<Int>){
    val titles = listOf(
        stringResource(R.string.estimator),
        stringResource(R.string.history),
        stringResource(R.string.projection))

    TabRow(selectedTabIndex = selectedTab.value) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab.value == index,
                onClick = { selectedTab.value = index },
                text = {
                    Text(text=title,
                        color=MaterialTheme.colorScheme.onBackground)
                }
            )
        }
    }
}

