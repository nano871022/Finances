package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.japl.android.finances.services.entities.SmsPaid
import java.time.LocalDateTime

object SmsPaidMapper {

    fun mapping(dto:SmsPaid):SMSPaidDTO{
        return SMSPaidDTO(
            id = dto.id!!,
            codeAccount = dto.codeAccount!!,
            nameAccount = dto.nameAccount!!,
            phoneNumber = dto.phoneNumber!!,
            pattern = dto.pattern!!,
            active = dto.active!!
        )
    }

    fun mapping(dto:SMSPaidDTO):SmsPaid{
        return SmsPaid(
            id = dto.id,
            codeAccount = dto.codeAccount,
            nameAccount = dto.nameAccount,
            phoneNumber = dto.phoneNumber,
            pattern = dto.pattern,
            active = dto.active,
            create = LocalDateTime.now()
        )
    }
}