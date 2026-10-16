package co.com.japl.module.paid.controllers.paid.form

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.LocalDate

class PaidViewModelTest {

    @Test
    fun testExecutionSuccess_setsLoadingFalseAndUpdatesProgress() = runBlocking {
        val fakeAccountPort = object : IAccountPort {
            override fun getById(codeAccount: Int): AccountDTO? = null
            override fun getAll(): List<AccountDTO> = emptyList()
            override fun getAllActive(): List<AccountDTO> = listOf(
                AccountDTO(1, LocalDate.now(), "Main Account", true)
            )
            override fun delete(codeAccount: Int): Boolean = false
            override fun create(dto: AccountDTO): Int = 0
            override fun update(dto: AccountDTO): Boolean = false
        }

        val viewModel = PaidViewModel(
            codeAccount = 1,
            codePaid = null,
            accountSvc = fakeAccountPort,
            paidSvc = null,
            navController = null
        )

        viewModel.execution()

        assertFalse(viewModel.loading.value)
        assertEquals(0.8f, viewModel.progressStatus.value, 0.01f)
    }

    @Test
    fun testExecutionException_guaranteesLoadingFalse() = runBlocking {
        val throwingAccountPort = object : IAccountPort {
            override fun getById(codeAccount: Int): AccountDTO? = null
            override fun getAll(): List<AccountDTO> = emptyList()
            override fun getAllActive(): List<AccountDTO> {
                throw RuntimeException("Database error")
            }
            override fun delete(codeAccount: Int): Boolean = false
            override fun create(dto: AccountDTO): Int = 0
            override fun update(dto: AccountDTO): Boolean = false
        }

        val viewModel = PaidViewModel(
            codeAccount = 1,
            codePaid = null,
            accountSvc = throwingAccountPort,
            paidSvc = null,
            navController = null
        )

        try {
            viewModel.execution()
        } catch (e: Exception) {
            // expected
        }

        assertFalse(viewModel.loading.value)
    }
}
