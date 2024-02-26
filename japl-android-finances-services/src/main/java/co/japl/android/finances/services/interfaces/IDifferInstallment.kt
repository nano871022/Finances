package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.DifferInstallmentDTO
import java.time.LocalDate

interface IDifferInstallment : SaveSvc<DifferInstallmentDTO> {

    fun get(cutOff: LocalDate):List<DifferInstallmentDTO>
}