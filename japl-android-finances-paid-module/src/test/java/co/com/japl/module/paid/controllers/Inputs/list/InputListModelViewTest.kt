package co.com.japl.module.paid.controllers.Inputs.list

import android.content.Context
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class InputListModelViewTest {

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
    fun main_turnsStateLoaderOnAtStart() {
        val fake = FakeInputPort(mutableListOf(dto(1)))
        val gate = CountDownLatch(1)
        fake.gate = gate
        val viewModel = InputListModelView(mock(Context::class.java), 1, null, fake)
        viewModel.stateLoader.value = false

        viewModel.main()

        assertTrue(viewModel.stateLoader.value)
        gate.countDown()
        waitUntil { viewModel._items.size == 1 }
        assertFalse(viewModel.stateLoader.value)
    }

    private fun waitUntil(timeoutMs: Long = 5_000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (condition()) return
            Thread.sleep(10)
        }
        throw AssertionError("Condition not met within ${timeoutMs}ms")
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
            gate?.await(5, TimeUnit.SECONDS)
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
