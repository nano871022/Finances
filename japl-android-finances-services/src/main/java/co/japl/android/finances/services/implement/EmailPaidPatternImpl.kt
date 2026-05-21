package co.japl.android.finances.services.implement

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.common.IEmailRead
import co.com.japl.finances.iports.outbounds.IEmailPaidPattern
import co.japl.finances.core.utils.DateUtils
import java.util.regex.Pattern
import co.japl.finances.core.utils.ExtractItemPatternUtil
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EmailPaidPatternImpl @Inject constructor(private val emailRead: IEmailRead) : IEmailPaidPattern {

    override fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO> {
        val emails = emailRead.getEmails(dto.sender, dto.subjectPattern, numDaysRead)

        return emails.map{ it.replace("\\s+".toRegex()," ")}.map { body ->
            dto.bodyPattern.toRegex().containsMatchIn(body)?.takeIf { it }?.let {
                dto.bodyPattern.toRegex().find(body)?.let {
                    ExtractItemPatternUtil.getValues(it.groupValues!!)?.let {
                        EmailValidationDTO(
                            name = it.first,
                            value = it.second.toString(),
                            date = DateUtils.localDateTimeToString(it.third),
                            matched = true,
                            bodySnippet = body.take(100).replace("\n", " ")
                        )
                    }
                }
            } ?: EmailValidationDTO(
                matched = false,
                bodySnippet = body.take(100).replace("\n", " ")
            )
        }
    }

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String> {
        return emailRead.getEmails(sender, subject, numDaysRead)
    }
}
