package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPattern
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.IEmailCreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(val svc: IEmailCreditCardPort, val messageSvc: IEmailCreditCardPattern, val boughtSmsSvc: IBoughtSms) : IEmailCreditCard {
    override fun create(dto: EmailCreditCardDTO): Int = svc.create(dto)

    override fun update(dto: EmailCreditCardDTO): Boolean =svc.update(dto)

    override fun getById(id: Int): EmailCreditCardDTO? = svc.getById(id)

    override fun getAll(): List<EmailCreditCardDTO> =svc.getAll()

    override fun delete(id: Int): Boolean = svc.delete(id)

    override fun activate(id: Int, active: Boolean): Boolean = getById(id)?.let{update(it.copy(active = active))}?:false

    override fun clone(id: Int): Boolean =getById(id)?.let{create(it.copy(id=0))!=0}?:false

    override fun validateMessagePattern(dto: EmailCreditCardDTO, numDaysRead: Int): List<EmailValidationDTO> = messageSvc.validateMessagePattern(dto, numDaysRead)

    override fun getEmailList(sender: String, subject: String, numDaysRead: Int): List<Pair<String, LocalDateTime>> = messageSvc.getEmailList(sender, subject, numDaysRead)

    override fun read(numDaysRead: Int) {
        val startDate = LocalDate.now().minusDays(numDaysRead.toLong())
        svc.getAll().filter { it.active }.forEach { emailConfig ->
            messageSvc.validateMessagePattern(emailConfig, numDaysRead).filter { it.matched }.forEach { validation ->
                val date = validation.date?.let { DateUtils.toLocalDateRegex(it) }
                val value = validation.value?.toDoubleOrNull()
                if (date != null && value != null && (date.isAfter(startDate) || date.isEqual(startDate))) {
                    boughtSmsSvc.createBySms(
                        co.com.japl.finances.iports.dtos.CreditCardBoughtDTO(
                            id = 0,
                            nameItem = validation.name ?: "",
                            valueItem = value.toBigDecimal(),
                            boughtDate = date.atStartOfDay(),
                            codeCreditCard = emailConfig.codeCreditCard,
                            kind = emailConfig.kindInterestRateEnum,
                            endDate = java.time.LocalDateTime.MAX,
                            cutOutDate = java.time.LocalDateTime.now(),
                            createDate = java.time.LocalDateTime.now(),
                            interest = 0.0,
                            kindOfTax = co.com.japl.finances.iports.enums.KindOfTaxEnum.MONTHLY_EFFECTIVE,
                            month = 1,
                            nameCreditCard = "",
                            recurrent = 0
                        )
                    )
                }
            }
        }
    }
}