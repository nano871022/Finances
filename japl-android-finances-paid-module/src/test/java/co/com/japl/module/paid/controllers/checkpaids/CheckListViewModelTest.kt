package co.com.japl.module.paid.controllers.checkpaids

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.com.japl.ui.testutil.MainDispatcherRule
import co.com.japl.ui.testutil.waitUntil
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.YearMonth
import java.util.concurrent.CountDownLatch

class CheckListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun reload_whenCoreIsBlocked_thenCallerReturnsAndLoaderIsOn() {
        val fake = FakeCheckPaymentPort(mutableListOf(dto()))
        val gate = CountDownLatch(1)
        fake.gate = gate
        val viewModel = CheckListViewModel(YearMonth.now(), fake)
        viewModel.loaderStatus.value = false

        val start = System.currentTimeMillis()
        viewModel.reload()
        val elapsed = System.currentTimeMillis() - start

        assertTrue("reload() blocked the caller for ${elapsed}ms", elapsed < 1_000)
        assertTrue(viewModel.loaderStatus.value)
        gate.countDown()
        waitUntil { !viewModel.loaderStatus.value }
        assertFalse(viewModel.loaderStatus.value)
        assertTrue(viewModel.listState.isNotEmpty())
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
            gate?.await(5, java.util.concurrent.TimeUnit.SECONDS)
            return store.toList()
        }

        override fun update(check: CheckPaymentDTO): Boolean = true

        override fun save(check: CheckPaymentDTO): CheckPaymentDTO = check

        override fun delete(check: CheckPaymentDTO): Boolean = store.removeIf { it.id == check.id }
    }
}


