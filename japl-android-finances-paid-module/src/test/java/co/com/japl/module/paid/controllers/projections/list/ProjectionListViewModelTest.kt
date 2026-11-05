package co.com.japl.module.paid.controllers.projections.list

import android.content.Context
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal
import java.time.LocalDate

class ProjectionListViewModelTest {

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
    fun constructor_doesNotLoadFromCore() {
        val fake = FakeProjectionListPort(mutableListOf(dto(1)))
        val viewModel = ProjectionListViewModel(mock(Context::class.java), fake, null)

        Thread.sleep(100)

        assertEquals(0, fake.getProjectionsCalls)
        assertFalse(viewModel.loader.value)
    }

    @Test
    fun reload_loadsFromCore() {
        val fake = FakeProjectionListPort(mutableListOf(dto(1)))
        val viewModel = ProjectionListViewModel(mock(Context::class.java), fake, null)

        viewModel.reload()

        waitUntil { viewModel.list.size == 1 }
        assertEquals(1, fake.getProjectionsCalls)
        assertFalse(viewModel.loader.value)
    }

    private fun waitUntil(timeoutMs: Long = 5_000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (condition()) return
            Thread.sleep(10)
        }
        throw AssertionError("Condition not met within ${timeoutMs}ms")
    }

    private fun dto(id: Int) = ProjectionDTO(
        id = id,
        create = LocalDate.now(),
        end = LocalDate.now().plusMonths(1),
        name = "Projection $id",
        value = BigDecimal.TEN,
        quote = BigDecimal.ONE
    )

    private class FakeProjectionListPort(
        private val store: MutableList<ProjectionDTO>
    ) : IProjectionListPort {

        var getProjectionsCalls = 0
            private set

        override fun getProjections(): List<ProjectionDTO> {
            getProjectionsCalls++
            return store.toList()
        }

        override fun delete(id: Int): Boolean = store.removeIf { it.id == id }
    }
}
