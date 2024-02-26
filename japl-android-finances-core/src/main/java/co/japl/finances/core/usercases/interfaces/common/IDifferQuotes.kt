package co.japl.finances.core.usercases.interfaces.common

import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import java.time.LocalDate

interface IDifferQuotes {

    fun getDifferQuote(date:LocalDate):List<DifferInstallmentDTO>
}