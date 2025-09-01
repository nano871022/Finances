package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.ProjectionDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IProjectionsRecapPort {
    fun getAllActive():List<ProjectionDTO>

    fun delete(id:Int):Boolean

    fun save(projection: ProjectionDTO): Boolean

    fun update(projection: ProjectionDTO): Boolean

    fun findById(id: Int): ProjectionDTO?
}