package co.com.japl.module.creditcard.controllers.account

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * 26.1 — execute() must turn showProgress on and turn it off even when the
 * service is null, so ON_RESUME reloads always paint the bar.
 */
class CreditCardListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute turns the progress on while the core is consulted`() {
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

        viewModel.main()

        assertTrue("load must reach the core", coreStarted.await(5, TimeUnit.SECONDS))
        assertTrue(
            "showProgress must be on while the core returns data",
            viewModel.showProgress.value
        )
        releaseCore.countDown()
    }
}
