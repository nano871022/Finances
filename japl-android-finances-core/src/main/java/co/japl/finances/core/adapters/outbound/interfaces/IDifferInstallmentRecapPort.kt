package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.DifferInstallmentDTO
import java.time.LocalDate

interface IDifferInstallmentRecapPort {

    fun get(cutOff: LocalDate):List<DifferInstallmentDTO>

    fun get(id:Int):DifferInstallmentDTO?

}