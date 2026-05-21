package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.outbounds.IEmailPaidPort
import co.japl.android.finances.services.dao.interfaces.IEmailPaidDAO
import javax.inject.Inject

class EmailPaidImpl @Inject constructor(val svc: IEmailPaidDAO) : IEmailPaidPort {
    override fun create(dto: EmailPaidDTO): Int = svc.create(dto)

    override fun update(dto: EmailPaidDTO): Boolean = svc.update(dto)

    override fun getById(id: Int): EmailPaidDTO? = svc.getById(id)

    override fun getAll(): List<EmailPaidDTO> = svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)
}
