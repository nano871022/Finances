package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditDTO

object CreditMapper {

    fun mapper(creditDTO: CreditDTO):co.japl.finances.core.dto.CreditDTO{
        return co.japl.finances.core.dto.CreditDTO(
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