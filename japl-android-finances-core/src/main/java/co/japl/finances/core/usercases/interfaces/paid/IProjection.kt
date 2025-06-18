package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import java.math.BigDecimal
import java.time.LocalDate

interface IProjection {

    fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>>

    fun getProjections():List<ProjectionDTO>

    fun delete(id:Int):Boolean

    fun save(projection: ProjectionDTO): Boolean

    fun update(projection: ProjectionDTO): Boolean

    fun findById(id: Int): ProjectionDTO?

    fun calculateQuote(period: KindPaymentsEnums, date: LocalDate, value: BigDecimal): BigDecimal

    fun savedAmount(quote:Double, monthsLeft:Int, months:Int): Double

}