package co.japl.android.finances.services.implement

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.inbounds.common.IEmailRead
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPattern
import java.util.regex.Pattern
import javax.inject.Inject

class EmailCreditCardPatternImpl @Inject constructor(private val emailRead: IEmailRead) : IEmailCreditCardPattern {

    override fun validateMessagePattern(dto: EmailCreditCardDTO): List<String> {
        val emails = emailRead.getEmails(dto.sender, dto.subjectPattern)
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
                
                "Match Found:\nName: $name\nValue: $value\nDate: $date"
            } else {
                "No match found.\nBody snippet: ${body.take(100).replace("\n", " ")}..."
            }
        }
    }
}
