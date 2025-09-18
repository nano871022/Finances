package co.com.japl.module.paid.views.projections.list.fakes

import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import java.math.BigDecimal

class FakeProjectionsPort : IProjectionsPort {
    override fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>> {
        return Triple(
            2,
            BigDecimal(200000),
            listOf(
                ProjectionRecap(
                    name = "Test Projection 1",
                    value = BigDecimal(1000000),
                    quote = BigDecimal(100000),
                    saved = BigDecimal(200000)
                ),
                ProjectionRecap(
                    name = "Test Projection 2",
                    value = BigDecimal(500000),
                    quote = BigDecimal(100000),
                    saved = BigDecimal(100000)
                )
            )
        )
    }
}
