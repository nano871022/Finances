package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort
import co.japl.android.finances.services.core.mapper.CheckPaymentMapper
import co.japl.android.finances.services.dao.interfaces.ICheckQuoteDAO
import java.time.YearMonth
import javax.inject.Inject

class CreditCardCheckPaymentImpl @Inject constructor(private val svc:ICheckQuoteDAO): ICreditCardCheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment().map(CheckPaymentMapper::mapper)
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        return svc.getAll().map{
            CheckPaymentMapper.mapper(it,CheckPaymentsEnum.QUOTE_CREDIT_CARD)
        }
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        val response = svc.save(CheckPaymentMapper.mapperQuote(check))
        return check.copy(id = response.toInt())
    }
}