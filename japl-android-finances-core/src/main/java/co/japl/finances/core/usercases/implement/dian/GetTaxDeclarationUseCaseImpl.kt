package co.japl.finances.core.usercases.implement.dian

import android.content.Context
import android.util.Log
import co.com.japl.finances.iports.dtos.dian.Form210DTO
import co.com.japl.finances.iports.inbounds.dian.IGetTaxDeclarationUseCase
import co.com.japl.finances.iports.dtos.FinancialItemDTO
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import co.com.japl.finances.iports.enums.dian.FinancialItemType
import co.japl.finances.core.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import javax.inject.Inject

class GetTaxDeclarationUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
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

        val nonConstitutiveIncome = deductionDetails.filter { it.type == FinancialItemType.HEALTH_MANDATORY.value || it.type == FinancialItemType.PENSION_MANDATORY.value }
            .sumOf { it.value }

        val netIncomeLine34 = grossIncome.subtract(nonConstitutiveIncome)

        val dependentsDeduction = grossIncome.multiply(configPort.getDependentsDeductionRate())
            .coerceAtMost(configPort.getDependentsDeductionMaxUVT().multiply(uvt))

        val prepaidHealth = deductionDetails.filter { it.type == FinancialItemType.PREPAID_HEALTH.value }.sumOf { it.value }
            .coerceAtMost(configPort.getPrepaidHealthMaxUVT().multiply(uvt))

        val mortgageInterest = deductionDetails.filter { it.type == FinancialItemType.MORTGAGE_INTEREST.value }.sumOf { it.value }
            .coerceAtMost(configPort.getMortgageInterestMaxUVT().multiply(uvt))

        val totalDeductionsLine38 = dependentsDeduction.add(prepaidHealth).add(mortgageInterest)

        val exemptIncome25Line36 = netIncomeLine34.subtract(totalDeductionsLine38)
            .multiply(configPort.getExemptIncomeRate())
            .coerceAtMost(configPort.getExemptIncomeMaxUVT().multiply(uvt))

        val totalExemptionsAndDeductions = totalDeductionsLine38.add(exemptIncome25Line36)
        val limit40Percent = netIncomeLine34.multiply(configPort.getLimit40PercentRate())
        val limitFlat = configPort.getLimitFlatUVT().multiply(uvt)

        val limitedExemptionsAndDeductionsLine41 = totalExemptionsAndDeductions
            .coerceAtMost(limit40Percent)
            .coerceAtMost(limitFlat)

        val finalNetIncomeLine42 = netIncomeLine34.subtract(limitedExemptionsAndDeductionsLine41)

        val taxableBaseLine91 = finalNetIncomeLine42
        val taxOnTaxableBaseLine117 = calculateProgressiveTax(taxableBaseLine91, uvt)
        Log.d(this.javaClass.name,"Calc: $taxOnTaxableBaseLine117 = $taxableBaseLine91 / $uvt")

        val previousYearAdvanceLine132 = BigDecimal.ZERO
        val withholdingsLine133 = withholdingDetails.sumOf { it.value }

        val nextYearAdvanceLine134 = taxOnTaxableBaseLine117.multiply(configPort.getNextYearAdvanceRate())
            .subtract(withholdingsLine133)
            .coerceAtLeast(BigDecimal.ZERO)

        val totalBalance = taxOnTaxableBaseLine117.subtract(previousYearAdvanceLine132).subtract(withholdingsLine133).add(nextYearAdvanceLine134)

        val balanceToPayLine136 = totalBalance.coerceAtLeast(BigDecimal.ZERO)
        val balanceInFavorLine137 = totalBalance.negate().coerceAtLeast(BigDecimal.ZERO)

        val reasons = mutableListOf<String>()

        if (grossPatrimony >= configPort.getWealthThresholdUVT().multiply(uvt)) reasons.add("${context.getString(R.string.gross_patrimony)} > ${configPort.getPatrimonyThresholdUVT()} UVT")
        if (grossIncome >= configPort.getIncomeThresholdUVT().multiply(uvt)) reasons.add("${context.getString(R.string.gross_income)} > ${configPort.getIncomeThresholdUVT()} UVT")
        if (grossIncome >= configPort.getConsumptionThresholdUVT().multiply(uvt)) reasons.add("${context.getString(R.string.total_consuptions)} > ${configPort.getConsumptionThresholdUVT()} UVT")

        val nonConstitutiveCapital= BigDecimal.ZERO
        val taxLiqCapital=BigDecimal.ZERO
        val grossCapital= BigDecimal.ZERO

        return Form210DTO(
            fiscalYear = year,
            grossPatrimony = grossPatrimony,
            debts = debts,
            netPatrimony = netPatrimony,
            grossIncome = grossIncome,
            nonConstitutiveIncome = nonConstitutiveIncome,
            netIncome = netIncomeLine34,
            deductions = totalDeductionsLine38,
            exemptIncome25 = exemptIncome25Line36,
            limitedExemptionsAndDeductions = limitedExemptionsAndDeductionsLine41,

            finalNetIncome = finalNetIncomeLine42,

            taxableBase = taxableBaseLine91,
            assetDetails = assets,
            debtDetails = liabilities,
            incomeDetails = incomeDetails,
            deductionDetails = deductionDetails,
            withholdingDetails = withholdingDetails,
            isObligatedToFile = reasons.isNotEmpty(),
            obligationReasons = reasons,

            taxOnTaxableBase = taxOnTaxableBaseLine117,
            previousYearAdvance = previousYearAdvanceLine132,
            withholdings = withholdingsLine133,
            nextYearAdvance = nextYearAdvanceLine134,
            balanceToPay = balanceToPayLine136,
            balanceInFavor = balanceInFavorLine137,
            taxLiqOrd = netIncomeLine34.subtract(limitedExemptionsAndDeductionsLine41),
            taxExpImp=finalNetIncomeLine42,
            nonConstitutiveCapital= nonConstitutiveCapital,
            taxLiqCapital=taxLiqCapital,
            grossCapital= grossCapital,
            taxLiqGeneral=netIncomeLine34.add(taxLiqCapital),
            taxLiqCedGen=netIncomeLine34.add(taxLiqCapital).subtract(limitedExemptionsAndDeductionsLine41)

            )
    }

    private fun calculateProgressiveTax(baseCOP: BigDecimal, uvt: BigDecimal): BigDecimal {
        val baseUVT = baseCOP.divide(uvt, 2, RoundingMode.HALF_UP)
        val brackets = configPort.getTaxBrackets()
        val roundingFactor = configPort.getRoundingFactor()

        for (bracket in brackets) {
            if (baseUVT > bracket.minLimitUvt && (bracket.maxLimitUvt == null || baseUVT <= bracket.maxLimitUvt)) {
                val taxUVT = baseUVT.subtract(bracket.minLimitUvt)
                                    .multiply(BigDecimal.valueOf(bracket.marginalRate))
                                    .add(bracket.baseOffsetUvt)

                return taxUVT.multiply(uvt).divide(roundingFactor, 0, RoundingMode.HALF_UP).multiply(roundingFactor).also{
                    Log.d(javaClass.name,"BaseCop: $baseCOP UvtBase: $baseUVT UvtTax: $taxUVT X $uvt / $roundingFactor X $roundingFactor ")
                }
            }
        }
        return BigDecimal.ZERO
    }

    private fun List<FinancialItemDTO>.sumOf(selector: (FinancialItemDTO) -> BigDecimal): BigDecimal {
        return this.map(selector).fold(BigDecimal.ZERO) { acc, v -> acc.add(v) }
    }
}
