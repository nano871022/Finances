package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort

class ProjectionListPortFake : IProjectionListPort {
    override fun getProjections(): List<ProjectionDTO> {
        return emptyList()
    }

    override fun delete(id: Int): Boolean {
        return true
    }
}
