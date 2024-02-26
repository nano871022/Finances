package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.ProjectionDTO

interface IProjectionsRecapPort {
    fun getAllActive():List<ProjectionDTO>
}