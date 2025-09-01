package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class ProjectionImpl @Inject constructor(private val projectionSvc: IProjection) : IProjectionsPort , IProjectionListPort, IProjectionFormPort{

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

    override fun save(projection: ProjectionDTO): Boolean {
        require(projection.id == 0){"El id no puede ser mayor a cero"}
        return projectionSvc.save(projection)
    }

    override fun update(projection: ProjectionDTO): Boolean {
        require(projection.id > 0){"El id no puede ser cero"}
        return projectionSvc.update(projection)
    }

    override fun findById(id: Int): ProjectionDTO? {
        require(id>=0){"El id no puede ser cero"}
        return projectionSvc.findById(id)
    }

    override fun calculateQuote(
        period: KindPaymentsEnums,
        date: LocalDate,
        value: BigDecimal
    ): BigDecimal {
        require(value > BigDecimal.ZERO){"El valor no puede ser cero"}
        return projectionSvc.calculateQuote(period, date, value)
    }

}