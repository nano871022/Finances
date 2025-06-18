package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import java.math.BigDecimal
import javax.inject.Inject

class ProjectionImpl @Inject constructor(private val projectionSvc: IProjection) : IProjectionsPort , IProjectionListPort{

    override fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>> {
        return projectionSvc.getProjectionRecap()
    }

    override fun getProjections(): List<ProjectionDTO> {
        return projectionSvc.getProjections()
    }

    override fun delete(id: Int): Boolean {
        require(id>=0){"El id no puede ser menor a cero"}
        return projectionSvc.delete(id)
    }

}