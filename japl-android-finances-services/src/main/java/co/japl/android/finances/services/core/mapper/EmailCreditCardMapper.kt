package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.japl.android.finances.services.entities.EmailCreditCard
import java.time.LocalDateTime

object EmailCreditCardMapper {

    fun mapping(dto: EmailCreditCardDTO): EmailCreditCard {
        return EmailCreditCard(
            id = dto.id,
            sender = dto.sender,
            subjectPattern = dto.subjectPattern,
            bodyPattern = dto.bodyPattern,
            codeCreditCard = dto.codeCreditCard,
            nameCreditCard = dto.nameCreditCard,
            kindInterestRateEnum = dto.kindInterestRateEnum,
            active = dto.active,
            create = LocalDateTime.now()
        )
    }

    fun mapping(entity: EmailCreditCard): EmailCreditCardDTO {
        return EmailCreditCardDTO(
            id = entity.id ?: 0,
            sender = entity.sender ?: "",
            subjectPattern = entity.subjectPattern ?: "",
            bodyPattern = entity.bodyPattern ?: "",
            codeCreditCard = entity.codeCreditCard ?: 0,
            nameCreditCard = entity.nameCreditCard ?: "",
            kindInterestRateEnum = entity.kindInterestRateEnum!!,
            active = entity.active ?: false
        )
    }
}
