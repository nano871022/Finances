package co.com.japl.module.paid.views.projections.list.fakes

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import java.math.BigDecimal
import java.time.LocalDate

class FakeProjectionListPort : IProjectionListPort {
    override fun getProjections(): List<ProjectionDTO> {
        return listOf(
            ProjectionDTO(
                id = 1,
                create = LocalDate.now(),
                end = LocalDate.now().plusMonths(12),
                name = "Test Projection 1",
                value = BigDecimal(1000000),
                quote = BigDecimal(100000),
                monthsLeft = 10,
                amountSaved = BigDecimal(200000),
                type = "MONTHLY"
            ),
            ProjectionDTO(
                id = 2,
                create = LocalDate.now(),
                end = LocalDate.now().plusMonths(6),
                name = "Test Projection 2",
                value = BigDecimal(500000),
                quote = BigDecimal(100000),
                monthsLeft = 5,
                amountSaved = BigDecimal(100000),
                type = "BI-WEEKLY"
            )
        )
    }

    override fun delete(id: Int): Boolean {
        return true
    }
}
