package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.outbounds.IEmailPaidPattern
import co.com.japl.finances.iports.outbounds.IEmailPaidPort
import co.japl.finances.core.usercases.interfaces.paid.IEmailPaid
import javax.inject.Inject

class EmailPaidImpl @Inject constructor(val svc: IEmailPaidPort, val messageSvc: IEmailPaidPattern) : IEmailPaid {
    override fun create(dto: EmailPaidDTO): Int = svc.create(dto)

    override fun update(dto: EmailPaidDTO): Boolean = svc.update(dto)

    override fun getById(id: Int): EmailPaidDTO? = svc.getById(id)

    override fun getAll(): List<EmailPaidDTO> = svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)

    override fun activate(id: Int, active: Boolean): Boolean = getById(id)?.let { update(it.copy(active = active)) } ?: false

    override fun clone(id: Int): Boolean = getById(id)?.let { create(it.copy(id = 0)) != 0 } ?: false

    override fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO> = messageSvc.validateMessagePattern(dto, numDaysRead)

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String> = messageSvc.getEmailList(sender, subject, numDaysRead)
}
