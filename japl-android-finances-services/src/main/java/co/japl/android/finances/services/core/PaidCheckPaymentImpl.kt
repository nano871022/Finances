package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.IPaidCheckPaymentPort
import co.japl.android.finances.services.core.mapper.CheckPaymentMapper
import co.japl.android.finances.services.dao.interfaces.ICheckPaymentDAO
import co.japl.android.finances.services.dto.CheckPaymentsDTO
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PaidCheckPaymentImpl @Inject constructor(private val svc:ICheckPaymentDAO) : IPaidCheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment().map(CheckPaymentMapper::mapper)
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        return svc.get(
            CheckPaymentsDTO(
                id = 0,
                date = LocalDateTime.MIN,
                check= -1,
                codPaid = 0,
                period = period.format(DateTimeFormatter.ofPattern("yyyyMM")),
            )
        ).map{CheckPaymentMapper.mapper(it,CheckPaymentsEnum.PAYMENTS)}
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        val response = svc.save(CheckPaymentMapper.mapperPaid(check))
        return check.copy(id = response.toInt())
    }
}