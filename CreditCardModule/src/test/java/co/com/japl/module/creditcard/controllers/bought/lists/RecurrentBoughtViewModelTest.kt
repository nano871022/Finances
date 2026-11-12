package co.com.japl.module.creditcard.controllers.bought.lists

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.Prefs
import co.com.japl.ui.testutil.MainDispatcherRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import android.app.Application
import android.content.SharedPreferences
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RecurrentBoughtViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun load_whenCoreIsConsulted_thenLoaderIsOn() {
        val coreStarted = CountDownLatch(1)
        val releaseCore = CountDownLatch(1)

        val port = object : IBoughtListPort {
            override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff: LocalDateTime, cache: Boolean): CreditCardBoughtListDTO = TODO()
            override fun getBoughtPeriodList(idCreditCard: Int, cache: Boolean): List<BoughtCreditCardPeriodDTO> = emptyList()
            override fun delete(codeBought: Int, cache: Boolean): Boolean = true
            override fun restore(codeBought: Int, cache: Boolean): Boolean = true
            override fun restoreEndDate(codeBought: Int, cache: Boolean): Boolean = true
            override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean = true
            override fun endingPayment(codeBought: Int, message: String, cutOff: LocalDateTime): Boolean = true
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime, cache: Boolean): Boolean = true
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean): Boolean = true
            override fun clone(codeBought: Int, cache: Boolean): Boolean = true
            override fun getAllRecurrent(idCreditCard: Int): List<CreditCardBoughtItemDTO> {
                coreStarted.countDown()
                releaseCore.await(5, TimeUnit.SECONDS)
                return emptyList()
            }
            override fun reactivateRecurrent(codeBought: Int): Boolean = true
            override fun changeQuotas(codeBought: Int, months: Int, cache: Boolean): Boolean = true
        }

        val sharedPrefs = mock(SharedPreferences::class.java)
        org.mockito.Mockito.doReturn("false").`when`(sharedPrefs).getString(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())
        val app = object : Application() {
            override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences = sharedPrefs
        }
        val viewModel = RecurrentBoughtViewModel(port, co.com.japl.ui.Prefs(app))
        viewModel.loader.value = false

        viewModel.load(1)

        assertTrue("load must reach the core", coreStarted.await(5, TimeUnit.SECONDS))
        assertTrue(
            "loader must be on while the core returns data",
            viewModel.loader.value
        )
        releaseCore.countDown()
    }
}
