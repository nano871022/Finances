package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.outbounds.IPeriodGracePort
import co.japl.finances.core.usercases.interfaces.credit.IPeriodGrace
import javax.inject.Inject

class PeriodGraceImpl @Inject constructor(private val periodGraceSvc: IPeriodGracePort): IPeriodGrace {
    override fun add(dto: GracePeriodDTO): Boolean {
        return periodGraceSvc.add(dto)
    }

    override fun delete(codeCredit: Int): Boolean {
        return periodGraceSvc.delete(codeCredit)
    }

    override fun hasGracePeriod(codeCredit: Int): Boolean {
        return periodGraceSvc.hasGracePeriod(codeCredit)
    }

}