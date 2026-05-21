package com.nano871022.finances.iport.ports.outbound

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
    suspend fun saveHistory(history: com.nano871022.finances.iport.dto.TaxHistoryDTO)
    suspend fun getHistory(): List<com.nano871022.finances.iport.dto.TaxHistoryDTO>
}

interface PatrimonyPersistencePort {
    suspend fun saveAsset(asset: com.nano871022.finances.iport.dto.PatrimonyAssetDTO)
    suspend fun getAssets(): List<com.nano871022.finances.iport.dto.PatrimonyAssetDTO>
    suspend fun deleteAsset(id: Long)
}
