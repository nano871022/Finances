package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort
import co.japl.finances.core.usercases.interfaces.credit.IExtraValueAmortization
import javax.inject.Inject

class ExtraValueAmortizationCreditImpl @Inject constructor(private val svc: IExtraValueAmortization) :IExtraValueAmortizationCreditPort  {
    override fun getAll(id: Int): List<ExtraValueAmortizationDTO> {
        require(id > 0){"Id must be greater than 0"}
        return svc.getByCodeCredit(id)
    }

    override fun save(id: Int, numQuotes: Long, value: Double): Int {
        require(id > 0){"Id must be greatest than 0"}
        require(numQuotes > 0){"Number of quotes must be greater than 0"}
        require(value > 0.0){"Value must be greater than 0"}
        return svc.save(id,numQuotes,value)
    }
}