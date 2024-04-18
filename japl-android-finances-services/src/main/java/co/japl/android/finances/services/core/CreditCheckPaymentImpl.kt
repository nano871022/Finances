package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.ICreditCheckPaymentPort
import co.japl.android.finances.services.core.mapper.CheckPaymentMapper
import co.japl.android.finances.services.dao.interfaces.ICheckCreditDAO
import co.japl.android.finances.services.dto.CheckCreditDB
import co.japl.android.finances.services.dto.CheckCreditDTO
import co.japl.android.finances.services.dto.CheckPaymentsDTO
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreditCheckPaymentImpl @Inject constructor(private val svc:ICheckCreditDAO) : ICreditCheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment().map(CheckPaymentMapper::mapper)
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        return svc.get(
            CheckCreditDTO(
                id = 0,
                date = LocalDateTime.MIN,
                check= -1,
                codCredit = 0,
                period = period.format(DateTimeFormatter.ofPattern("yyyyMM")),
            )
        ).map{CheckPaymentMapper.mapper(it,CheckPaymentsEnum.CREDITS)}
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        val response = svc.save(CheckPaymentMapper.mapperCredit(check))
        return check.copy(id = response.toInt())
    }
}