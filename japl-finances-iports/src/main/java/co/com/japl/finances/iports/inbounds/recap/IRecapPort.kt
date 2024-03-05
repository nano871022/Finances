package co.com.japl.finances.iports.inbounds.recap

import co.com.japl.finances.iports.dtos.RecapDTO
import java.time.LocalDate

interface IRecapPort {

    fun getTotalValues(cutOff:LocalDate):RecapDTO
}