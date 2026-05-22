package co.japl.android.finances.services.core

import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.dao.interfaces.ICreditDAO
import co.japl.android.finances.services.dao.interfaces.IInputDAO
import java.math.BigDecimal
import javax.inject.Inject

class FinancesModuleIntegrationAdapter @Inject constructor(
    private val paidDAO: IPaidDAO,
    private val boughtDAO: IQuoteCreditCardDAO,
    private val creditDAO: ICreditDAO,
    private val inputDAO: IInputDAO
) : ExternalFinancialDataPort {

    override suspend fun getIncomeYTD(year: Int): BigDecimal {
        return inputDAO.getTotalInputs()
    }

    override suspend fun getCreditCardPurchasesYTD(year: Int): BigDecimal {
        return boughtDAO.getAll().filter { it.boughtDate.year == year }.sumOf { it.valueItem }
    }

    override suspend fun getDebitPaymentsYTD(year: Int): BigDecimal {
        return paidDAO.getAll().filter { it.date.year == year }.sumOf { it.value }
    }

    override suspend fun getOutstandingDebts(year: Int): BigDecimal {
        return creditDAO.getAll().filter { it.date.year == year }.sumOf { it.value }
    }

    override suspend fun getAverageMonthlyIncomeHistorical(): BigDecimal {
        return BigDecimal.ZERO
    }

    override suspend fun getAverageMonthlyExpenseHistorical(): BigDecimal {
        return BigDecimal.ZERO
    }
}
