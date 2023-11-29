package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditCardMap
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardDTO
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