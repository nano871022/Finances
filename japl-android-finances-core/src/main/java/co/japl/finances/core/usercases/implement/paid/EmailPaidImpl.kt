package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.outbounds.IEmailPaidPattern
import co.com.japl.finances.iports.outbounds.IEmailPaidPort
import co.japl.finances.core.usercases.interfaces.paid.IEmailPaid
import co.japl.finances.core.usercases.interfaces.paid.ISms2
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import javax.inject.Inject

class EmailPaidImpl @Inject constructor(val svc: IEmailPaidPort, val messageSvc: IEmailPaidPattern, val paidSmsSvc: ISms2) : IEmailPaid {
    override fun create(dto: EmailPaidDTO): Int = svc.create(dto)

    override fun update(dto: EmailPaidDTO): Boolean = svc.update(dto)

    override fun getById(id: Int): EmailPaidDTO? = svc.getById(id)

    override fun getAll(): List<EmailPaidDTO> = svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)

    override fun activate(id: Int, active: Boolean): Boolean = getById(id)?.let { update(it.copy(active = active)) } ?: false

    override fun clone(id: Int): Boolean = getById(id)?.let { create(it.copy(id = 0)) != 0 } ?: false

    override fun validateMessagePattern(dto: EmailPaidDTO, numDaysRead: Int): List<EmailValidationDTO> = messageSvc.validateMessagePattern(dto, numDaysRead)

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<Pair<String, LocalDateTime>> = messageSvc.getEmailList(sender, subject, numDaysRead)

    override fun read(numDaysRead: Int) {
        svc.getAll().filter { it.active }.forEach { emailConfig ->
            messageSvc.validateMessagePattern(emailConfig, numDaysRead).filter { it.matched }.forEach { validation ->
                val date = validation.date?.let {
                    DateUtils.toLocalDate(it)
                }?.atStartOfDay()

                val value = validation.value?.toDoubleOrNull()
                if (date != null && value != null) {
                    paidSmsSvc.createBySms(
                        co.com.japl.finances.iports.dtos.PaidDTO(
                            id = 0,
                            itemName = validation.name ?: "",
                            itemValue = value,
                            datePaid = date,
                            account = emailConfig.codeAccount,
                            recurrent = false,
                            end = java.time.LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }
}
