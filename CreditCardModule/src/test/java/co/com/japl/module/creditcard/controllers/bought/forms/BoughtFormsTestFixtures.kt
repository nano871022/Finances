package co.com.japl.module.creditcard.controllers.bought.forms

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

internal fun testCreditCard(
    id: Int = 1,
    cutOffDay: Short = 15
): CreditCardDTO = CreditCardDTO(
    id = id,
    name = "Test credit card",
    maxQuotes = 24,
    cutOffDay = cutOffDay,
    warningValue = BigDecimal.ZERO,
    create = LocalDateTime.of(2026, 1, 1, 0, 0),
    status = true,
    interest1Quote = false,
    interest1NotQuote = false
)

internal fun testRate(
    kind: KindInterestRateEnum,
    value: Double = 15.0,
    period: Short = 1
): TaxDTO = TaxDTO(
    id = 1,
    value = value,
    kind = kind,
    period = period,
    kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE
)

internal class FakeTaxPort(private val rate: TaxDTO?) : ITaxPort {

    override fun get(
        codCreditCard: Int,
        month: Int,
        year: Int,
        kind: KindInterestRateEnum
    ): TaxDTO? {
        return rate
    }

    override fun getById(codeCreditRate: Int): TaxDTO? = null

    override fun getByCreditCard(codCreditCard: Int): List<TaxDTO>? = null

    override fun getByCreditCard(codeCreditCard: Int, cutOff: LocalDate): List<TaxDTO> = emptyList()

    override fun delete(code: Int): Boolean = false

    override fun enable(code: Int): Boolean = false

    override fun disable(code: Int): Boolean = false

    override fun create(dto: TaxDTO): Boolean = false

    override fun update(dto: TaxDTO): Boolean = false

    override fun clone(code: Int): Boolean = false
}

internal class FakeCreditCardPort(private val card: CreditCardDTO?) : ICreditCardPort {

    override fun getCreditCard(codeCreditCard: Int): CreditCardDTO? = card

    override fun getAll(): List<CreditCardDTO> = card?.let { listOf(it) } ?: emptyList()

    override fun delete(id: Int): Boolean = false

    override fun create(dto: CreditCardDTO): Int = 0

    override fun update(dto: CreditCardDTO): Boolean = false
}
