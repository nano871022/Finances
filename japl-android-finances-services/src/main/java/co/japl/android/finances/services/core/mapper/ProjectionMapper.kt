package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.ProjectionDTO

object ProjectionMapper {

    fun mapper(projectionDTO: ProjectionDTO):co.japl.finances.core.dto.ProjectionDTO{
        return co.japl.finances.core.dto.ProjectionDTO(
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