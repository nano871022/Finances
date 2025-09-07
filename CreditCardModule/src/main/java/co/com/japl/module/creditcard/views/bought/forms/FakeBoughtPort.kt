package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import java.math.BigDecimal
import java.time.LocalDateTime

class FakeBoughtPort : IBoughtPort {
    override fun get(codeCreditCard: Int, period: LocalDateTime): List<CreditCardBoughtDTO> {
        return emptyList()
    }

    override fun getById(code: Int, cache: Boolean): CreditCardBoughtDTO? {
        return null
    }

    override fun get(codeBought: Int): List<CreditCardBoughtDTO> {
        return emptyList()
    }

    override fun getRecurrent(codeCreditCard: Int): List<CreditCardBoughtDTO> {
        return emptyList()
    }

    override fun getPendingToPay(codeCreditCard: Int): List<CreditCardBoughtDTO> {
        return emptyList()
    }

    override fun create(creditCardBought: CreditCardBoughtDTO, cache: Boolean): Int {
        return 1
    }

    override fun update(creditCardBought: CreditCardBoughtDTO, cache: Boolean): Boolean {
        return true
    }

    override fun delete(code: Int, cache: Boolean): Boolean {
        return true
    }

    override fun quoteValue(
        idCreditRate: Int,
        quotes: Short,
        value: Double,
        kind: KindOfTaxEnum,
        interest: KindInterestRateEnum
    ): BigDecimal {
        return BigDecimal.ZERO
    }

    override fun interestValue(
        idCreditRate: Int,
        quotes: Short,
        value: Double,
        kind: KindOfTaxEnum,
        interest: KindInterestRateEnum
    ): BigDecimal {
        return BigDecimal.ZERO
    }

    override fun capitalValue(
        idCreditRate: Int,
        quotes: Short,
        value: Double,
        kind: KindOfTaxEnum,
        interest: KindInterestRateEnum
    ): BigDecimal {
        return BigDecimal.ZERO
    }
}
