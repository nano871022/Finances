package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.finances.core.usercases.interfaces.creditcard.IEmailCreditCard
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort as IEmailCreditCardOutPort
import co.com.japl.finances.iports.outbounds.IGmailPort
import co.japl.finances.core.utils.SmsUtil
import java.time.LocalDateTime
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(private val svc: IEmailCreditCardOutPort, private val gmailPort: IGmailPort) : IEmailCreditCard {

    override fun create(dto: EmailCreditCardDTO): Int {
        return svc.create(dto)
    }

    override fun update(dto: EmailCreditCardDTO): Boolean {
        return svc.update(dto)
    }

    override fun delete(codeEmailCreditCard: Int): Boolean {
        return svc.delete(codeEmailCreditCard)
    }

    override fun getById(codeEmailCreditCard: Int): EmailCreditCardDTO? {
        return svc.getById(codeEmailCreditCard)
    }

    override suspend fun validateMessagePattern(dto: EmailCreditCardDTO): List<String> {
        val list = mutableListOf<String>()
        val emails = gmailPort.loadEmails(dto.sender)
        emails.forEach { (subject, body) ->
            getEmailMessages(dto.subjectPattern, dto.bodyPattern, subject, body)?.let {
                list.add("OK $it")
            } ?: list.add("Not matched: Subject: $subject Snippet: $body")
        }
        return list
    }

    override fun getAllByCodeCreditCard(codeCreditCard: Int): List<EmailCreditCardDTO> {
        return svc.getByCodeCreditCard(codeCreditCard)
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<EmailCreditCardDTO> {
        return svc.getByCreditCardAndKindInterest(codeCreditCard, kind)
    }

    override suspend fun getEmailMessages(sender: String, subjectPattern: String, bodyPattern: String): List<Triple<String, Double, LocalDateTime>> {
        val list = mutableListOf<Triple<String, Double, LocalDateTime>>()
        val emails = gmailPort.loadEmails(sender)
        emails.forEach { (subject, body) ->
            getEmailMessages(subjectPattern, bodyPattern, subject, body)?.let {
                list.add(it)
            }
        }
        return list
    }

    override fun getEmailMessages(subjectPattern: String, bodyPattern: String, subject: String, body: String): Triple<String, Double, LocalDateTime>? {
        // Try to find values in subject first, then in body
        var result: Triple<String, Double, LocalDateTime>? = null
        if (subjectPattern.isNotEmpty() && subjectPattern.toRegex().containsMatchIn(subject)) {
            subjectPattern.toRegex().find(subject)?.let {
                result = SmsUtil.getValues(it.groupValues)
            }
        }
        if (result == null && bodyPattern.isNotEmpty() && bodyPattern.toRegex().containsMatchIn(body)) {
            bodyPattern.toRegex().find(body)?.let {
                result = SmsUtil.getValues(it.groupValues)
            }
        }
        return result
    }

    override fun enable(codeEmailCreditCard: Int): Boolean {
        return svc.getById(codeEmailCreditCard)?.takeIf { !it.active }?.let {
            return svc.update(it.copy(active = true))
        } ?: false
    }

    override fun disable(codeEmailCreditCard: Int): Boolean {
        return svc.getById(codeEmailCreditCard)?.takeIf { it.active }?.let {
            return svc.update(it.copy(active = false))
        } ?: false
    }
}
