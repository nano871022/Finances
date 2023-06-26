package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.GracePeriodDTO
import java.time.LocalDate
import java.util.Optional

interface IGracePeriod : SaveSvc<GracePeriodDTO> {
    fun get(codCredit:Long): List<GracePeriodDTO>
    fun get(id: Int, date: LocalDate): Optional<GracePeriodDTO>
}