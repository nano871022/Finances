package co.japl.finances.core.usercases.implement.dian

import co.com.japl.finances.iports.dtos.dian.Form210DTO
import co.com.japl.finances.iports.inbounds.dian.IGetTaxDeclarationUseCase
import co.com.japl.finances.iports.dtos.FinancialItemDTO
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import javax.inject.Inject

class GetTaxDeclarationUseCaseImpl @Inject constructor(
    private val configPort: TaxConfigurationPort,
    private val financialDataPort: ExternalFinancialDataPort
) : IGetTaxDeclarationUseCase {

    override suspend fun getTaxDeclaration(year: Int): Form210DTO {
        val uvt = configPort.getUVTValue(year)
        val cutOffDate = LocalDate.of(year, 12, 31)

        val assets = financialDataPort.getAssetsAt(cutOffDate)
        val liabilities = financialDataPort.getLiabilitiesAt(cutOffDate)

        val grossPatrimony = assets.sumOf { it.value }
        val debts = liabilities.sumOf { it.value }
        val netPatrimony = grossPatrimony.subtract(debts).coerceAtLeast(BigDecimal.ZERO)

        val incomeDetails = financialDataPort.getIncomeDetails(year)
        val deductionDetails = financialDataPort.getDeductionDetails(year)
        val withholdingDetails = financialDataPort.getWithholdingDetails(year)

        val grossIncome = incomeDetails.sumOf { it.value }

        val nonConstitutiveIncome = deductionDetails.filter { it.type == "HEALTH_MANDATORY" || it.type == "PENSION_MANDATORY" }
            .sumOf { it.value }

        val netIncomeLine35 = grossIncome.subtract(nonConstitutiveIncome)

        val dependentsDeduction = grossIncome.multiply(BigDecimal("0.10"))
            .coerceAtMost(BigDecimal("384").multiply(uvt))

        val prepaidHealth = deductionDetails.filter { it.type == "PREPAID_HEALTH" }.sumOf { it.value }
            .coerceAtMost(BigDecimal("192").multiply(uvt))

        val mortgageInterest = deductionDetails.filter { it.type == "MORTGAGE_INTEREST" }.sumOf { it.value }
            .coerceAtMost(BigDecimal("1200").multiply(uvt))

        val totalDeductionsLine38 = dependentsDeduction.add(prepaidHealth).add(mortgageInterest)

        val exemptIncome25Line36 = netIncomeLine35.subtract(totalDeductionsLine38).multiply(BigDecimal("0.25"))
            .coerceAtMost(BigDecimal("948").multiply(uvt))

        val totalExemptionsAndDeductions = totalDeductionsLine38.add(exemptIncome25Line36)
        val limit40Percent = netIncomeLine35.multiply(BigDecimal("0.40"))
        val limitFlat = BigDecimal("1340").multiply(uvt)

        val limitedExemptionsAndDeductionsLine40 = totalExemptionsAndDeductions
            .coerceAtMost(limit40Percent)
            .coerceAtMost(limitFlat)

        val finalNetIncomeLine41 = netIncomeLine35.subtract(limitedExemptionsAndDeductionsLine40)

        val taxableBaseLine91 = finalNetIncomeLine41
        val taxOnTaxableBaseLine117 = calculateProgressiveTax(taxableBaseLine91, uvt)

        val previousYearAdvanceLine132 = BigDecimal.ZERO
        val withholdingsLine133 = withholdingDetails.sumOf { it.value }

        val nextYearAdvanceLine134 = taxOnTaxableBaseLine117.multiply(BigDecimal("0.75")).subtract(withholdingsLine133)
            .coerceAtLeast(BigDecimal.ZERO)

        val totalBalance = taxOnTaxableBaseLine117.subtract(previousYearAdvanceLine132).subtract(withholdingsLine133).add(nextYearAdvanceLine134)

        val balanceToPayLine136 = totalBalance.coerceAtLeast(BigDecimal.ZERO)
        val balanceInFavorLine137 = totalBalance.negate().coerceAtLeast(BigDecimal.ZERO)

        val reasons = mutableListOf<String>()
        if (grossPatrimony >= configPort.getWealthThresholdUVT().multiply(uvt)) reasons.add("Gross Patrimony > 4,500 UVT")
        if (grossIncome >= configPort.getIncomeThresholdUVT().multiply(uvt)) reasons.add("Gross Income > 1,400 UVT")
        if (grossIncome >= configPort.getConsumptionThresholdUVT().multiply(uvt)) reasons.add("Total Consumptions > 1,400 UVT")

        return Form210DTO(
            fiscalYear = year,
            grossPatrimony = grossPatrimony,
            debts = debts,
            netPatrimony = netPatrimony,
            grossIncome = grossIncome,
            nonConstitutiveIncome = nonConstitutiveIncome,
            netIncome = netIncomeLine35,
            deductions = totalDeductionsLine38,
            exemptIncome25 = exemptIncome25Line36,
            limitedExemptionsAndDeductions = limitedExemptionsAndDeductionsLine40,
            finalNetIncome = finalNetIncomeLine41,
            taxableBase = taxableBaseLine91,
            taxOnTaxableBase = taxOnTaxableBaseLine117,
            previousYearAdvance = previousYearAdvanceLine132,
            withholdings = withholdingsLine133,
            nextYearAdvance = nextYearAdvanceLine134,
            balanceToPay = balanceToPayLine136,
            balanceInFavor = balanceInFavorLine137,
            assetDetails = assets,
            debtDetails = liabilities,
            incomeDetails = incomeDetails,
            deductionDetails = deductionDetails,
            withholdingDetails = withholdingDetails,
            isObligatedToFile = reasons.isNotEmpty(),
            obligationReasons = reasons
        )
    }

    private fun calculateProgressiveTax(baseCOP: BigDecimal, uvt: BigDecimal): BigDecimal {
        val baseUVT = baseCOP.divide(uvt, 2, RoundingMode.HALF_UP)
        val brackets = configPort.getTaxBrackets()

        for (bracket in brackets) {
            if (baseUVT > bracket.minLimitUvt && (bracket.maxLimitUvt == null || baseUVT <= bracket.maxLimitUvt)) {
                val taxUVT = baseUVT.subtract(bracket.minLimitUvt).multiply(BigDecimal.valueOf(bracket.marginalRate)).add(bracket.baseOffsetUvt)

                return taxUVT.multiply(uvt).divide(BigDecimal("1000"), 0, RoundingMode.HALF_UP).multiply(BigDecimal("1000"))
            }
        }
        return BigDecimal.ZERO
    }

    private fun List<FinancialItemDTO>.sumOf(selector: (FinancialItemDTO) -> BigDecimal): BigDecimal {
        return this.map(selector).fold(BigDecimal.ZERO) { acc, v -> acc.add(v) }
    }
}
