package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.japl.android.finances.services.entities.SmsCreditCard
import java.time.LocalDateTime

object SmsCreditCardMapper {

    fun mapping(dto:SmsCreditCard):SMSCreditCard{
        return SMSCreditCard(
            id = dto.id!!,
            codeCreditCard = dto.codeCreditCard!!,
            nameCreditCard = dto.nameCreditCard!!,
            kindInterestRateEnum = dto.kindInterestRateEnum!!,
            phoneNumber = dto.phoneNumber!!,
            pattern = dto.pattern!!,
            active = dto.active!!
        )
    }

    fun mapping(dto:SMSCreditCard):SmsCreditCard{
        return SmsCreditCard(
            id = dto.id,
            codeCreditCard = dto.codeCreditCard,
            nameCreditCard = dto.nameCreditCard,
            kindInterestRateEnum = dto.kindInterestRateEnum,
            phoneNumber = dto.phoneNumber,
            pattern = dto.pattern,
            active = dto.active,
            create = LocalDateTime.now()
        )
    }
}