package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import java.time.YearMonth
import javax.inject.Inject

class CreditImpl @Inject constructor(private val creditSvc:ICredit):ICreditPort {
    override fun getCreditEnable(period: YearMonth): List<CreditDTO> {
        if(period > YearMonth.now()){
            return emptyList()
        }
        return creditSvc.getAllEnable(period)
    }

    override  fun delete(id:Int):Boolean{
        if(id <= 0){
            return false
        }
        return creditSvc.delete(id)
    }

}