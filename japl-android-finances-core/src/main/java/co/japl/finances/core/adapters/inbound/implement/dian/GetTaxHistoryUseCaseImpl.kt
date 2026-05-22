package co.japl.finances.core.adapters.inbound.implement.dian

import co.com.japl.finances.iports.dtos.TaxHistoryDTO
import co.com.japl.finances.iports.inbounds.common.GetTaxHistoryUseCase
import co.com.japl.finances.iports.outbounds.TaxHistoryPersistencePort
import javax.inject.Inject

class GetTaxHistoryUseCaseImpl @Inject constructor(
    private val persistencePort: TaxHistoryPersistencePort
) : GetTaxHistoryUseCase {
    override suspend fun getTaxHistory(): List<TaxHistoryDTO> = persistencePort.getHistory()
}
