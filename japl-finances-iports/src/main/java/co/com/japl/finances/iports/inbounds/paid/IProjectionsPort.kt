package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.ProjectionRecap
import java.math.BigDecimal

interface IProjectionsPort {

    fun getProjectionRecap(): Triple<Int, BigDecimal,List<ProjectionRecap>>
}