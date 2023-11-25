package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.ProjectionDTO

interface IProjectionsRecapPort {
    fun getAllActive():List<ProjectionDTO>
}