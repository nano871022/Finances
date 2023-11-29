package co.japl.finances.core.adapters.inbound.implement.common

import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.japl.finances.core.usercases.interfaces.common.IDifferQuotes
import java.time.LocalDate
import javax.inject.Inject

class DifferQuotesImpl @Inject constructor(private val differQuoteSvc:IDifferQuotes):IDifferQuotesPort {
    override fun getDifferQuote(cutOff: LocalDate): List<DifferInstallmentDTO> {
        return differQuoteSvc.getDifferQuote(cutOff)
    }

}