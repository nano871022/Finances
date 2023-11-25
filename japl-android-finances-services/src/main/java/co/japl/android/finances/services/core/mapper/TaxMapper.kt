package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.TaxDTO

object TaxMapper {

    fun mapper(taxDTO: TaxDTO):co.japl.finances.core.dto.TaxDTO {
        return co.japl.finances.core.dto.TaxDTO(
            taxDTO.id,
            taxDTO.month,
            taxDTO.year,
            taxDTO.status,
            taxDTO.codCreditCard,
            taxDTO.create,
            taxDTO.value,
            taxDTO.kind,
            taxDTO.period,
            taxDTO.kindOfTax
        )
    }
}