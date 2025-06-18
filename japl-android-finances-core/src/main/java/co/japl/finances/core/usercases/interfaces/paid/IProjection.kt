package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.dtos.ProjectionRecap
import java.math.BigDecimal

interface IProjection {

    fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>>

    fun getProjections():List<ProjectionDTO>

    fun delete(id:Int):Boolean

}