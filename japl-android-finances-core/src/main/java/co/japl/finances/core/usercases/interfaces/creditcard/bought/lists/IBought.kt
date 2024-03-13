package co.japl.finances.core.usercases.interfaces.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import java.time.LocalDateTime

interface IBought {
    fun getRecap(creditCard: CreditCardDTO, cutOffDate: LocalDateTime, cache: Boolean): RecapMonthly?

    fun getBoughtCurrentPeriodList(creditCardDTO: CreditCardDTO, cutOff:LocalDateTime, cache:Boolean):List<Pair<String,Double>>?

    fun create(creditCardBoughtDTO: CreditCardBoughtDTO,cache: Boolean): Int

    fun update(creditCardBoughtDTO: CreditCardBoughtDTO,cache: Boolean): Boolean

    fun quoteValue(codeCreditRate:Int,months:Short,value:Double,kindOfTax:KindOfTaxEnum,kindOfInterest:KindInterestRateEnum):Double

    fun capitalValue(codeCreditRate:Int,months:Short,value:Double,kindOfTax:KindOfTaxEnum,kindOfInterest:KindInterestRateEnum):Double

    fun interestValue(codeCreditRate:Int,months:Short,value:Double,kindOfTax:KindOfTaxEnum,kindOfInterest:KindInterestRateEnum):Double

    fun getById(codeBought:Int,cache:Boolean):CreditCardBoughtDTO?

}