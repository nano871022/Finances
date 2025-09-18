package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import java.math.BigDecimal

class ProjectionsPortFake : IProjectionsPort {
    override fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>> {
        return Triple(0, BigDecimal.ZERO, emptyList())
    }
}
