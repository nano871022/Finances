package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import java.time.LocalDateTime

interface ISms {
    fun createBySms(dto: PaidDTO): Boolean

}