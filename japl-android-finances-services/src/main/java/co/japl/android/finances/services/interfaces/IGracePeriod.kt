package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.GracePeriodDTO
import java.time.LocalDate
import java.util.Optional

interface IGracePeriod : SaveSvc<GracePeriodDTO> {
    fun get(codCredit:Long): List<GracePeriodDTO>
    fun get(id: Int, date: LocalDate): Optional<GracePeriodDTO>
}