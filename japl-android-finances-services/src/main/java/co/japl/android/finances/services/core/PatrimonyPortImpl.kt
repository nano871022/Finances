package co.japl.android.finances.services.core

import com.nano871022.finances.iport.dto.PatrimonyAssetDTO
import com.nano871022.finances.iport.ports.outbound.PatrimonyPersistencePort
import co.japl.android.finances.services.dao.interfaces.IPatrimonyDAO
import co.japl.android.finances.services.dto.PatrimonyDTO
import javax.inject.Inject

class PatrimonyPortImpl @Inject constructor(
    private val patrimonyDAO: IPatrimonyDAO
) : PatrimonyPersistencePort {

    override suspend fun saveAsset(asset: PatrimonyAssetDTO) {
        patrimonyDAO.save(
            PatrimonyDTO(
                id = asset.id,
                name = asset.name,
                value = asset.value,
                type = asset.type
            )
        )
    }

    override suspend fun getAssets(): List<PatrimonyAssetDTO> {
        return patrimonyDAO.getAll().map {
            PatrimonyAssetDTO(
                id = it.id,
                name = it.name,
                value = it.value,
                type = it.type
            )
        }
    }

    override suspend fun deleteAsset(id: Long) {
        patrimonyDAO.delete(id)
    }
}
