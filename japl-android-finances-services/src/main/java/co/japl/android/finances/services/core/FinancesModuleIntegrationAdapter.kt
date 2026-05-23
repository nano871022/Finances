package co.japl.android.finances.services.core

import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.dao.interfaces.ICreditDAO
import co.japl.android.finances.services.dao.interfaces.IInputDAO
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
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

    override suspend fun getIncomeForYear(year: Int): BigDecimal {
        val inputs = inputDAO.getAll()
        var total = BigDecimal.ZERO
        inputs.forEach { input ->
            val startOfYear = LocalDate.of(year, 1, 1)
            val endOfYear = LocalDate.of(year, 12, 31)
            val start = if (input.dateStart.isBefore(startOfYear)) startOfYear else input.dateStart
            val end = if (input.dateEnd.isAfter(endOfYear)) endOfYear else input.dateEnd

            if (start.isBefore(end) || start.isEqual(end)) {
                val months = countOccurrences(start, end, input.kindOf)
                total = total.add(input.value.multiply(BigDecimal.valueOf(months.toLong())))
            }
        }
        return total
    }

    override suspend fun getProjectedIncome(year: Int): BigDecimal {
        val inputs = inputDAO.getAll().filter { it.dateEnd.isAfter(LocalDate.now()) }
        var total = BigDecimal.ZERO
        inputs.forEach { input ->
            val factor = when (input.kindOf.lowercase()) {
                "mensual" -> 12
                "bi-mensual" -> 6
                "trimestral" -> 4
                "cuatrimestral" -> 3
                "semestral" -> 2
                "anual" -> 1
                else -> 0
            }
            total = total.add(input.value.multiply(BigDecimal.valueOf(factor.toLong())))
        }
        return total
    }

    override suspend fun getCreditCardPaymentsForYear(year: Int): BigDecimal {
        val boughts = boughtDAO.getAll()
        var total = BigDecimal.ZERO
        boughts.forEach { bought ->
            val startOfYear = LocalDate.of(year, 1, 1)
            val endOfYear = LocalDate.of(year, 12, 31)
            val start = if (bought.boughtDate.toLocalDate().isBefore(startOfYear)) startOfYear else bought.boughtDate.toLocalDate()
            val end = if (bought.endDate.toLocalDate().isAfter(endOfYear)) endOfYear else bought.endDate.toLocalDate()

            if (start.isBefore(end) || start.isEqual(end)) {
                val months = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), end.withDayOfMonth(1)) + 1
                val quoteValue = bought.valueItem.divide(BigDecimal.valueOf(bought.month.toLong()), 2, java.math.RoundingMode.HALF_UP)
                total = total.add(quoteValue.multiply(BigDecimal.valueOf(months)))
            }
        }
        return total
    }

    override suspend fun getCreditPaymentsForYear(year: Int): BigDecimal {
        val credits = creditDAO.getAll()
        var total = BigDecimal.ZERO
        credits.forEach { credit ->
            val startOfYear = LocalDate.of(year, 1, 1)
            val endOfYear = LocalDate.of(year, 12, 31)
            val start = if (credit.date.isBefore(startOfYear)) startOfYear else credit.date
            val endDate = credit.date.plusMonths(credit.periods.toLong())
            val end = if (endDate.isAfter(endOfYear)) endOfYear else endDate

            if (start.isBefore(end) || start.isEqual(end)) {
                val months = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), end.withDayOfMonth(1)) + 1
                total = total.add(credit.quoteValue.multiply(BigDecimal.valueOf(months)))
            }
        }
        return total
    }

    private fun countOccurrences(start: LocalDate, end: LocalDate, periodicity: String): Int {
        val step = when (periodicity.lowercase()) {
            "mensual" -> 1
            "bi-mensual" -> 2
            "trimestral" -> 3
            "cuatrimestral" -> 4
            "semestral" -> 6
            "anual" -> 12
            else -> return 0
        }
        var count = 0
        var current = start
        while (current.isBefore(end) || current.isEqual(end)) {
            count++
            current = current.plusMonths(step.toLong())
        }
        return count
    }
}
