package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.japl.finances.core.usercases.interfaces.common.IDifferQuotes
import java.time.LocalDate
import javax.inject.Inject

class DifferQuoteImpl @Inject constructor(private val differQuoteSvc:IDifferInstallmentRecapPort): IDifferQuotes{
    override fun getDifferQuote(date: LocalDate): List<DifferInstallmentDTO> {
        return differQuoteSvc.get(date)
    }

}