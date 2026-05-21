package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO

interface IEmailCreditCardPort {
    fun create(dto: EmailCreditCardDTO): Int

    fun update(dto: EmailCreditCardDTO): Boolean

    fun getById(id: Int): EmailCreditCardDTO?

    fun getAll(): List<EmailCreditCardDTO>

    fun delete(id: Int): Boolean
}