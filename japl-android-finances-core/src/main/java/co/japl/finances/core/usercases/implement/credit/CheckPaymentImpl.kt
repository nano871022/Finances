package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.ICreditCheckPaymentPort
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.japl.finances.core.usercases.interfaces.credit.ICheckPayment
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:ICreditCheckPaymentPort,private val creditSvc:ICreditPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        val list = mutableListOf<CheckPaymentDTO>()
        val check = svc.getCheckPayments(period).takeIf { it.isNotEmpty() }?.map {
            creditSvc.getById(it.codPaid.toInt())?.let { credit ->
                it.name = credit.name
                it.amount = credit.quoteValue.toDouble()
            }
            it
        }?: emptyList()
        list.addAll(check)
        creditSvc.getAllActive(period).takeIf { it.isNotEmpty() && it.size > check.size }
            ?.map { credit ->
                CheckPaymentDTO(
                    id = 0,
                    name = credit.name,
                    amount = credit.quoteValue.toDouble(),
                    date = LocalDateTime.of(credit.date, LocalTime.MAX),
                    codPaid = credit.id.toLong(),
                    period = period,
                    type = CheckPaymentsEnum.CREDITS
                )
        }?.takeIf { it.isNotEmpty() }?.let(list::addAll)

        return list
    }

    override fun update(check: CheckPaymentDTO): Boolean {
        return svc.save(check).id > 0
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        return svc.save(check)
    }
}