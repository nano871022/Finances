package co.japl.finances.core.usercases.interfaces.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import java.time.LocalDateTime

interface IBought {
    fun getRecap(creditCard: CreditCardDTO, cutOffDate: LocalDateTime): RecapMonthly?
}