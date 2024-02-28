package co.japl.finances.core.adapters.inbound.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import java.time.LocalDateTime
import javax.inject.Inject

class BoughtImpl @Inject constructor(private val service:IBought) : IBoughtPort{
    override fun getRecap(creditCard: CreditCardDTO, cutOffDate: LocalDateTime): RecapMonthly? {
        return service.getRecap(creditCard,cutOffDate)
    }

}