package co.japl.finances.core.usercases.interfaces.recap

import co.com.japl.finances.iports.dtos.RecapDTO
import java.time.LocalDate

interface IRecap {
    fun getTotalValues(cutOff:LocalDate,cache:Boolean): RecapDTO
}