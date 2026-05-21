package co.japl.android.finances.services.implement

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.common.IEmailRead
import co.com.japl.finances.iports.outbounds.IEmailPaidPattern
import java.util.regex.Pattern
import javax.inject.Inject

class EmailPaidPatternImpl @Inject constructor(private val emailRead: IEmailRead) : IEmailPaidPattern {

    override fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO> {
        val emails = emailRead.getEmails(dto.sender, dto.subjectPattern, numDaysRead)
        val pattern = Pattern.compile(dto.bodyPattern, Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)

        return emails.map { body ->
            val matcher = pattern.matcher(body)
            if (matcher.find()) {
                val name = try { matcher.group("name") } catch (e: Exception) {
                    if (matcher.groupCount() >= 1) matcher.group(1) else ""
                }
                val value = try { matcher.group("value") } catch (e: Exception) {
                    if (matcher.groupCount() >= 2) matcher.group(2) else ""
                }
                val date = try { matcher.group("date") } catch (e: Exception) {
                    if (matcher.groupCount() >= 3) matcher.group(3) else ""
                }

                EmailValidationDTO(name = name, value = value, date = date, matched = true, bodySnippet = body.take(100).replace("\n", " "))
            } else {
                EmailValidationDTO(matched = false, bodySnippet = body.take(100).replace("\n", " "))
            }
        }
    }

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String> {
        return emailRead.getEmails(sender, subject, numDaysRead)
    }
}
