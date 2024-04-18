package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.japl.android.finances.services.dto.CheckCreditDTO
import co.japl.android.finances.services.dto.CheckPaymentsDTO
import co.japl.android.finances.services.dto.CheckPaymentsPOJO
import co.japl.android.finances.services.dto.CheckQuoteDTO
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

    fun mapper(dto:CheckPaymentsDTO,type:CheckPaymentsEnum):CheckPaymentDTO{
        return CheckPaymentDTO(
            id = dto.id,
            date = dto.date,
            check = dto.check.toInt() == 1,
            period = YearMonth.parse(dto.period, DateTimeFormatter.ofPattern("yyyyMM")),
            codPaid = dto.codPaid.toLong(),
            type = type,
            name=""
        )
    }

    fun mapperPaid(dto:CheckPaymentDTO):CheckPaymentsDTO{
        return CheckPaymentsDTO(
            id = dto.id,
            date = dto.date!!,
            check = if(dto.check) 1 else 0,
            period = DateTimeFormatter.ofPattern("yyyyMM").format(dto.period),
            codPaid = dto.codPaid.toInt()
        )
    }

    fun mapper(dto: CheckQuoteDTO, type:CheckPaymentsEnum):CheckPaymentDTO{
        return CheckPaymentDTO(
            id = dto.id,
            date = dto.date,
            check = dto.check.toInt() == 1,
            period = YearMonth.parse(dto.period, DateTimeFormatter.ofPattern("yyyyMM")),
            codPaid = dto.codQuote.toLong(),
            type = type,
            name=""
        )
    }

    fun mapperQuote(dto: CheckPaymentDTO):CheckQuoteDTO{
        return CheckQuoteDTO(
            id = dto.id,
            date = dto.date!!,
            check = if(dto.check) 1 else 0,
            period = DateTimeFormatter.ofPattern("yyyyMM").format(dto.period),
            codQuote = dto.codPaid.toInt()
        )
    }

    fun mapper(dto: CheckCreditDTO, type:CheckPaymentsEnum):CheckPaymentDTO{
        return CheckPaymentDTO(
            id = dto.id,
            date = dto.date,
            check = dto.check.toInt() == 1,
            period = YearMonth.parse(dto.period, DateTimeFormatter.ofPattern("yyyyMM")),
            codPaid = dto.codCredit.toLong(),
            type = type,
            name=""
        )
    }

    fun mapperCredit(dto: CheckPaymentDTO):CheckCreditDTO{
        return CheckCreditDTO(
            id = dto.id,
            date = dto.date!!,
            check = if(dto.check) 1 else 0,
            period = DateTimeFormatter.ofPattern("yyyyMM").format(dto.period),
            codCredit = dto.codPaid.toInt()
        )
    }
}