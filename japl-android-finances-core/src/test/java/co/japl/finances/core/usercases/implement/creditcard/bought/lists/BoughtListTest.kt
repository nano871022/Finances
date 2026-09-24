package co.japl.finances.core.usercases.implement.creditcard.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.RecapCalculation
import co.japl.finances.core.usercases.implement.common.ListBoughts
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.time.LocalDateTime

class BoughtListTest {

    @Mock
    private lateinit var quoteCCSvc: IQuoteCreditCardPort

    @Mock
    private lateinit var recapCalculation: RecapCalculation

    @Mock
    private lateinit var differQuotesSvc: IDifferInstallmentRecapPort

    @Mock
    private lateinit var tagsSvc: ITagQuoteCreditCardPort

    @Mock
    private lateinit var creditCardSvc: ICreditCardPort

    @Mock
    private lateinit var interestCalculation: InterestCalculations

    @Mock
    private lateinit var listBoughts: ListBoughts

    private lateinit var boughtList: BoughtList

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        boughtList = BoughtList(
            quoteCCSvc,
            recapCalculation,
            differQuotesSvc,
            tagsSvc,
            creditCardSvc,
            interestCalculation,
            listBoughts
        )
    }

    private fun createBoughtDTO(id: Int, creditCardId: Int, month: Int) = CreditCardBoughtDTO(
        id = id,
        codeCreditCard = creditCardId,
        nameCreditCard = "Test CC",
        nameItem = "Item 1",
        valueItem = BigDecimal("100.0"),
        interest = 1.5,
        month = month,
        boughtDate = LocalDateTime.of(2025, 1, 1, 10, 0),
        cutOutDate = LocalDateTime.of(2025, 1, 15, 0, 0),
        createDate = LocalDateTime.of(2025, 1, 1, 10, 0),
        endDate = LocalDateTime.of(2026, 1, 15, 0, 0),
        recurrent = 0,
        kind = KindInterestRateEnum.CREDIT_CARD,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE
    )

    private fun createCreditCardDTO(id: Int, maxQuotes: Short = 36) = CreditCardDTO(
        id = id,
        name = "Test Card",
        maxQuotes = maxQuotes,
        cutOffDay = 15,
        warningValue = BigDecimal.TEN,
        create = LocalDateTime.now(),
        status = true,
        interest1Quote = false,
        interest1NotQuote = false
    )

    private fun <T> anyObject(): T {
        any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T

    @Test
    fun changeQuotas_successful() {
        val boughtDTO = createBoughtDTO(id = 10, creditCardId = 1, month = 12)
        val creditCardDTO = createCreditCardDTO(id = 1, maxQuotes = 36)

        `when`(quoteCCSvc.get(10, false)).thenReturn(boughtDTO)
        `when`(creditCardSvc.get(1)).thenReturn(creditCardDTO)
        `when`(quoteCCSvc.update(anyObject(), eq(false))).thenReturn(true)

        val result = boughtList.changeQuotas(codeBought = 10, months = 24, cache = false)

        assertTrue(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeQuotas_invalidCodeBought_throwsException() {
        boughtList.changeQuotas(codeBought = 0, months = 12, cache = false)
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeQuotas_zeroMonths_throwsException() {
        boughtList.changeQuotas(codeBought = 10, months = 0, cache = false)
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeQuotas_exceedsMaxQuotes_throwsException() {
        val boughtDTO = createBoughtDTO(id = 10, creditCardId = 1, month = 12)
        val creditCardDTO = createCreditCardDTO(id = 1, maxQuotes = 24)

        `when`(quoteCCSvc.get(10, false)).thenReturn(boughtDTO)
        `when`(creditCardSvc.get(1)).thenReturn(creditCardDTO)

        boughtList.changeQuotas(codeBought = 10, months = 36, cache = false)
    }

    @Test
    fun changeQuotas_boughtNotFound_returnsFalse() {
        `when`(quoteCCSvc.get(10, false)).thenReturn(null)

        val result = boughtList.changeQuotas(codeBought = 10, months = 12, cache = false)

        assertFalse(result)
    }
}
