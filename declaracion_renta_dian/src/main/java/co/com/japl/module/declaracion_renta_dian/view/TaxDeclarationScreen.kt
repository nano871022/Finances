package co.com.japl.module.declaracion_renta_dian.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.PatrimonyAssetDTO
import co.com.japl.finances.iports.dtos.TaxDeclarationDTO
import co.com.japl.finances.iports.dtos.TaxHistoryDTO
import co.com.japl.finances.iports.dtos.TaxProjectionDTO
import co.com.japl.finances.iports.inbounds.common.GetTaxDeclarationUseCase
import co.com.japl.finances.iports.inbounds.common.GetTaxHistoryUseCase
import co.com.japl.finances.iports.inbounds.common.GetTaxProjectionUseCase
import co.com.japl.finances.iports.inbounds.common.SavePatrimonyAssetUseCase
import co.com.japl.module.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
import co.com.japl.module.declaracion_renta_dian.R
import co.com.japl.ui.theme.MaterialThemeComposeUI

@Composable
fun TaxDeclarationScreen(viewModel: TaxDeclarationViewModel) {
    val selectedTab = remember { mutableStateOf(0) }


    Scaffold (
        topBar = {TopBar(selectedTab)},
        containerColor = MaterialTheme.colorScheme.background
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

@Preview
@Composable
private fun TaxDeclarationPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        TaxDeclarationScreen(vm)
    }
}

@Preview( showSystemUi = true , showBackground = true ,uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaxDeclarationDarkPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        TaxDeclarationScreen(vm)
    }
}

@Composable
private fun getViewModel(): TaxDeclarationViewModel{
    return TaxDeclarationViewModel(
        object : GetTaxDeclarationUseCase{
            override suspend fun getTaxDeclaration(year: Int): TaxDeclarationDTO {
                TODO("Not yet implemented")
            }
        },
     object:GetTaxHistoryUseCase{
         override suspend fun getTaxHistory(): List<TaxHistoryDTO> {
             TODO("Not yet implemented")
         }
     },
        object:GetTaxProjectionUseCase{
            override suspend fun getProjection(year: Int): List<TaxProjectionDTO> {
                TODO("Not yet implemented")
            }
        },
        object:SavePatrimonyAssetUseCase{
            override suspend fun saveAsset(asset: PatrimonyAssetDTO) {
                TODO("Not yet implemented")
            }

            override suspend fun getAssets(): List<PatrimonyAssetDTO> {
                TODO("Not yet implemented")
            }

            override suspend fun deleteAsset(id: Long) {
                TODO("Not yet implemented")
            }
        }
    )
}
