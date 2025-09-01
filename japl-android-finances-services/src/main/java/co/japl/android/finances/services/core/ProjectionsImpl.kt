package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.ProjectionMapper
import co.japl.android.finances.services.dao.interfaces.IProjectionsSvc
import co.com.japl.finances.iports.outbounds.IProjectionsRecapPort
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(private val projectionsImpl:IProjectionsSvc): IProjectionsRecapPort{
    override fun getAllActive(): List<ProjectionDTO> {
        return projectionsImpl.getAllActive().map (ProjectionMapper::mapper)
    }

    override fun delete(id: Int): Boolean {
        return projectionsImpl.delete(id)
    }

    override fun save(projection: ProjectionDTO): Boolean {
        return projectionsImpl.save(ProjectionMapper.mapper(projection)) > 0
    }

    override fun update(projection: ProjectionDTO): Boolean {
        return projectionsImpl.save(ProjectionMapper.mapper(projection)) > 0
    }

    override fun findById(id: Int): ProjectionDTO? {
        return projectionsImpl.get(id).takeIf { it.isPresent }?.let{ value ->
            ProjectionMapper.mapper(value.get())
        }
    }


}