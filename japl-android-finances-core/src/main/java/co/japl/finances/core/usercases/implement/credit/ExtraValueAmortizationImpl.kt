package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.finances.iports.outbounds.IExtraValueAmortizationPort
import co.japl.finances.core.usercases.interfaces.credit.IExtraValueAmortization
import java.time.LocalDate
import javax.inject.Inject

class ExtraValueAmortizationImpl @Inject constructor(private val svc: IExtraValueAmortizationPort) : IExtraValueAmortization {
    override fun getByCodeCredit(id: Int): List<ExtraValueAmortizationDTO> {
        return svc.getByCode(id)
    }

    override fun save(id: Int, numQuotes: Long, value: Double): Int {
        val dto = ExtraValueAmortizationDTO(
            id=0,
            code=id,
            numQuote=numQuotes,
            value=value,
            create=LocalDate.now())
        return svc.save(dto).toInt()
    }
}