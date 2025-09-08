package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import java.math.BigDecimal

class ProjectionsFake : IProjectionsPort{
    override fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>> {
        TODO("Not yet implemented")
    }
}