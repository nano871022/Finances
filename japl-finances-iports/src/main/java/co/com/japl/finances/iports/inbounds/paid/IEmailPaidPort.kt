package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO

interface IEmailPaidPort {

    fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO>

    fun create(dto: EmailPaidDTO): Int

    fun update(dto: EmailPaidDTO): Boolean

    fun getById(id: Int): EmailPaidDTO?

    fun getAll(): List<EmailPaidDTO>

    fun activate(id: Int, active: Boolean): Boolean

    fun delete(id: Int): Boolean

    fun clone(id: Int): Boolean

    fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String>

    fun read(numDaysRead: Int)
}
