package co.com.japl.finances.iports.inbounds.recap

import co.com.japl.finances.iports.dtos.RecapDTO

interface IRecapPort {

    fun getTotalValues():RecapDTO
}