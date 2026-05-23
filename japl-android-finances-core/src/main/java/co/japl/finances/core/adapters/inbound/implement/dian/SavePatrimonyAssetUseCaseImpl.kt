package co.japl.finances.core.adapters.inbound.implement.dian

import co.com.japl.finances.iports.dtos.PatrimonyAssetDTO
import co.com.japl.finances.iports.inbounds.common.SavePatrimonyAssetUseCase
import co.com.japl.finances.iports.outbounds.PatrimonyPersistencePort
import javax.inject.Inject

class SavePatrimonyAssetUseCaseImpl @Inject constructor(
    private val persistencePort: PatrimonyPersistencePort
) : SavePatrimonyAssetUseCase {
    override suspend fun saveAsset(asset: PatrimonyAssetDTO) = persistencePort.saveAsset(asset)
    override suspend fun getAssets(): List<PatrimonyAssetDTO> = persistencePort.getAssets()
    override suspend fun deleteAsset(id: Long) = persistencePort.deleteAsset(id)
}
