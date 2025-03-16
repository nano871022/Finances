package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.japl.finances.core.usercases.interfaces.credit.IPeriodGrace
import javax.inject.Inject

class PeriodGraceImpl @Inject constructor(private val periodGraceSvc: IPeriodGrace) : IPeriodGracePort {
    override fun add(dto: GracePeriodDTO): Boolean {
        if(dto.id > 0){
            return false
        }
        if(dto.codeCredit <= 0){
            return false
        }
        if(dto.periods <= 0){
            return false
        }
        if(dto.create.isAfter(dto.end)){
            return false
        }
        return periodGraceSvc.add(dto)
    }

    override fun delete(codeCredit: Int): Boolean {
        if(codeCredit <= 0){
            return false
        }
        return periodGraceSvc.delete(codeCredit)
    }

    override fun hasGracePeriod(codeCredit: Int): Boolean {
        if(codeCredit <= 0){
            return false
        }
        return periodGraceSvc.hasGracePeriod(codeCredit)
    }
}