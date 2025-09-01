package co.japl.finances.core.adapters.inbound.implement.creditcard.bought.lists

import android.util.Log
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.interfaces.creditcard.paid.lists.IPaidList
import co.japl.finances.core.usercases.mapper.CreditCardMap
import java.time.LocalDateTime
import javax.inject.Inject

class ListImpl @Inject constructor(private val service: IBoughtList,private val paidList:IPaidList): IBoughtListPort {
    override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff:LocalDateTime, cache:Boolean): CreditCardBoughtListDTO {
        return service.getBoughtList(CreditCardMap.mapper(creditCardDTO),cutOff,cache).also {
            Log.d(javaClass.name,"<<<=== GetBoughtList CreditCard Id: ${creditCardDTO.id} Cutoff: ${cutOff} Size: ${it.list?.size}")
        }
    }



    override fun getBoughtPeriodList(idCreditCard: Int,cache: Boolean): List<BoughtCreditCardPeriodDTO> {
        return paidList?.getBoughtPeriodList(idCreditCard,cache)?.sortedByDescending { it.periodStart }?: arrayListOf()
    }

    override fun delete(codeBought: Int, cache: Boolean): Boolean {
        return service.delete(codeBought,cache)
    }

    override fun restore(codeBought: Int, cache: Boolean): Boolean {
        return service.restore(codeBought,cache)
    }

    override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean {
        return service.endingRecurrentPayment(codeBought,cutOff)
    }

    override fun endingPayment(codeBought: Int, message:String, cutOff: LocalDateTime): Boolean {
        if(message.isBlank()) return false
        return service.endingPayment(codeBought,message,cutOff)
    }

    override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff:LocalDateTime, cache: Boolean): Boolean {
        return service.updateRecurrentValue(codeBought,value,cutOff,cache)
    }

    override fun differntInstallment(codeBought: Int, value: Long, cutOff:LocalDateTime, cache: Boolean): Boolean {
        return service.differntInstallment(codeBought,value,cutOff,cache)
    }

    override fun clone(codeBought: Int, cache: Boolean): Boolean {
        return service.clone(codeBought,cache)
    }
}