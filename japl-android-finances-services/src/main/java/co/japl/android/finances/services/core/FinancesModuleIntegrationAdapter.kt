package co.japl.android.finances.services.core

import android.util.Log
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.com.japl.finances.iports.dtos.FinancialItemDTO
import co.com.japl.finances.iports.enums.dian.FinancialItemType
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.dao.interfaces.ICreditDAO
import co.japl.android.finances.services.dao.interfaces.IInputDAO
import co.japl.android.finances.services.dao.interfaces.IPatrimonyDAO
import co.japl.android.finances.services.dao.interfaces.IAccountDAO
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class FinancesModuleIntegrationAdapter @Inject constructor(
    private val paidDAO: IPaidDAO,
    private val boughtDAO: IQuoteCreditCardDAO,
    private val creditDAO: ICreditDAO,
    private val inputDAO: IInputDAO,
    private val creditCardSvc: ICreditCardSvc,
    private val patrimonyDAO: IPatrimonyDAO,
    private val accountDAO: IAccountDAO,
    private val configPort: TaxConfigurationPort
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

    override suspend fun getCreditDebt(date: LocalDate): BigDecimal {
        return creditDAO.getPendingToPayAll(date)
    }

    override suspend fun getCreditCardDebt(date: LocalDate): BigDecimal {
        val creditCards = creditCardSvc.getAll().filter { it.status }
        var total = BigDecimal.ZERO
        creditCards.forEach { creditCard ->
            val endDate = DateUtils.cutOffLastMonth(creditCard.cutOffDay.toShort(), LocalDateTime.of(date, java.time.LocalTime.MAX))
            val startDate = DateUtils.startDateFromCutoff(creditCard.cutOffDay.toShort(), endDate)
            val pendingToPay = boughtDAO.getPendingToPay(creditCard.id, startDate, endDate).orElse(BigDecimal.ZERO)
            val pendingToPayQuotes = boughtDAO.getPendingToPayQuotes(creditCard.id, startDate, endDate).orElse(BigDecimal.ZERO)
            total = total.add(pendingToPay).add(pendingToPayQuotes)
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

    override suspend fun getIncomeDetails(year: Int): List<FinancialItemDTO> {
        val inputs = inputDAO.getAll()
        val details = mutableListOf<FinancialItemDTO>()
        inputs.forEach { input ->
            val startOfYear = LocalDate.of(year, 1, 1)
            val endOfYear = LocalDate.of(year, 12, 31)
            val start = if (input.date.isBefore(startOfYear)) startOfYear else input.date
            val end = if (input.dateEnd.isAfter(endOfYear)) endOfYear else input.dateEnd

            if (start.isBefore(end) || start.isEqual(end)) {
                val months = countOccurrences(start, end, input.kindOf)
                if (months > 0) {
                    details.add(FinancialItemDTO(
                        name = input.name,
                        value = input.value.multiply(BigDecimal.valueOf(months.toLong())),
                        date = start,
                        type = FinancialItemType.INCOME.value
                    ))
                }
            }
        }
        return details
    }

    override suspend fun getDeductionDetails(year: Int): List<FinancialItemDTO> {
        val paids = paidDAO.getAll()
        val details = mutableListOf<FinancialItemDTO>()
        val healthKw = configPort.getHealthKeyword()
        val pensionKw = configPort.getPensionKeyword()
        val prepaidKw = configPort.getPrepaidKeyword()
        val mortgageKw = configPort.getMortgageKeyword()

        paids.forEach { paid ->
             if (paid.date.year == year) {
                 val type = when {
                     paid.name.contains(healthKw, true) -> FinancialItemType.HEALTH_MANDATORY.value
                     paid.name.contains(pensionKw, true) -> FinancialItemType.PENSION_MANDATORY.value
                     paid.name.contains(prepaidKw, true) -> FinancialItemType.PREPAID_HEALTH.value
                     paid.name.contains(mortgageKw, true) -> FinancialItemType.MORTGAGE_INTEREST.value
                     else -> FinancialItemType.DEDUCTION.value
                 }
                 details.add(FinancialItemDTO(
                     name = paid.name,
                     value = paid.value,
                     date = paid.date,
                     type = type
                 ))
             }
        }
        return details
    }

    override suspend fun getWithholdingDetails(year: Int): List<FinancialItemDTO> {
        val withholdingKw = configPort.getWithholdingKeyword()
        return paidDAO.getAll().filter { it.date.year == year && it.name.contains(withholdingKw, ignoreCase = true) }
            .map { FinancialItemDTO(it.name, it.value, it.date, FinancialItemType.WITHHOLDING.value) }
    }

    override suspend fun getAssetsAt(date: LocalDate): List<FinancialItemDTO> {
        val assets = mutableListOf<FinancialItemDTO>()
        accountDAO.getAll().forEach { account ->
             assets.add(FinancialItemDTO(account.name, BigDecimal.ZERO, date, FinancialItemType.ACCOUNT.value))
        }
        patrimonyDAO.getAll().forEach { asset ->
             assets.add(FinancialItemDTO(asset.name, asset.value, date, asset.type))
        }
        return assets
    }

    override suspend fun getLiabilitiesAt(date: LocalDate): List<FinancialItemDTO> {
        val liabilities = mutableListOf<FinancialItemDTO>()
        creditDAO.getAll().forEach { credit ->
            val pending = creditDAO.getPendingToPayAll(date)
            if (pending > BigDecimal.ZERO) {
                liabilities.add(FinancialItemDTO("Credit: ${credit.name}", pending, date, FinancialItemType.CREDIT.value))
            }
        }
        val ccDebt = getCreditCardDebt(date)
        if (ccDebt > BigDecimal.ZERO) {
            liabilities.add(FinancialItemDTO("Credit Cards Total", ccDebt, date, FinancialItemType.CREDIT_CARD.value))
        }
        return liabilities
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
        Log.d(javaClass.name,"Current: $current End: $end Cnt: $count Step: $step")
        while (current.isBefore(end) || current.isEqual(end)) {
            count++
            current = current.plusMonths(step.toLong())
        }
        Log.d(javaClass.name,"Current: $current End: $end Cnt: $count Step: $step")
        return count
    }
}
