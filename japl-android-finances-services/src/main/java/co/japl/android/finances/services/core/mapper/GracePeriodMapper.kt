package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.GracePeriodDTO
import co.com.japl.finances.iports.dtos.GracePeriodDTO as GracePeriodDTO2

object GracePeriodMapper {

    fun mapper(dto:GracePeriodDTO2):GracePeriodDTO{
        return GracePeriodDTO(
            id = dto.id,
            create = dto.create,
            end = dto.end,
            codeCredit = dto.codeCredit,
            periods = dto.periods
        )
    }

    fun mapper(dto:GracePeriodDTO):GracePeriodDTO2{
        return GracePeriodDTO2(
            id = dto.id,
            create = dto.create,
            end = dto.end,
            codeCredit = dto.codeCredit,
            periods = dto.periods
        )
    }
}