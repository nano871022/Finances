package co.japl.finances.core.adapters.inbound.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.time.LocalDateTime

class BoughtImplTest {

    @Mock
    private lateinit var service: IBought

    @Mock
    private lateinit var smsImpl: IBoughtSms

    private lateinit var boughtImpl: BoughtImpl

    private fun createCreditCardDTO(id: Int) = CreditCardDTO(
        id = id,
        name = "Test Card",
        maxQuotes = 12,
        cutOffDay = 15,
        warningValue = BigDecimal.TEN,
        create = LocalDateTime.now(),
        status = true,
        interest1Quote = false,
        interest1NotQuote = false
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        boughtImpl = BoughtImpl(service, smsImpl)
    }

    @Test
    fun getBoughtCurrentPeriodList_whenCutOffIsInPast_returnsList() {
        val creditCard = createCreditCardDTO(1)
        val cutOff = LocalDateTime.now().minusDays(1)
        val expectedList = listOf("Item 1" to 100.0)

        `when`(service.getBoughtCurrentPeriodList(creditCard, cutOff, cache = true)).thenReturn(expectedList)

        val result = boughtImpl.getBoughtCurrentPeriodList(creditCard, cutOff, cache = true)

        assertEquals(expectedList, result)
    }

    @Test
    fun getBoughtCurrentPeriodList_whenCutOffIsNow_returnsList() {
        val creditCard = createCreditCardDTO(1)
        val cutOff = LocalDateTime.now()
        val expectedList = listOf("Item 1" to 100.0)

        `when`(service.getBoughtCurrentPeriodList(creditCard, cutOff, cache = true)).thenReturn(expectedList)

        val result = boughtImpl.getBoughtCurrentPeriodList(creditCard, cutOff, cache = true)

        assertEquals(expectedList, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getBoughtCurrentPeriodList_whenCutOffIsInFuture_throwsException() {
        val creditCard = createCreditCardDTO(1)
        val cutOff = LocalDateTime.now().plusDays(1)

        boughtImpl.getBoughtCurrentPeriodList(creditCard, cutOff, cache = true)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getBoughtCurrentPeriodList_whenCreditCardIdIsZero_throwsException() {
        val creditCard = createCreditCardDTO(0)
        val cutOff = LocalDateTime.now().minusDays(1)

        boughtImpl.getBoughtCurrentPeriodList(creditCard, cutOff, cache = true)
    }
}
