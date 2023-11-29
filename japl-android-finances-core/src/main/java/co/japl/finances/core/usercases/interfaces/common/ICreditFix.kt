package co.japl.finances.core.usercases.interfaces.common

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import java.math.BigDecimal
import java.time.LocalDate

interface ICreditFix {
    fun getTotalQuote(date: LocalDate):BigDecimal
}