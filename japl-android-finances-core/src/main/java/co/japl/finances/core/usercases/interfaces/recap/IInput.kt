package co.japl.finances.core.usercases.interfaces.recap

import java.math.BigDecimal

interface IInput  {

    fun getTotalInputs(): BigDecimal?

}