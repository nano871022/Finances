package co.com.japl.module.credit.model

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.dtos.CreditDTO
import java.math.BigDecimal

data class CreditAmortizationState(
    val credit:CreditDTO? = null,
    val additional:BigDecimal? = null,
    val gracePeriod:Short? = null,
    val quotesPaid:Int? = null,
    val amortization:List<AmortizationRowDTO>? = null,
    val isLoading:Boolean = false
)
