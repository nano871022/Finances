package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import java.time.LocalDateTime

interface IEmailPaidPattern {

    fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO>

    fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<Pair<String, LocalDateTime>>
}
