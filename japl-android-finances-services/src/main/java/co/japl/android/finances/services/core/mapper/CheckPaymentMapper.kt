package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object CheckPaymentMapper {

    fun mapper(dto:PeriodCheckPaymentsPOJO):PeriodCheckPaymentDTO{
        return PeriodCheckPaymentDTO(
            period = YearMonth.parse(dto.period, DateTimeFormatter.ofPattern("yyyyMM")),
            count = dto.count.toInt(),
            amount = dto.amount,
            paid = dto.paid
        )
    }
}