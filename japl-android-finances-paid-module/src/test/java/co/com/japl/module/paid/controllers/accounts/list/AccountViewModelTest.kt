package co.com.japl.module.paid.controllers.accounts.list

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class AccountViewModelTest {

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
    fun delete_reloadsListFromCore() {
        val fake = FakeAccountPort(mutableListOf(dto(1), dto(2)))
        val viewModel = AccountViewModel(fake, null)
        viewModel.main()
        waitUntil { viewModel.list.size == 2 }
        assertEquals(1, fake.getAllCalls)

        viewModel.delete(1)

        waitUntil { fake.getAllCalls == 2 && viewModel.list.size == 1 }
        assertEquals(2, viewModel.list.first().id)
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun delete_doesNotReloadWhenCoreFails() {
        val fake = FakeAccountPort(mutableListOf(dto(1)))
        fake.deleteResult = false
        val viewModel = AccountViewModel(fake, null)
        viewModel.main()
        waitUntil { viewModel.list.size == 1 }
        assertEquals(1, fake.getAllCalls)

        viewModel.delete(1)

        Thread.sleep(100)
        assertEquals(1, fake.getAllCalls)
        assertEquals(1, viewModel.list.size)
    }

    @Test
    fun main_turnsLoadingOnAtStart() {
        val fake = FakeAccountPort(mutableListOf(dto(1)))
        val gate = CountDownLatch(1)
        fake.gate = gate
        val viewModel = AccountViewModel(fake, null)
        viewModel.loading.value = false

        viewModel.main()

        assertTrue(viewModel.loading.value)
        gate.countDown()
        waitUntil { viewModel.list.size == 1 }
        assertFalse(viewModel.loading.value)
    }

    private fun waitUntil(timeoutMs: Long = 5_000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (condition()) return
            Thread.sleep(10)
        }
        throw AssertionError("Condition not met within ${timeoutMs}ms")
    }

    private fun dto(id: Int) = AccountDTO(
        id = id,
        create = LocalDate.now(),
        name = "Account $id",
        active = true
    )

    private class FakeAccountPort(
        private val store: MutableList<AccountDTO>
    ) : IAccountPort {

        var getAllCalls = 0
            private set
        var gate: CountDownLatch? = null
        var deleteResult = true

        override fun getById(codeAccount: Int): AccountDTO? = store.firstOrNull { it.id == codeAccount }

        override fun getAll(): List<AccountDTO> {
            getAllCalls++
            gate?.await(5, TimeUnit.SECONDS)
            return store.toList()
        }

        override fun getAllActive(): List<AccountDTO> = store.filter { it.active }

        override fun delete(codeAccount: Int): Boolean {
            if (!deleteResult) return false
            return store.removeIf { it.id == codeAccount }
        }

        override fun create(dto: AccountDTO): Int = 0

        override fun update(dto: AccountDTO): Boolean = true
    }
}
