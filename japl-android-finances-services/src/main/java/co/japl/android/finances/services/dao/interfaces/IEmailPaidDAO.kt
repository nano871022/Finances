package co.japl.android.finances.services.dao.interfaces

import co.com.japl.finances.iports.dtos.EmailPaidDTO

interface IEmailPaidDAO {

    fun create(dto: EmailPaidDTO): Int

    fun update(dto: EmailPaidDTO): Boolean

    fun getById(id: Int): EmailPaidDTO?

    fun getAll(): List<EmailPaidDTO>

    fun delete(id: Int): Boolean
}
