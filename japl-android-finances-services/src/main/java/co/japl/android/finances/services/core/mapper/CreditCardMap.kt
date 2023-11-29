package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditCardDTO

object CreditCardMap {

    fun mapper(creditCard:CreditCardDTO):co.com.japl.finances.iports.dtos.CreditCardDTO{
        return co.com.japl.finances.iports.dtos.CreditCardDTO(
            id = creditCard.id,
            name = creditCard.name,
            maxQuotes = creditCard.maxQuotes,
            cutOffDay = creditCard.cutOffDay,
            warningValue = creditCard.warningValue,
            create = creditCard.create,
            status = creditCard.status,
            interest1Quote = creditCard.interest1Quote,
            interest1NotQuote = creditCard.interest1NotQuote
        )
    }

}