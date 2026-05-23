package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.dtos.TaxDeclarationDTO
import co.com.japl.finances.iports.dtos.TaxHistoryDTO
import co.com.japl.finances.iports.dtos.PatrimonyAssetDTO
import co.com.japl.finances.iports.dtos.TaxProjectionDTO

interface GetTaxDeclarationUseCase {
    suspend fun getTaxDeclaration(year: Int): TaxDeclarationDTO
}

interface GetTaxHistoryUseCase {
    suspend fun getTaxHistory(): List<TaxHistoryDTO>
}

interface SavePatrimonyAssetUseCase {
    suspend fun saveAsset(asset: PatrimonyAssetDTO)
    suspend fun getAssets(): List<PatrimonyAssetDTO>
    suspend fun deleteAsset(id: Long)
}

interface GetTaxProjectionUseCase {
    suspend fun getProjection(year: Int): List<TaxProjectionDTO>
}
