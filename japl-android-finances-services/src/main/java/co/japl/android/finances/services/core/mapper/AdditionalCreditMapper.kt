package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.AdditionalCreditDTO


object AdditionalCreditMapper {

    fun mapper(additionalCreditDTO: AdditionalCreditDTO):co.com.japl.finances.iports.dtos.AdditionalCreditDTO{
        return co.com.japl.finances.iports.dtos.AdditionalCreditDTO(
            additionalCreditDTO.id,
            additionalCreditDTO.name,
            additionalCreditDTO.value,
            additionalCreditDTO.creditCode,
            additionalCreditDTO.startDate,
            additionalCreditDTO.endDate
        )
    }

    fun mapper(additionalCreditDTO: co.com.japl.finances.iports.dtos.AdditionalCreditDTO):AdditionalCreditDTO{
        return AdditionalCreditDTO(
            additionalCreditDTO.id,
            additionalCreditDTO.name,
            additionalCreditDTO.value,
            additionalCreditDTO.creditCode,
            additionalCreditDTO.startDate,
            additionalCreditDTO.endDate
        )
    }
}