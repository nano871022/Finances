package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal

interface IPaid {
    fun getTotalPaid(): BigDecimal
}