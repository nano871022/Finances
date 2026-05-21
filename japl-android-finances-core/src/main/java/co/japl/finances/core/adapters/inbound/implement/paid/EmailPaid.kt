package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.japl.finances.core.usercases.interfaces.paid.IEmailPaid
import javax.inject.Inject

class EmailPaid @Inject constructor(val svc: IEmailPaid) : IEmailPaidPort {
    override fun create(dto: EmailPaidDTO): Int = svc.create(dto)

    override fun update(dto: EmailPaidDTO): Boolean = svc.update(dto)

    override fun getById(id: Int): EmailPaidDTO? = svc.getById(id)

    override fun getAll(): List<EmailPaidDTO> = svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)

    override fun activate(id: Int, active: Boolean): Boolean = svc.activate(id, active)

    override fun clone(id: Int): Boolean = svc.clone(id)

    override fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO> = svc.validateMessagePattern(dto, numDaysRead)

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String> = svc.getEmailList(sender, subject, numDaysRead)
}
