package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.PaidDTO
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

interface IPaidSvc: SaveSvc<PaidDTO>,ISaveSvc<PaidDTO> {

    fun getTotalPaid(current:LocalDate = LocalDate.now()):BigDecimal
    fun getRecurrent(date: LocalDate):List<PaidDTO>
}