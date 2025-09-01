package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.GracePeriodDTO
import co.japl.android.finances.services.interfaces.SaveSvc
import java.time.LocalDate
import java.util.Optional

interface IGracePeriodDAO : SaveSvc<GracePeriodDTO> {
    fun get(codCredit:Long): List<GracePeriodDTO>
    fun get(id: Int, date: LocalDate): Optional<GracePeriodDTO>
}