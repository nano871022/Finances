package co.japl.finances.core.adapters.inbound.implement.common

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.japl.finances.core.usercases.interfaces.paid.ICheckPayment
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class CheckPaymentsImpl @Inject constructor(
    private val paidSvc:ICheckPayment,
    private val creditSvc:co.japl.finances.core.usercases.interfaces.credit.ICheckPayment,
    private val creditCardSvc:co.japl.finances.core.usercases.interfaces.creditcard.ICheckPayment
):ICheckPaymentPort{
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        val list = ArrayList<PeriodCheckPaymentDTO>()
        paidSvc.getPeriodsPayment().takeIf { it.isNotEmpty() }?.let{ list.addAll(it) }
        creditSvc.getPeriodsPayment().takeIf { it.isNotEmpty() }?.let{ list.addAll(it) }
        creditCardSvc.getPeriodsPayment().takeIf { it.isNotEmpty() }?.let{ list.addAll(it) }
        return list.groupBy {
            it.period
        }.map {
            it.value[0].copy(
                amount = it.value.sumOf { it.amount },
                count = it.value.sumOf { it.count },
                paid = it.value.sumOf { it.paid }
            )
        }
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        val list = mutableListOf<CheckPaymentDTO>()
        paidSvc.getCheckPayments(period).takeIf { it.isNotEmpty() }?.let( list::addAll )
        creditSvc.getCheckPayments(period).takeIf { it.isNotEmpty() }?.let( list::addAll )
        creditCardSvc.getCheckPayments(period).takeIf { it.isNotEmpty() }?.let( list::addAll )
        return list
    }

    override fun update(check: CheckPaymentDTO): Boolean {
        require(check.id > 0){"Id must be greater than 0"}
        require(check.update){"Updated must be true"}
        if(check.check) {
            check.date = LocalDateTime.now()
        }
        return when(check.type){
            CheckPaymentsEnum.PAYMENTS -> paidSvc.update(check)
            CheckPaymentsEnum.CREDITS -> creditSvc.update(check)
            CheckPaymentsEnum.QUOTE_CREDIT_CARD -> creditCardSvc.update(check)
        }

    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        require(check.id == 0){"Id must be 0"}
        return when(check.type){
            CheckPaymentsEnum.PAYMENTS -> paidSvc.save(check)
            CheckPaymentsEnum.CREDITS -> creditSvc.save(check)
            CheckPaymentsEnum.QUOTE_CREDIT_CARD -> creditCardSvc.save(check)
        }
    }
}