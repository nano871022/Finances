package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.outbounds.IProjectionsRecapPort
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class ProjectionImpl @Inject constructor(private val projection:IProjectionsRecapPort): IProjection {

    override fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>> {
        val list = projection.getAllActive()
        val recap = list.map{
            val period = Period.between(LocalDate.now(),it.end)
            val periods = Period.between(it.create,it.end)
            ProjectionRecap(
                limitDate = it.end,
                monthsLeft = period.toTotalMonths().toShort(),
                savedCash = it.quote *  (periods.toTotalMonths() -period.toTotalMonths()).toBigDecimal()
            )
        }.sortedByDescending { it.limitDate }
        val listEnd = arrayListOf<ProjectionRecap>()
        recap.firstOrNull()?.let(listEnd::add)
        recap.lastOrNull()?.let(listEnd::add)
        return Triple(list.size, list.sumOf { it.quote },listEnd)
    }

    override fun getProjections(): List<ProjectionDTO> {
        val list = projection.getAllActive()
        list.forEach {
            val period = Period.between(LocalDate.now(),it.end)
            val periods = Period.between(it.create,it.end)
            it.monthsLeft = period.toTotalMonths().toInt()
            it.amountSaved = it.quote *  (periods.toTotalMonths() -period.toTotalMonths()).toBigDecimal()
        }
        return list
    }

    override fun delete(id: Int): Boolean {
        return projection.delete(id)
    }

}