package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.RecapCreditDTO
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

    override fun getCreditsEnables(period:YearMonth): List<RecapCreditDTO> {
        if(period > YearMonth.now()){
            return emptyList()
        }
        return creditSvc.getCreditsEnables(period)
    }

    override fun getCredit(code: Int): CreditDTO? {
        require(code > 0){"Code is not valid, it should be more than Zero"}
        return creditSvc.findCreditById(code)
    }

}