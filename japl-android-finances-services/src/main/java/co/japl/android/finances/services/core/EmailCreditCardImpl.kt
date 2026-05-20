package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort
import co.japl.android.finances.services.dao.interfaces.IEmailCreditCardDAO
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(val svc: IEmailCreditCardDAO) : IEmailCreditCardPort {
    override fun create(dto: EmailCreditCardDTO): Int = svc.create(dto)

    override fun update(dto: EmailCreditCardDTO): Boolean = svc.update(dto)

    override fun getById(id: Int): EmailCreditCardDTO? = svc.getById(id)

    override fun getAll(): List<EmailCreditCardDTO> = svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)
}