package co.com.japl.module.paid.controllers.Inputs.list

import android.app.Application
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.ui.testutil.MainDispatcherRule
import co.com.japl.ui.testutil.waitUntil
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.CountDownLatch

class InputListModelViewTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun reload_whenCalled_thenStateLoaderIsOnAtStart() {
        val fake = FakeInputPort(mutableListOf(dto(1)))
        val gate = CountDownLatch(1)
        fake.gate = gate
        val viewModel = InputListModelView(Application(), 1, null, fake)
        viewModel.stateLoader.value = false

        viewModel.reload()

        assertTrue(viewModel.stateLoader.value)
        gate.countDown()
        waitUntil { viewModel._items.size == 1 }
        assertFalse(viewModel.stateLoader.value)
    }

    private fun dto(id: Int) = InputDTO(
        id = id,
        date = LocalDate.now(),
        accountCode = 1,
        kindOf = "FIXED",
        name = "Input $id",
        value = BigDecimal.TEN,
        dateStart = LocalDate.now(),
        dateEnd = LocalDate.now()
    )

    private class FakeInputPort(
        private val store: MutableList<InputDTO>
    ) : IInputPort {

        var gate: CountDownLatch? = null

        override fun getInputs(accountCode: Int): List<InputDTO> {
            gate?.await(5, java.util.concurrent.TimeUnit.SECONDS)
            return store.toList()
        }

        override fun deleteRecord(id: Int): Boolean = store.removeIf { it.id == id }

        override fun updateValue(id: Int, value: Double): Boolean = true

        override fun getById(id: Int): InputDTO? = store.firstOrNull { it.id == id }

        override fun create(input: InputDTO): Boolean = true

        override fun update(input: InputDTO): Boolean = true

        override fun getTotalInputs(codeAccount: Int, period: YearMonth): Double = 0.0
    }
}

