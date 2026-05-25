package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PatrimonyAssetDTO
import co.com.japl.finances.iports.dtos.TaxHistoryDTO
import java.math.BigDecimal
import java.time.LocalDate

interface TaxConfigurationPort {
    fun getUVTValue(year: Int): BigDecimal
    fun getIncomeThresholdUVT(): BigDecimal
    fun getPatrimonyThresholdUVT(): BigDecimal
    fun getConsumptionThresholdUVT(): BigDecimal
    fun getCreditCardThresholdUVT(): BigDecimal
    fun getWealthThresholdUVT(): BigDecimal
    fun getAlphaSmoothingFactor(): Double
    fun getTaxBrackets(): List<TaxBracketConfig>
    fun getDependentsDeductionMaxUVT(): BigDecimal
    fun getDependentsDeductionRate(): BigDecimal
    fun getPrepaidHealthMaxUVT(): BigDecimal
    fun getMortgageInterestMaxUVT(): BigDecimal
    fun getExemptIncomeRate(): BigDecimal
    fun getExemptIncomeMaxUVT(): BigDecimal
    fun getLimit40PercentRate(): BigDecimal
    fun getLimitFlatUVT(): BigDecimal
    fun getNextYearAdvanceRate(): BigDecimal
    fun getRoundingFactor(): BigDecimal
    fun getWithholdingKeyword(): String
    fun getHealthKeyword(): String
    fun getPensionKeyword(): String
    fun getPrepaidKeyword(): String
    fun getMortgageKeyword(): String
}

data class TaxBracketConfig(
    val minLimitUvt: BigDecimal,
    val maxLimitUvt: BigDecimal?,
    val marginalRate: Double,
    val baseOffsetUvt: BigDecimal
)

interface ExternalFinancialDataPort {
    suspend fun getIncomeYTD(year: Int): BigDecimal
    suspend fun getCreditCardPurchasesYTD(year: Int): BigDecimal
    suspend fun getDebitPaymentsYTD(year: Int): BigDecimal
    suspend fun getOutstandingDebts(year: Int): BigDecimal
    suspend fun getAverageMonthlyIncomeHistorical(): BigDecimal
    suspend fun getAverageMonthlyExpenseHistorical(): BigDecimal

    suspend fun getIncomeForYear(year: Int): BigDecimal
    suspend fun getProjectedIncome(year: Int): BigDecimal
    suspend fun getCreditCardPaymentsForYear(year: Int): BigDecimal
    suspend fun getCreditPaymentsForYear(year: Int): BigDecimal
    suspend fun getCreditDebt(date: LocalDate): BigDecimal
    suspend fun getCreditCardDebt(date: LocalDate): BigDecimal

    suspend fun getIncomeDetails(year: Int): List<co.com.japl.finances.iports.dtos.FinancialItemDTO>
    suspend fun getDeductionDetails(year: Int): List<co.com.japl.finances.iports.dtos.FinancialItemDTO>
    suspend fun getWithholdingDetails(year: Int): List<co.com.japl.finances.iports.dtos.FinancialItemDTO>
    suspend fun getAssetsAt(date: LocalDate): List<co.com.japl.finances.iports.dtos.FinancialItemDTO>
    suspend fun getLiabilitiesAt(date: LocalDate): List<co.com.japl.finances.iports.dtos.FinancialItemDTO>
}

interface TaxHistoryPersistencePort {
    suspend fun saveHistory(history: TaxHistoryDTO)
    suspend fun getHistory(): List<TaxHistoryDTO>
}

interface PatrimonyPersistencePort {
    suspend fun saveAsset(asset: PatrimonyAssetDTO)
    suspend fun getAssets(): List<PatrimonyAssetDTO>
    suspend fun deleteAsset(id: Long)
}
