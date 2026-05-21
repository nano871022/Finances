package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO

interface IEmailPaid {
    fun create(dto: EmailPaidDTO): Int
    fun update(dto: EmailPaidDTO): Boolean
    fun getById(id: Int): EmailPaidDTO?
    fun getAll(): List<EmailPaidDTO>
    fun delete(id: Int): Boolean
    fun activate(id: Int, active: Boolean): Boolean
    fun clone(id: Int): Boolean
    fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO>
    fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String>

    fun read(numDaysRead: Int)
}
