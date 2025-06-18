package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO

interface IProjectionListPort {

    fun getProjections():List<ProjectionDTO>

    fun delete(id:Int):Boolean
}