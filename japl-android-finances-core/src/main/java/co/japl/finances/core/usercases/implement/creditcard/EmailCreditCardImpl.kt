package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPattern
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.IEmailCreditCard
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(val svc: IEmailCreditCardPort, val messageSvc: IEmailCreditCardPattern) : IEmailCreditCard {
    override fun create(dto: EmailCreditCardDTO): Int = svc.create(dto)

    override fun update(dto: EmailCreditCardDTO): Boolean =svc.update(dto)

    override fun getById(id: Int): EmailCreditCardDTO? = svc.getById(id)

    override fun getAll(): List<EmailCreditCardDTO> =svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)

    override fun activate(id: Int, active: Boolean): Boolean = getById(id)?.let{update(it.copy(active = active))}?:false

    override fun clone(id: Int): Boolean =getById(id)?.let{create(it.copy(id=0))!=0}?:false

    override fun validateMessagePattern(dto: EmailCreditCardDTO): List<EmailValidationDTO> = messageSvc.validateMessagePattern(dto)

    override fun getEmailList(sender: String, subject: String): List<String> = messageSvc.getEmailList(sender, subject)
}