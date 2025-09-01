package co.japl.android.finances.services.dao.implement

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.japl.android.finances.services.dto.AddAmortizationDTO as AddAmortizationDTOs

object AddAmortizationMapper {

    fun mapper(entity: AddAmortizationDTOs): ExtraValueAmortizationDTO{
        return ExtraValueAmortizationDTO(
            id = entity.id,
            code = entity.code,
            numQuote = entity.nbrQuote,
            value = entity.value,
            create = entity.create
        )
    }

    fun mapper(dto: ExtraValueAmortizationDTO): AddAmortizationDTOs{
        return AddAmortizationDTOs(
            id = dto.id,
            code = dto.code,
            nbrQuote = dto.numQuote,
            value = dto.value,
            create = dto.create
        )
    }
}