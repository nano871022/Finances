package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.japl.finances.core.enums.AutoLoadKind
import java.time.LocalDateTime

interface ISms2 {
    fun createByAutoLoad(dto: PaidDTO, kind: AutoLoadKind= AutoLoadKind.SMS): Boolean

}