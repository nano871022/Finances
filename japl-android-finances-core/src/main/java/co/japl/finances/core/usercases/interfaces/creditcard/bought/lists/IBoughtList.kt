package co.japl.finances.core.usercases.interfaces.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.japl.finances.core.model.CreditCard
import java.time.LocalDateTime
import java.time.YearMonth

interface IBoughtList {

    fun getBoughtList(creditCard: CreditCard,cutoff:LocalDateTime):CreditCardBoughtListDTO

    fun delete(codeBought:Int):Boolean

    fun endingRecurrentPayment(codeBought:Int,cutoff:LocalDateTime):Boolean

    fun updateRecurrentValue(codeBought:Int,value:Double, cutoff: LocalDateTime):Boolean

    fun differntInstallment(codeBought:Int,value:Long,cutOff:LocalDateTime):Boolean

}