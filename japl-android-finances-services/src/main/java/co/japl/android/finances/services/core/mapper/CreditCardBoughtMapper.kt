package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditCardBoughtDTO

object CreditCardBoughtMapper {

    fun mapper(creditCardBoughtDTO: CreditCardBoughtDTO):co.japl.finances.core.dto.CreditCardBoughtDTO{
        return co.japl.finances.core.dto.CreditCardBoughtDTO(
            id = creditCardBoughtDTO.id,
            nameCreditCard = creditCardBoughtDTO.nameCreditCard,
            nameItem = creditCardBoughtDTO.nameItem,
            valueItem = creditCardBoughtDTO.valueItem,
            interest = creditCardBoughtDTO.interest,
            month = creditCardBoughtDTO.month,
            boughtDate = creditCardBoughtDTO.boughtDate,
            cutOutDate = creditCardBoughtDTO.cutOutDate,
            createDate = creditCardBoughtDTO.createDate,
            endDate = creditCardBoughtDTO.endDate,
            recurrent = creditCardBoughtDTO.recurrent,
            kind = creditCardBoughtDTO.kind,
            kindOfTax = creditCardBoughtDTO.kindOfTax,
            codeCreditCard = creditCardBoughtDTO.codeCreditCard
        )
    }
}