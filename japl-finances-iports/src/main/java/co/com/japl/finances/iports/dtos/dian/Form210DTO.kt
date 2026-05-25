package co.com.japl.finances.iports.dtos.dian

import java.math.BigDecimal
import co.com.japl.finances.iports.dtos.FinancialItemDTO

data class Form210DTO(
    val fiscalYear: Int,
    val grossPatrimony: BigDecimal,
    val debts: BigDecimal,
    val netPatrimony: BigDecimal,
    val grossIncome: BigDecimal,
    val nonConstitutiveIncome: BigDecimal,
    val netIncome: BigDecimal,
    val deductions: BigDecimal,
    val exemptIncome25: BigDecimal,
    val limitedExemptionsAndDeductions: BigDecimal,
    val finalNetIncome: BigDecimal,
    val taxableBase: BigDecimal,
    val taxOnTaxableBase: BigDecimal,
    val previousYearAdvance: BigDecimal,
    val withholdings: BigDecimal,
    val nextYearAdvance: BigDecimal,
    val balanceToPay: BigDecimal,
    val balanceInFavor: BigDecimal,
    val assetDetails: List<FinancialItemDTO>,
    val debtDetails: List<FinancialItemDTO>,
    val incomeDetails: List<FinancialItemDTO>,
    val deductionDetails: List<FinancialItemDTO>,
    val withholdingDetails: List<FinancialItemDTO>,
    val isObligatedToFile: Boolean,
    val obligationReasons: List<String>,
    val taxLiqOrd: BigDecimal,
    val taxExpImp: BigDecimal,
    val nonConstitutiveCapital: BigDecimal,
    val taxLiqCapital: BigDecimal,
    val grossCapital: BigDecimal,
    val taxLiqGeneral:BigDecimal,
    val taxLiqCedGen:BigDecimal
)
