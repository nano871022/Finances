package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditCardMap
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.japl.finances.core.adapters.outbound.interfaces.ICreditCardPort
import co.japl.finances.core.dto.CreditCardDTO
import javax.inject.Inject

class CreditCardImpl @Inject constructor(private val credirCardSvc:ICreditCardSvc): ICreditCardPort {
    override fun getAll(): List<CreditCardDTO> {
        return credirCardSvc.getAll().map(CreditCardMap::mapper)
    }

    override fun get(id: Int): CreditCardDTO? {
        val creditCard = credirCardSvc.get(id)
        if(creditCard.isPresent) {
            return CreditCardMap.mapper(creditCard.get())
        }
        return null

    }
}