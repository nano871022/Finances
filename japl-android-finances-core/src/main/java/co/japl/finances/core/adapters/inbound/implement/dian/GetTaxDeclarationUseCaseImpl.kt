package co.japl.finances.core.adapters.inbound.implement.dian

import co.com.japl.finances.iports.dtos.TaxDeclarationDTO
import co.com.japl.finances.iports.inbounds.common.GetTaxDeclarationUseCase
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.com.japl.finances.iports.outbounds.PatrimonyPersistencePort
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class GetTaxDeclarationUseCaseImpl @Inject constructor(
    private val configPort: TaxConfigurationPort,
    private val financialDataPort: ExternalFinancialDataPort,
    private val patrimonyPersistencePort: PatrimonyPersistencePort
) : GetTaxDeclarationUseCase {

    override suspend fun getTaxDeclaration(year: Int): TaxDeclarationDTO {
        val uvt = configPort.getUVTValue(year)
        val income = financialDataPort.getIncomeForYear(year)
        val ccPurchases = financialDataPort.getCreditCardPaymentsForYear(year)
        val creditPayments = financialDataPort.getCreditPaymentsForYear(year)
        val debitPayments = financialDataPort.getDebitPaymentsYTD(year)
        val totalConsumptions = ccPurchases.add(debitPayments).add(creditPayments)
        val manualPatrimony = patrimonyPersistencePort.getAssets().sumOf { it.value }

        val reasons = mutableListOf<String>()
        if (income >= configPort.getIncomeThresholdUVT().multiply(uvt)) reasons.add("Gross Income threshold exceeded")
        if (ccPurchases >= configPort.getCreditCardThresholdUVT().multiply(uvt)) reasons.add("Credit Card Purchases threshold exceeded")
        if (totalConsumptions >= configPort.getConsumptionThresholdUVT().multiply(uvt)) reasons.add("Total Consumptions threshold exceeded")
        if (manualPatrimony >= configPort.getWealthThresholdUVT().multiply(uvt)) reasons.add("Gross Patrimony threshold exceeded")

        val taxableBase = income
        val taxCOP = calculateProgressiveTax(taxableBase, uvt)

        return TaxDeclarationDTO(
            fiscalYear = year,
            grossIncome = income,
            creditCardPurchases = ccPurchases,
            totalConsumptions = totalConsumptions,
            grossPatrimony = manualPatrimony,
            taxableBase = taxableBase,
            taxValueCOP = taxCOP,
            isObligatedToFile = reasons.isNotEmpty(),
            reasons = reasons
        )
    }

    private fun calculateProgressiveTax(baseCOP: BigDecimal, uvt: BigDecimal): BigDecimal {
        val baseUVT = baseCOP.divide(uvt, 2, RoundingMode.HALF_UP)
        val brackets = configPort.getTaxBrackets()

        for (bracket in brackets) {
            if (baseUVT > bracket.minLimitUvt && (bracket.maxLimitUvt == null || baseUVT <= bracket.maxLimitUvt)) {
                val taxUVT = baseUVT.subtract(bracket.minLimitUvt)
                    .multiply(BigDecimal.valueOf(bracket.marginalRate))
                    .add(bracket.baseOffsetUvt)
                return taxUVT.multiply(uvt).setScale(0, RoundingMode.HALF_UP)
            }
        }
        return BigDecimal.ZERO
    }
}
