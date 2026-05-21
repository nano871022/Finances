package com.nano871022.finances.core.domain.inbound

import com.nano871022.finances.iport.dto.TaxHistoryDTO
import com.nano871022.finances.iport.ports.inbound.GetTaxHistoryUseCase
import com.nano871022.finances.iport.ports.outbound.TaxHistoryPersistencePort
import javax.inject.Inject

class GetTaxHistoryUseCaseImpl @Inject constructor(
    private val persistencePort: TaxHistoryPersistencePort
) : GetTaxHistoryUseCase {
    override suspend fun getTaxHistory(): List<TaxHistoryDTO> = persistencePort.getHistory()
}
