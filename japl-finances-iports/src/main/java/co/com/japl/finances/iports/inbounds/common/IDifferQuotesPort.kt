package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import java.time.LocalDate

interface IDifferQuotesPort {
    fun getDifferQuote(cutOff: LocalDate):List<DifferInstallmentDTO>

}