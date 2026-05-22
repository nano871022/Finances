package co.japl.android.finances.services.core

import com.nano871022.finances.iport.ports.outbound.ExternalFinancialDataPort
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.dao.interfaces.ICreditDAO
import java.math.BigDecimal
import javax.inject.Inject

class FinancesModuleIntegrationAdapter @Inject constructor(
    private val paidDAO: IPaidDAO,
    private val boughtDAO: IQuoteCreditCardDAO,
    private val creditDAO: ICreditDAO
) : ExternalFinancialDataPort {

    override suspend fun getIncomeYTD(year: Int): BigDecimal {
        return BigDecimal.ZERO
    }

    override suspend fun getCreditCardPurchasesYTD(year: Int): BigDecimal {
        return boughtDAO.getAll().filter { it.boughtDate.year == year }.map { it.valueItem }.fold(BigDecimal.ZERO, BigDecimal::add)
    }

    override suspend fun getDebitPaymentsYTD(year: Int): BigDecimal {
        return paidDAO.getAll().filter { it.date.year == year }.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)
    }

    override suspend fun getOutstandingDebts(year: Int): BigDecimal {
        return creditDAO.getAll().filter { it.date.year == year }.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)
    }

    override suspend fun getAverageMonthlyIncomeHistorical(): BigDecimal {
        return BigDecimal.ZERO
    }

    override suspend fun getAverageMonthlyExpenseHistorical(): BigDecimal {
        return BigDecimal.ZERO
    }
}
