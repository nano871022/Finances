package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal
import java.time.LocalDate

interface IInput  {

    fun getTotalInputs(cutoff:LocalDate): BigDecimal?

}