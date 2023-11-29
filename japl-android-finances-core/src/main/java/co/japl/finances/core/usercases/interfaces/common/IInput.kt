package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal

interface IInput  {

    fun getTotalInputs(): BigDecimal?

}