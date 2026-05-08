package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort as IEmailCreditCardOutPort
import co.japl.finances.core.utils.SmsUtil
import java.time.LocalDateTime
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(private val svc: IEmailCreditCardOutPort) : IEmailCreditCardPort {

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

    override fun validateMessagePattern(dto: EmailCreditCardDTO): List<String> {
        // Since we don't have a direct way to load emails yet like SMS,
        // we'll return a placeholder or implement logic if we can mock an email string
        return listOf("Email loading not yet implemented for direct validation")
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

    override fun getEmailMessages(sender: String, subjectPattern: String, bodyPattern: String): List<Triple<String, Double, LocalDateTime>> {
        // This will be implemented when we have an IEmailRead interface
        return emptyList()
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
