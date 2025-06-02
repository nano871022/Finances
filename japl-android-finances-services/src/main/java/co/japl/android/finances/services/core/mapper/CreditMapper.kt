package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditDTO

object CreditMapper {

    fun mapper(creditDTO: CreditDTO):co.com.japl.finances.iports.dtos.CreditDTO{
        return co.com.japl.finances.iports.dtos.CreditDTO(
            creditDTO.id,
            creditDTO.name,
            creditDTO.date,
            creditDTO.tax,
            creditDTO.periods,
            creditDTO.value,
            creditDTO.quoteValue,
            creditDTO.kindOf,
            creditDTO.kindOfTax
        )
    }

    fun mapper(creditDTO: co.com.japl.finances.iports.dtos.CreditDTO):CreditDTO{
        return CreditDTO(
            creditDTO.id,
            creditDTO.name,
            creditDTO.date,
            creditDTO.tax,
            creditDTO.periods,
            creditDTO.value,
            creditDTO.quoteValue,
            creditDTO.kindOf,
            creditDTO.kindOfTax
        )
    }
}