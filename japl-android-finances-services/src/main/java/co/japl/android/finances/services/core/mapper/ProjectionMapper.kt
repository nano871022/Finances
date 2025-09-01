package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.japl.android.finances.services.dto.ProjectionDTO

object ProjectionMapper {

    fun mapper(projectionDTO: ProjectionDTO):co.com.japl.finances.iports.dtos.ProjectionDTO{
        return co.com.japl.finances.iports.dtos.ProjectionDTO(
            id = projectionDTO.id,
            name = projectionDTO.name,
            value = projectionDTO.value,
            create = projectionDTO.create,
            end = projectionDTO.end,
            type = KindPaymentsEnums.find(projectionDTO.type.uppercase()),
            quote = projectionDTO.quote,
            active = when(projectionDTO.active.toInt()){
                0-> false
                1-> true
                else -> false
            }
        )
    }

    fun mapper(projectionDTO: co.com.japl.finances.iports.dtos.ProjectionDTO):ProjectionDTO{
        return ProjectionDTO(
            id = projectionDTO.id,
            name = projectionDTO.name,
            value = projectionDTO.value,
            create = projectionDTO.create,
            end = projectionDTO.end,
            type = projectionDTO.type.toString(),
            quote = projectionDTO.quote,
            active = if(projectionDTO.active) 1 else 0
        )
    }
}