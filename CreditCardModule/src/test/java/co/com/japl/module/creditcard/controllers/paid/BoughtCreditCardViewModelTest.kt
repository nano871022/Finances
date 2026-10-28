package co.com.japl.module.creditcard.controllers.paid

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.Prefs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime

class BoughtCreditCardViewModelTest {

    @Test
    fun testExecuteSuccess_setsLoaderTrue() = runBlocking {
        val fakeService = object : IBoughtListPort {
            override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff: LocalDateTime, cache: Boolean): CreditCardBoughtListDTO {
                throw NotImplementedError()
            }
            override fun getBoughtPeriodList(idCreditCard: Int, cache: Boolean): List<BoughtCreditCardPeriodDTO> = emptyList()
            override fun delete(codeBought: Int, cache: Boolean): Boolean = false
            override fun restore(codeBought: Int, cache: Boolean): Boolean = false
            override fun restoreEndDate(codeBought: Int, cache: Boolean): Boolean = false
            override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean = false
            override fun endingPayment(codeBought: Int, message: String, cutOff: LocalDateTime): Boolean = false
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime, cache: Boolean): Boolean = false
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean): Boolean = false
            override fun clone(codeBought: Int, cache: Boolean): Boolean = false
            override fun getAllRecurrent(idCreditCard: Int): List<CreditCardBoughtItemDTO> = emptyList()
            override fun reactivateRecurrent(codeBought: Int): Boolean = false
            override fun changeQuotas(codeBought: Int, months: Int, cache: Boolean): Boolean = false
        }

        val mockPrefs = mock(Prefs::class.java)
        `when`(mockPrefs.simulator).thenReturn(false)

        val viewModel = BoughtCreditCardViewModel(
            service = fakeService,
            idCreditCard = 1,
            navController = null,
            prefs = mockPrefs
        )

        viewModel.execute()

        assertTrue(viewModel.loader.value)
        assertEquals(1.0f, viewModel.progress.floatValue, 0.01f)
    }

    @Test
    fun testExecuteException_guaranteesLoaderTrue() = runBlocking {
        val throwingService = object : IBoughtListPort {
            override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff: LocalDateTime, cache: Boolean): CreditCardBoughtListDTO {
                throw NotImplementedError()
            }
            override fun getBoughtPeriodList(idCreditCard: Int, cache: Boolean): List<BoughtCreditCardPeriodDTO> {
                throw RuntimeException("Error loading periods")
            }
            override fun delete(codeBought: Int, cache: Boolean): Boolean = false
            override fun restore(codeBought: Int, cache: Boolean): Boolean = false
            override fun restoreEndDate(codeBought: Int, cache: Boolean): Boolean = false
            override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean = false
            override fun endingPayment(codeBought: Int, message: String, cutOff: LocalDateTime): Boolean = false
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime, cache: Boolean): Boolean = false
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean): Boolean = false
            override fun clone(codeBought: Int, cache: Boolean): Boolean = false
            override fun getAllRecurrent(idCreditCard: Int): List<CreditCardBoughtItemDTO> = emptyList()
            override fun reactivateRecurrent(codeBought: Int): Boolean = false
            override fun changeQuotas(codeBought: Int, months: Int, cache: Boolean): Boolean = false
        }

        val mockPrefs = mock(Prefs::class.java)
        `when`(mockPrefs.simulator).thenReturn(false)

        val viewModel = BoughtCreditCardViewModel(
            service = throwingService,
            idCreditCard = 1,
            navController = null,
            prefs = mockPrefs
        )

        try {
            viewModel.execute()
        } catch (e: Exception) {
            // expected
        }

        assertTrue(viewModel.loader.value)
        assertEquals(1.0f, viewModel.progress.floatValue, 0.01f)
    }
}
