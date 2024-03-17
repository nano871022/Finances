package co.japl.finances.core.usercases.interfaces.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO

interface IBoughtSms {
    fun createBySms(dto: CreditCardBoughtDTO): Boolean
}