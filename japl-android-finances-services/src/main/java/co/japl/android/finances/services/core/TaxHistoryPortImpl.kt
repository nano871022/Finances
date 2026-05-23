package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.TaxHistoryDTO as IPortTaxHistoryDTO
import co.com.japl.finances.iports.outbounds.TaxHistoryPersistencePort
import co.japl.android.finances.services.dao.interfaces.ITaxHistoryDAO
import co.japl.android.finances.services.dto.TaxHistoryDTO
import javax.inject.Inject

class TaxHistoryPortImpl @Inject constructor(
    private val taxHistoryDAO: ITaxHistoryDAO
) : TaxHistoryPersistencePort {

    override suspend fun saveHistory(history: IPortTaxHistoryDTO) {
        taxHistoryDAO.save(
            TaxHistoryDTO(
                id = history.id,
                fiscalYear = history.fiscalYear,
                taxValueCOP = history.taxValueCOP,
                date = history.date
            )
        )
    }

    override suspend fun getHistory(): List<IPortTaxHistoryDTO> {
        return taxHistoryDAO.getAll().map {
            IPortTaxHistoryDTO(
                id = it.id ?: 0L,
                fiscalYear = it.fiscalYear,
                taxValueCOP = it.taxValueCOP,
                date = it.date
            )
        }
    }
}
