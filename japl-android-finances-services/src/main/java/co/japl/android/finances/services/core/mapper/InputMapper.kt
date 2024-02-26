package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.InputDTO

object InputMapper {

    fun mapper(inputDTO: InputDTO):co.com.japl.finances.iports.dtos.InputDTO {
        return co.com.japl.finances.iports.dtos.InputDTO(
            id = inputDTO.id,
            name = inputDTO.name,
            value = inputDTO.value,
            dateStart = inputDTO.dateStart,
            dateEnd = inputDTO.dateEnd,
            kindOf = inputDTO.kindOf,
            date = inputDTO.date,
            accountCode = inputDTO.accountCode
        )
    }

    fun mapper(input:co.com.japl.finances.iports.dtos.InputDTO):InputDTO{
        return InputDTO(
            id = input.id,
            name = input.name,
            value = input.value,
            dateStart = input.dateStart,
            dateEnd = input.dateEnd,
            kindOf = input.kindOf,
            date = input.date,
            accountCode = input.accountCode
        )
    }
}