package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.EmailPaidDTO

interface IEmailPaidPort {

    fun create(dto: EmailPaidDTO): Int

    fun update(dto: EmailPaidDTO): Boolean

    fun getById(id: Int): EmailPaidDTO?

    fun getAll(): List<EmailPaidDTO>

    fun delete(id: Int): Boolean
}
