package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort
import co.japl.android.finances.services.core.mapper.CheckPaymentMapper
import co.japl.android.finances.services.dao.interfaces.ICheckQuoteDAO
import co.japl.android.finances.services.dto.CheckQuoteDTO
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreditCardCheckPaymentImpl @Inject constructor(private val svc:ICheckQuoteDAO): ICreditCardCheckPaymentPort {
    override fun getPeriodsPayment(): List<Array<Any>> {
        return svc.getPeriodsPayment().map {
            val list = it.period.split("::")
            val count = it.count
            val period = list[0]
            val codCreditCard = list[1].toInt()
            val check = list[2].toInt() == 1
            arrayOf(period,codCreditCard,count,check)
        }
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        return svc.get(
            CheckQuoteDTO(
                id = 0,
                date = LocalDateTime.MIN,
                check= -1,
                codQuote = 0,
                period = period.format(DateTimeFormatter.ofPattern("yyyyMM")),
            )
        ).map{
            CheckPaymentMapper.mapper(it,CheckPaymentsEnum.QUOTE_CREDIT_CARD)
        }
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        val response = svc.save(CheckPaymentMapper.mapperQuote(check))
        return check.copy(id = response.toInt())
    }
}