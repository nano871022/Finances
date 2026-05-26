package co.japl.android.finances.services.implement

import android.util.Log
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.common.IEmailRead
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPattern
import co.japl.finances.core.utils.DateUtils
import co.japl.finances.core.utils.ExtractItemPatternUtil
import java.time.LocalDateTime
import java.util.regex.Pattern
import javax.inject.Inject

class EmailCreditCardPatternImpl @Inject constructor(private val emailRead: IEmailRead) : IEmailCreditCardPattern {

    override fun validateMessagePattern(dto: EmailCreditCardDTO, numDaysRead: Int): List<EmailValidationDTO> {
        val emails = emailRead.getEmails(dto.sender, dto.subjectPattern, numDaysRead)
        return emails.map { body ->
            dto.bodyPattern.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(body.first).takeIf { it }?.let {
                dto.bodyPattern.toRegex().find(body.first)?.let {
                    ExtractItemPatternUtil.getValues(it.groupValues, body.second)?.let {
                        EmailValidationDTO(
                            name = it.first,
                            value = it.second.toString(),
                            date = DateUtils.localDateTimeToString(it.third),
                            matched = true,
                            bodySnippet = body.first.take(100).replace("\n", " ")
                        )
                    }
                }
            }?:EmailValidationDTO(matched = false, bodySnippet = body.first.take(100).replace("\n", " "))
        }
    }

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<Pair<String, LocalDateTime>> {
        return emailRead.getEmails(sender, subject, numDaysRead)
    }
}
