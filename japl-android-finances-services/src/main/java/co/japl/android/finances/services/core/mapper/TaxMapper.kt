package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.dto.TaxDTO

object TaxMapper {

    fun mapper(taxDTO: TaxDTO):co.com.japl.finances.iports.dtos.TaxDTO {
        return co.com.japl.finances.iports.dtos.TaxDTO(
            taxDTO.id,
            taxDTO.month,
            taxDTO.year,
            taxDTO.status,
            taxDTO.codCreditCard,
            taxDTO.create,
            taxDTO.value,
            KindInterestRateEnum.findByOrdinal(taxDTO.kind),
            taxDTO.period,
            KindOfTaxEnum.findByValue(taxDTO.kindOfTax)
        )
    }
}