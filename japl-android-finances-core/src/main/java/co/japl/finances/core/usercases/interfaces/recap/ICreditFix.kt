package co.japl.finances.core.usercases.interfaces.recap

import co.japl.finances.core.dto.CreditDTO
import co.japl.finances.core.dto.PeriodCreditDTO
import java.math.BigDecimal
import java.time.LocalDate

interface ICreditFix {
    fun getTotalQuote(date: LocalDate):BigDecimal
}