package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import java.time.LocalDateTime

interface IEmailCreditCard {

    fun validateMessagePattern(dto: EmailCreditCardDTO, numDaysRead: Int): List<EmailValidationDTO>

    fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<Pair<String, LocalDateTime>>

    fun create(dto: EmailCreditCardDTO): Int

    fun update(dto: EmailCreditCardDTO): Boolean

    fun getById(id: Int): EmailCreditCardDTO?

    fun getAll(): List<EmailCreditCardDTO>

    fun activate(id: Int, active: Boolean): Boolean

    fun delete(id: Int): Boolean

    fun clone(id: Int): Boolean

    fun read(numDaysRead: Int)
}