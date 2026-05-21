package com.nano871022.finances.service.adapter.outbound.integration

import com.nano871022.finances.iport.ports.outbound.ExternalFinancialDataPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import java.math.BigDecimal
import javax.inject.Inject

class FinancesModuleIntegrationAdapter @Inject constructor(
    private val paidPort: IPaidPort,
    private val boughtPort: IBoughtPort,
    private val creditPort: ICreditPort
) : ExternalFinancialDataPort {

    override suspend fun getIncomeYTD(year: Int): BigDecimal {
        return BigDecimal.ZERO
    }

    override suspend fun getCreditCardPurchasesYTD(year: Int): BigDecimal {
        return boughtPort.getAll().filter { it.date.year == year }.sumOf { it.boughtValue }
    }

    override suspend fun getDebitPaymentsYTD(year: Int): BigDecimal {
        return paidPort.getAll().filter { it.date.year == year }.sumOf { it.amount }
    }

    override suspend fun getOutstandingDebts(year: Int): BigDecimal {
        return creditPort.getAll().filter { it.date.year == year }.sumOf { it.value }
    }

    override suspend fun getAverageMonthlyIncomeHistorical(): BigDecimal {
        return BigDecimal.ZERO
    }

    override suspend fun getAverageMonthlyExpenseHistorical(): BigDecimal {
        return BigDecimal.ZERO
    }
}
