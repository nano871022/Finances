package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.japl.finances.core.usercases.implement.creditcard.EmailCreditCardImpl
import java.time.LocalDateTime
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(private val svc: EmailCreditCardImpl) : IEmailCreditCardPort {

    override fun create(dto: EmailCreditCardDTO): Int {
        require(dto.id == 0) { "Id must be zero" }
        return svc.create(dto)
    }

    override fun update(dto: EmailCreditCardDTO): Boolean {
        require(dto.id != 0) { "Id must not be zero" }
        return svc.update(dto)
    }

    override fun delete(codeEmailCreditCard: Int): Boolean {
        require(codeEmailCreditCard != 0) { "Id must not be zero" }
        return svc.delete(codeEmailCreditCard)
    }

    override fun getById(codeEmailCreditCard: Int): EmailCreditCardDTO? {
        require(codeEmailCreditCard != 0) { "Id must not be zero" }
        return svc.getById(codeEmailCreditCard)
    }

    override fun validateMessagePattern(dto: EmailCreditCardDTO): List<String> {
        return svc.validateMessagePattern(dto)
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<EmailCreditCardDTO> {
        require(codeCreditCard != 0) { "Id must not be zero" }
        return svc.getByCreditCardAndKindInterest(codeCreditCard, kind)
    }

    override fun getAllByCodeCreditCard(codeCreditCard: Int): List<EmailCreditCardDTO> {
        return svc.getAllByCodeCreditCard(codeCreditCard)
    }

    override fun getEmailMessages(
        sender: String,
        subjectPattern: String,
        bodyPattern: String
    ): List<Triple<String, Double, LocalDateTime>> {
        require(sender.isNotEmpty()) { "Sender must not be empty" }
        return svc.getEmailMessages(sender, subjectPattern, bodyPattern)
    }

    override fun getEmailMessages(
        subjectPattern: String,
        bodyPattern: String,
        subject: String,
        body: String
    ): Triple<String, Double, LocalDateTime>? {
        return svc.getEmailMessages(subjectPattern, bodyPattern, subject, body)
    }

    override fun enable(codeEmailCreditCard: Int): Boolean {
        return svc.enable(codeEmailCreditCard)
    }

    override fun disable(codeEmailCreditCard: Int): Boolean {
        return svc.disable(codeEmailCreditCard)
    }
}
