package co.japl.finances.core.usercases.interfaces.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.japl.finances.core.enums.AutoLoadKind

interface IBoughtSms {
    fun createByAutoLoad(dto: CreditCardBoughtDTO, kind: AutoLoadKind= AutoLoadKind.SMS): Boolean
}