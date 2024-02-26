package co.japl.finances.core.usercases.interfaces.recap

import co.com.japl.finances.iports.dtos.RecapDTO

interface IRecap {
    fun getTotalValues(): RecapDTO
}