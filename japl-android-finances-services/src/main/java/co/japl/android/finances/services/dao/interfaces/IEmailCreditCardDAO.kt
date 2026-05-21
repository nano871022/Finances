package co.japl.android.finances.services.dao.interfaces

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO

interface IEmailCreditCardDAO {

    fun create(dto: EmailCreditCardDTO): Int

    fun update(dto: EmailCreditCardDTO): Boolean

    fun getById(id: Int): EmailCreditCardDTO?

    fun getAll(): List<EmailCreditCardDTO>

    fun delete(id: Int): Boolean
}