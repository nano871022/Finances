package co.japl.finances.core.usercases.mapper

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO

object CreditCardBoughtItemMapper {

    fun mapper(creditCardBought:CreditCardBoughtDTO):CreditCardBoughtItemDTO {
        return CreditCardBoughtItemDTO(
            id = creditCardBought.id,
            nameCreditCard =  creditCardBought.nameCreditCard,
            nameItem = creditCardBought.nameItem,
            valueItem = creditCardBought.valueItem.toDouble(),
            interest = creditCardBought.interest,
            month = creditCardBought.month,
            boughtDate = creditCardBought.boughtDate,
            cutOutDate = creditCardBought.cutOutDate,
            createDate = creditCardBought.createDate,
            endDate = creditCardBought.endDate,
            recurrent = creditCardBought.recurrent == 1.toShort(),
            kind = creditCardBought.kind,
            kindOfTax = creditCardBought.kindOfTax,
            codeCreditCard = creditCardBought.codeCreditCard,
            capitalValue = 0.0,
            interestValue = 0.0,
            pendingToPay = 0.0,
            quoteValue = 0.0,
            settings = 0.0,
            monthPaid = 0,
            tagName = "",
            settingCode = 0
        )
    }

}