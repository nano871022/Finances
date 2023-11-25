package co.japl.finances.core.adapters.inbound.implement.recap

import co.japl.finances.core.adapters.inbound.interfaces.recap.ICreditFixPort
import co.japl.finances.core.usercases.interfaces.recap.ICreditFix
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class CreditFixImpl @Inject constructor(private val userCase: ICreditFix) : ICreditFixPort {

    override fun getTotalQuote(date: LocalDate): BigDecimal {
        return userCase.getTotalQuote(date)
    }

}