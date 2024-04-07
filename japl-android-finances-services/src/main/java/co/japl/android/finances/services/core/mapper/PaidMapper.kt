package co.japl.android.finances.services.core.mapper

import java.time.LocalDateTime
import java.time.LocalTime

object PaidMapper {

    fun mapper(paid: co.japl.android.finances.services.dto.PaidDTO):co.com.japl.finances.iports.dtos.PaidDTO {
        return co.com.japl.finances.iports.dtos.PaidDTO(
            datePaid = LocalDateTime.of(paid.date, LocalTime.MIN),
            itemName = paid.name,
            itemValue = paid.value.toDouble(),
            id = paid.id,
            account = if("[\\w\\d]+".toRegex().containsMatchIn(paid.account)){
                "(\\d+)\\.".toRegex().find(paid.account)?.groupValues?.get(1)?.toInt()?:0
            }else{ paid.account.toInt()},
            recurrent = paid.recurrent.toInt() == 1,
            end = LocalDateTime.of(paid.end, LocalTime.MIN)
        )
    }

    fun mapper(paid: co.com.japl.finances.iports.dtos.PaidDTO):co.japl.android.finances.services.dto.PaidDTO {
        return co.japl.android.finances.services.dto.PaidDTO(
            date = paid.datePaid.toLocalDate(),
            name = paid.itemName,
            value = paid.itemValue.toBigDecimal(),
            id = paid.id,
            account = paid.account.toString(),
            recurrent = if(paid.recurrent)1 else 0,
            end = paid.end.toLocalDate()
        )
    }
}