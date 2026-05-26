package co.japl.finances.core.adapters.inbound.implement.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.IEmailCreditCard
import javax.inject.Inject

class EmailCreditCard @Inject constructor(val svc: IEmailCreditCard) : IEmailCreditCardPort {

    override fun validateMessagePattern(dto: EmailCreditCardDTO, numDaysRead: Int): List<EmailValidationDTO> {
        require(dto.bodyPattern.isNotEmpty()){"Need pattern for find in message"}
        require(dto.subjectPattern.isNotEmpty()){"Need pattern for find in email"}
        require(dto.sender.isNotEmpty()){"Need sender of email"}
        return svc.validateMessagePattern(dto, numDaysRead)
    }

    override fun create(dto: EmailCreditCardDTO): Int {
        require(dto.id == 0){"Id is not 0"}
        require(dto.codeCreditCard != 0){"Code Credit card is not provided"}
        require(dto.bodyPattern.isNotEmpty()){"Need pattern for find in message"}
        require(dto.subjectPattern.isNotEmpty()){"Need pattern for find in email"}
        require(dto.sender.isNotEmpty()){"Need sender of email"}
        return svc.create(dto)
    }

    override fun update(dto: EmailCreditCardDTO): Boolean {
        require(dto.id != 0){"Id is 0"}
        require(dto.codeCreditCard != 0){"Code Credit card is not provided"}
        require(dto.bodyPattern.isNotEmpty()){"Need pattern for find in message"}
        require(dto.subjectPattern.isNotEmpty()){"Need pattern for find in email"}
        require(dto.sender.isNotEmpty()){"Need sender of email"}
        return svc.update(dto)
    }

    override fun getById(id: Int): EmailCreditCardDTO? {
        require(id != 0){"Id is 0"}
        return svc.getById(id)
    }

    override fun getAll(): List<EmailCreditCardDTO> {
        return svc.getAll()
    }

    override fun activate(id: Int, active: Boolean): Boolean {
        require(id != 0){"Id is 0"}
        return svc.activate(id, active)
    }

    override fun delete(id: Int): Boolean {
        require(id != 0){"Id is 0"}
        return svc.delete(id)
    }

    override fun clone(id: Int): Boolean {
        require(id != 0){"Id is 0"}
        return svc.clone(id)
    }

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<String> {
        return svc.getEmailList(sender, subject, numDaysRead).map{it.first}
    }

    override fun read(numDaysRead: Int) {
        svc.read(numDaysRead)
    }
}