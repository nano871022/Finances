package co.com.japl.module.creditcard.views.fakeSvc

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import java.time.LocalDateTime

class FakeBoughtPort : IBoughtPort {
    override fun getRecap(
        creditCard: CreditCardDTO,
        cutOffDate: LocalDateTime,
        cache: Boolean
    ): RecapMonthly? {
        TODO("Not yet implemented")
    }

    override fun getBoughtCurrentPeriodList(
        creditCardDTO: CreditCardDTO,
        cutOff: LocalDateTime,
        cache: Boolean
    ): List<Pair<String, Double>>? {
        TODO("Not yet implemented")
    }

    override fun create(
        creditCardBoughtDTO: CreditCardBoughtDTO,
        cache: Boolean
    ): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        creditCardBoughtDTO: CreditCardBoughtDTO,
        cache: Boolean
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun quoteValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double {
        TODO("Not yet implemented")
    }

    override fun capitalValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double {
        TODO("Not yet implemented")
    }

    override fun interestValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double {
        TODO("Not yet implemented")
    }

    override fun getById(
        codeBought: Int,
        cache: Boolean
    ): CreditCardBoughtDTO? {
        TODO("Not yet implemented")
    }

    override fun fixDataProcess() {
        TODO("Not yet implemented")
    }

}
