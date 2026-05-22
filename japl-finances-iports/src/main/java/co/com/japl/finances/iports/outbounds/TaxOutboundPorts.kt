package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PatrimonyAssetDTO
import co.com.japl.finances.iports.dtos.TaxHistoryDTO
import java.math.BigDecimal

interface TaxConfigurationPort {
    fun getUVTValue(year: Int): BigDecimal
    fun getIncomeThresholdUVT(): BigDecimal
    fun getConsumptionThresholdUVT(): BigDecimal
    fun getCreditCardThresholdUVT(): BigDecimal
    fun getWealthThresholdUVT(): BigDecimal
    fun getAlphaSmoothingFactor(): Double
    fun getTaxBrackets(): List<TaxBracketConfig>
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
