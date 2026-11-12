package co.com.japl.module.paid.controllers.projections.list

import android.app.Application
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.ui.testutil.MainDispatcherRule
import co.com.japl.ui.testutil.waitUntil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal
import java.time.LocalDate

class ProjectionListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun constructor_whenCreated_thenDoesNotLoadFromCore() {
        val fake = FakeProjectionListPort(mutableListOf(dto(1)))
        val viewModel = ProjectionListViewModel(Application(), fake, null)

        Thread.sleep(100)

        assertEquals(0, fake.getProjectionsCalls)
        assertFalse(viewModel.loader.value)
    }

    @Test
    fun reload_whenCalled_thenLoadsFromCore() {
        val fake = FakeProjectionListPort(mutableListOf(dto(1)))
        val viewModel = ProjectionListViewModel(Application(), fake, null)

        viewModel.reload()

        waitUntil { viewModel.list.size == 1 }
        assertEquals(1, fake.getProjectionsCalls)
        assertFalse(viewModel.loader.value)
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
