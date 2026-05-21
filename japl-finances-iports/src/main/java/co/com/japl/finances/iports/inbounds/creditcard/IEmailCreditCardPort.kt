package co.com.japl.finances.iports.inbounds.creditcard;

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO

interface IEmailCreditCardPort{

    fun validateMessagePattern(dto: EmailCreditCardDTO):List<EmailValidationDTO>

    fun create(dto: EmailCreditCardDTO): Int

    fun update(dto: EmailCreditCardDTO): Boolean

    fun getById(id: Int): EmailCreditCardDTO?

    fun getAll(): List<EmailCreditCardDTO>

    fun activate(id:Int, active:Boolean):Boolean

    fun delete(id:Int): Boolean

    fun clone(id:Int):Boolean

    fun getEmailList(sender: String, subject: String): List<String>
}