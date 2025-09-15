package co.com.japl.module.creditcard.views.fakesSvc

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import java.time.LocalDateTime

class BoughtPortFake : IBoughtPort {
    override fun getRecap(
        creditCard: CreditCardDTO,
        cutOff: LocalDateTime,
        cache: Boolean
    ): CreditCardBoughtListDTO {
        return CreditCardBoughtListDTO()
    }

    override fun getBoughtCurrentPeriodList(
        creditCard: CreditCardDTO,
        cutOff: LocalDateTime,
        cache: Boolean
    ): List<Pair<String, Double>> {
        return emptyList()
    }

    override fun get(codeBought: Int, cache: Boolean): CreditCardBoughtDTO? {
        return null
    }

    override fun get(idCreditCard: Int): List<CreditCardBoughtDTO> {
        return emptyList()
    }
}
