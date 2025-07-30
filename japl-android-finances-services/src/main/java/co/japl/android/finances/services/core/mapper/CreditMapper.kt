package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.japl.android.finances.services.dto.CreditDTO
import co.japl.android.finances.services.enums.KindOfPayListEnum
import co.com.japl.finances.iports.dtos.CreditDTO as CreditDTo
import co.japl.android.finances.services.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum as KindOfTaxEnumI

object CreditMapper {

    fun mapper(creditDTO: CreditDTO):CreditDTo{
        return CreditDTo(
            creditDTO.id,
            creditDTO.name,
            creditDTO.date,
            creditDTO.tax,
            creditDTO.periods,
            creditDTO.value,
            creditDTO.quoteValue,
            KindPaymentsEnums.findByIndex( creditDTO.kindOf.getMonths()),
            KindOfTaxEnumI.findByValue(creditDTO.kindOfTax.name)
        )
    }

    fun mapper(creditDTO: CreditDTo):CreditDTO{
        return CreditDTO(
            creditDTO.id,
            creditDTO.name,
            creditDTO.date,
            creditDTO.tax,
            creditDTO.periods,
            creditDTO.value,
            creditDTO.quoteValue,
            KindOfPayListEnum.actions.findByMonths(creditDTO.kindOf.month),
            KindOfTaxEnum.valueOf(creditDTO.kindOfTax.value)
        )
    }
}