package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.GracePeriodDTO

object GracePeriodMapper {

    fun mapper(dto:co.com.japl.finances.iports.dtos.GracePeriodDTO):GracePeriodDTO{
        return GracePeriodDTO(
            id = dto.id,
            create = dto.create,
            end = dto.end,
            codeCredit = dto.codeCredit,
            periods = dto.periods
        )
    }
}