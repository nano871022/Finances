package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.AdditionalCreditDTO
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO as AdditionalCreditDTOs

object AddAdditionalValueMapper {

    fun mapper(entity: AdditionalCreditDTO):AdditionalCreditDTOs{
       return AdditionalCreditDTOs(
            id=entity.id,
            name=entity.name,
            value=entity.value,
            creditCode = entity.creditCode,
            startDate=entity.startDate,
            endDate=entity.endDate

        )
    }

}