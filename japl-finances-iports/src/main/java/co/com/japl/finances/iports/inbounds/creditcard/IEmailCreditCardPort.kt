package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

interface IEmailCreditCardPort {

    fun create(dto: EmailCreditCardDTO): Int

    fun update(dto: EmailCreditCardDTO): Boolean

    fun delete(codeEmailCreditCard: Int): Boolean

    fun getById(codeEmailCreditCard: Int): EmailCreditCardDTO?

    suspend fun validateMessagePattern(dto: EmailCreditCardDTO): List<String>

    fun getByCreditCardAndKindInterest(codeCreditCard: Int, kind: KindInterestRateEnum): List<EmailCreditCardDTO>

    fun getAllByCodeCreditCard(codeCreditCard: Int): List<EmailCreditCardDTO>

    suspend fun getEmailMessages(sender: String, subjectPattern: String, bodyPattern: String): List<Triple<String, Double, LocalDateTime>>

    fun getEmailMessages(subjectPattern: String, bodyPattern: String, subject: String, body: String): Triple<String, Double, LocalDateTime>?

    fun enable(codeEmailCreditCard: Int): Boolean

    fun disable(codeEmailCreditCard: Int): Boolean

}
