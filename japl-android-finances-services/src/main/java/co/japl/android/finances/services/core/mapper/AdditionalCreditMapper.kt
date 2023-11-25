package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.AdditionalCreditDTO


object AdditionalCreditMapper {
    fun mapper(additionalCreditDTO: AdditionalCreditDTO):co.japl.finances.core.dto.AdditionalCreditDTO{
        return co.japl.finances.core.dto.AdditionalCreditDTO(
            additionalCreditDTO.id,
            additionalCreditDTO.name,
            additionalCreditDTO.value,
            additionalCreditDTO.creditCode,
            additionalCreditDTO.startDate,
            additionalCreditDTO.endDate
        )
    }
}