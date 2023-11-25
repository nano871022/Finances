package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.ProjectionMapper
import co.japl.android.finances.services.interfaces.IProjectionsSvc
import co.japl.finances.core.adapters.outbound.interfaces.IProjectionsRecapPort
import co.japl.finances.core.dto.ProjectionDTO
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(private val projectionsImpl:IProjectionsSvc): IProjectionsRecapPort {
    override fun getAllActive(): List<ProjectionDTO> {
        return projectionsImpl.getAllActive().map (ProjectionMapper::mapper)
    }
}