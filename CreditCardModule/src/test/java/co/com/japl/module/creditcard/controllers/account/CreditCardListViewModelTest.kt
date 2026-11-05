package co.com.japl.module.creditcard.controllers.account

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.ui.testutil.MainDispatcherRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CreditCardListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun reload_whenCoreIsConsulted_thenProgressIsOn() {
        val coreStarted = CountDownLatch(1)
        val releaseCore = CountDownLatch(1)

        val service = object : ICreditCardPort {
            override fun getCreditCard(codeCreditCard: Int): CreditCardDTO? = null
            override fun getAll(): List<CreditCardDTO> {
                coreStarted.countDown()
                releaseCore.await(5, TimeUnit.SECONDS)
                return emptyList()
            }

            override fun delete(id: Int): Boolean = false
            override fun create(dto: CreditCardDTO): Int = 0
            override fun update(dto: CreditCardDTO): Boolean = false
        }

        val viewModel = CreditCardListViewModel(service)
        viewModel.showProgress.value = false

        viewModel.reload()

        assertTrue("load must reach the core", coreStarted.await(5, TimeUnit.SECONDS))
        assertTrue(
            "showProgress must be on while the core returns data",
            viewModel.showProgress.value
        )
        releaseCore.countDown()
    }
}

