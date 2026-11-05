package co.com.japl.module.paid.controllers.checkpaids

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.YearMonth
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CheckListViewModelTest {

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
    fun main_doesNotBlockCallerAndTogglesLoader() {
        val fake = FakeCheckPaymentPort(mutableListOf(dto()))
        val gate = CountDownLatch(1)
        fake.gate = gate
        val viewModel = CheckListViewModel(YearMonth.now(), fake)
        viewModel.loaderStatus.value = false

        val start = System.currentTimeMillis()
        viewModel.main()
        val elapsed = System.currentTimeMillis() - start

        assertTrue("main() blocked the caller for ${elapsed}ms", elapsed < 1_000)
        assertTrue(viewModel.loaderStatus.value)
        gate.countDown()
        waitUntil { !viewModel.loaderStatus.value }
        assertFalse(viewModel.loaderStatus.value)
        assertTrue(viewModel.listState.isNotEmpty())
    }

    private fun waitUntil(timeoutMs: Long = 5_000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (condition()) return
            Thread.sleep(10)
        }
        throw AssertionError("Condition not met within ${timeoutMs}ms")
    }

    private fun dto() = CheckPaymentDTO(
        id = 1,
        name = "Check",
        period = YearMonth.now(),
        codPaid = 1L,
        amount = 100.0,
        date = null,
        check = false,
        update = false,
        type = CheckPaymentsEnum.PAYMENTS
    )

    private class FakeCheckPaymentPort(
        private val store: MutableList<CheckPaymentDTO>
    ) : ICheckPaymentPort {

        var gate: CountDownLatch? = null

        override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> = emptyList()

        override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
            gate?.await(5, TimeUnit.SECONDS)
            return store.toList()
        }

        override fun update(check: CheckPaymentDTO): Boolean = true

        override fun save(check: CheckPaymentDTO): CheckPaymentDTO = check

        override fun delete(check: CheckPaymentDTO): Boolean = store.removeIf { it.id == check.id }
    }
}
