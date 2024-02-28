package co.com.japl.finances.iports.inbounds.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import java.time.LocalDateTime

interface IBoughtPort {
    fun getRecap(creditCard: CreditCardDTO, cutOffDate: LocalDateTime): RecapMonthly?
}