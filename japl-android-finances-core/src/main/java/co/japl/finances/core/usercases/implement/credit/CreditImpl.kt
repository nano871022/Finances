package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import java.time.YearMonth
import javax.inject.Inject

class CreditImpl @Inject constructor(private val creditSvc:ICreditPort): ICredit {
    override fun getAllEnable(period:YearMonth): List<CreditDTO> {
        return creditSvc.getAllActive(period)
    }

    override fun delete(id: Int): Boolean {
        return creditSvc.delete(id)
    }

}