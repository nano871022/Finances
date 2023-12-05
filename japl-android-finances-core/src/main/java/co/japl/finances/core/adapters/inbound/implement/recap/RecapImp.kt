package co.japl.finances.core.adapters.inbound.implement.recap

import co.com.japl.finances.iports.dtos.RecapDTO
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.japl.finances.core.usercases.interfaces.recap.IRecap
import javax.inject.Inject

class RecapImp @Inject constructor(private val recapSvc:IRecap):IRecapPort{
    override fun getTotalValues(): RecapDTO {
        return recapSvc.getTotalValues()
    }

}