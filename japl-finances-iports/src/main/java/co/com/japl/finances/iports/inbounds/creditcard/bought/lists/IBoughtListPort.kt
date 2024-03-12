package co.com.japl.finances.iports.inbounds.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import java.time.LocalDateTime

interface IBoughtListPort {

    fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff:LocalDateTime,cache:Boolean):CreditCardBoughtListDTO

    fun getBoughtPeriodList(idCreditCard:Int,cache:Boolean):List<BoughtCreditCardPeriodDTO>

    fun delete(codeBought:Int,cache:Boolean):Boolean

    fun endingRecurrentPayment(codeBought: Int, cutOff:LocalDateTime):Boolean

    fun updateRecurrentValue(codeBought:Int, value:Double, cutOff:LocalDateTime, cache:Boolean):Boolean

    fun differntInstallment(codeBought:Int, value:Long, cutOff:LocalDateTime, cache:Boolean):Boolean

    fun clone(codeBought:Int,cache: Boolean):Boolean

}