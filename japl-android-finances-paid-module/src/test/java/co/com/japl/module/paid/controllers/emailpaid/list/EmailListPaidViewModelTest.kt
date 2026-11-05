package co.com.japl.module.paid.controllers.emailpaid.list

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.ui.testutil.MainDispatcherRule
import co.com.japl.ui.testutil.waitUntil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

class EmailListPaidViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun delete_whenCoreSucceeds_thenReloadsFromCore() {
        val fake = FakeEmailPaidPort(mutableListOf(dto(1), dto(2)))
        val viewModel = EmailListPaidViewModel(fake, null)
        viewModel.reload()
        waitUntil { viewModel.list.size == 2 }
        assertEquals(1, fake.getAllCalls)

        viewModel.delete(1)

        waitUntil { fake.getAllCalls == 2 && viewModel.list.size == 1 }
        assertEquals(2, viewModel.list.single().id)
        assertFalse(viewModel.load.value)
    }

    @Test
    fun activate_whenCoreSucceeds_thenReloadsFromCore() {
        val fake = FakeEmailPaidPort(mutableListOf(dto(1, active = false)))
        val viewModel = EmailListPaidViewModel(fake, null)
        viewModel.reload()
        waitUntil { viewModel.list.size == 1 }
        assertEquals(1, fake.getAllCalls)

        viewModel.activate(1, true)

        waitUntil { fake.getAllCalls == 2 && viewModel.list.size == 1 && viewModel.list.first().active }
        assertTrue(viewModel.list.single().active)
        assertFalse(viewModel.load.value)
    }

    @Test
    fun reload_whenServiceIsNull_thenLoadIsTurnedOff() {
        val viewModel = EmailListPaidViewModel(null, null)
        assertTrue(viewModel.load.value)

        viewModel.reload()

        waitUntil { !viewModel.load.value }
        assertFalse(viewModel.load.value)
    }

    @Test
    fun reload_whenCalled_thenLoadIsOnAtStart() {
        val fake = FakeEmailPaidPort(mutableListOf(dto(1)))
        val gate = CountDownLatch(1)
        fake.gate = gate
        val viewModel = EmailListPaidViewModel(fake, null)
        viewModel.load.value = false

        viewModel.reload()

        assertTrue(viewModel.load.value)
        gate.countDown()
        waitUntil { viewModel.list.size == 1 }
        assertFalse(viewModel.load.value)
    }

    private fun dto(id: Int, active: Boolean = true) = EmailPaidDTO(
        id = id,
        sender = "sender@test.com",
        subjectPattern = "subject",
        bodyPattern = "body",
        codeAccount = 1,
        nameAccount = "Account",
        active = active
    )

    private class FakeEmailPaidPort(
        private val store: MutableList<EmailPaidDTO>
    ) : IEmailPaidPort {

        var getAllCalls = 0
            private set
        var gate: CountDownLatch? = null

        override fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO> =
            emptyList()

        override fun create(dto: EmailPaidDTO): Int = 0

        override fun update(dto: EmailPaidDTO): Boolean = true

        override fun getById(id: Int): EmailPaidDTO? = store.firstOrNull { it.id == id }

        override fun getAll(): List<EmailPaidDTO> {
            getAllCalls++
            gate?.await(5, java.util.concurrent.TimeUnit.SECONDS)
            return store.toList()
        }

        override fun activate(id: Int, active: Boolean): Boolean {
            val index = store.indexOfFirst { it.id == id }
            if (index == -1) return false
            store[index] = store[index].copy(active = active)
            return true
        }

        override fun delete(id: Int): Boolean = store.removeIf { it.id == id }

        override fun clone(id: Int): Boolean = false

        override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String> = emptyList()

        override fun read(numDaysRead: Int) = Unit

        override fun isEmailAccessGranted(): Boolean = true

        override fun hasActiveEmail(): Boolean = true
    }
}

