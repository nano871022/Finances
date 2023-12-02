package co.japl.finances.core.adapters.inbound.implement.creditcard.bought.lists

import android.util.Log
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.mapper.CreditCardMap
import java.time.LocalDateTime
import javax.inject.Inject

class ListImpl @Inject constructor(private val service: IBoughtList): IBoughtListPort {
    override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff:LocalDateTime): CreditCardBoughtListDTO {
        return service.getBoughtList(CreditCardMap.mapper(creditCardDTO),cutOff).also {
            Log.d(javaClass.name,"<<<=== GetBoughtList CreditCard Id: ${creditCardDTO.id} Cutoff: ${cutOff} Size: ${it.list?.size}")
        }
    }

    override fun delete(codeBought: Int): Boolean {
        return service.delete(codeBought)
    }

    override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean {
        return service.endingRecurrentPayment(codeBought,cutOff)
    }

    override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff:LocalDateTime): Boolean {
        return service.updateRecurrentValue(codeBought,value,cutOff)
    }

    override fun differntInstallment(codeBought: Int, value: Long, cutOff:LocalDateTime): Boolean {
        return service.differntInstallment(codeBought,value,cutOff)
    }
}