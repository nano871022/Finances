package com.nano871022.finances.core.domain.inbound

import com.nano871022.finances.iport.dto.PatrimonyAssetDTO
import com.nano871022.finances.iport.ports.inbound.SavePatrimonyAssetUseCase
import com.nano871022.finances.iport.ports.outbound.PatrimonyPersistencePort
import javax.inject.Inject

class SavePatrimonyAssetUseCaseImpl @Inject constructor(
    private val persistencePort: PatrimonyPersistencePort
) : SavePatrimonyAssetUseCase {
    override suspend fun saveAsset(asset: PatrimonyAssetDTO) = persistencePort.saveAsset(asset)
    override suspend fun getAssets(): List<PatrimonyAssetDTO> = persistencePort.getAssets()
    override suspend fun deleteAsset(id: Long) = persistencePort.deleteAsset(id)
}
