package co.com.japl.module.credit.model

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.dtos.CreditDTO
import java.math.BigDecimal

data class CreditAmortizationState(
    var credit:CreditDTO? = null,
    var additional:BigDecimal? = null,
    var gracePeriod:Short? = null,
    var quotesPaid:Int? = null,
    var amortization:List<AmortizationRowDTO>? = null,
    var isLoading:Boolean = false
)
