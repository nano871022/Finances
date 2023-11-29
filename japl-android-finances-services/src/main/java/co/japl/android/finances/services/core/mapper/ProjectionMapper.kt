package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.ProjectionDTO

object ProjectionMapper {

    fun mapper(projectionDTO: ProjectionDTO):co.com.japl.finances.iports.dtos.ProjectionDTO{
        return co.com.japl.finances.iports.dtos.ProjectionDTO(
            id = projectionDTO.id,
            name = projectionDTO.name,
            value = projectionDTO.value,
            create = projectionDTO.create,
            end = projectionDTO.end,
            type = projectionDTO.type,
            quote = projectionDTO.quote,
            active = projectionDTO.active
        )
    }
}