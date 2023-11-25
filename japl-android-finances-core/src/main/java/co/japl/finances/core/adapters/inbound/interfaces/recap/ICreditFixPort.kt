package co.japl.finances.core.adapters.inbound.interfaces.recap

import co.japl.finances.core.dto.CreditDTO
import co.japl.finances.core.dto.PeriodCreditDTO
import java.math.BigDecimal
import java.time.LocalDate

interface ICreditFixPort {

    fun getTotalQuote(date: LocalDate):BigDecimal
}