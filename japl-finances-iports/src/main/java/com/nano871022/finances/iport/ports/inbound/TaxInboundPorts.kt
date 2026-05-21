package com.nano871022.finances.iport.ports.inbound

import com.nano871022.finances.iport.dto.TaxDeclarationDTO
import com.nano871022.finances.iport.dto.TaxHistoryDTO
import com.nano871022.finances.iport.dto.PatrimonyAssetDTO
import com.nano871022.finances.iport.dto.TaxProjectionDTO

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
