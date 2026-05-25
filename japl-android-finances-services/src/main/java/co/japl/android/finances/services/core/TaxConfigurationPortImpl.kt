package co.japl.android.finances.services.core

import android.content.Context
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import co.com.japl.finances.iports.outbounds.TaxBracketConfig
import java.math.BigDecimal
import java.util.Properties
import javax.inject.Inject

class TaxConfigurationPortImpl @Inject constructor(
    private val context: Context
) : TaxConfigurationPort {

    private val properties = Properties().apply {
        context.assets.open("dian_config.properties").use { load(it) }
    }

    override fun getUVTValue(year: Int): BigDecimal =
        properties.getProperty("uvt.value.$year")?.toBigDecimal() ?: BigDecimal("47065.0")

    override fun getIncomeThresholdUVT(): BigDecimal =
        properties.getProperty("threshold.income.uvt").toBigDecimal()

    override fun getPatrimonyThresholdUVT(): BigDecimal =
        properties.getProperty("threshold.patrimony.uvt").toBigDecimal()

    override fun getConsumptionThresholdUVT(): BigDecimal =
        properties.getProperty("threshold.consumption.uvt").toBigDecimal()

    override fun getCreditCardThresholdUVT(): BigDecimal =
        properties.getProperty("threshold.creditcard.uvt").toBigDecimal()

    override fun getWealthThresholdUVT(): BigDecimal =
        properties.getProperty("threshold.wealth.uvt").toBigDecimal()

    override fun getAlphaSmoothingFactor(): Double =
        properties.getProperty("projection.alpha").toDouble()

    override fun getTaxBrackets(): List<TaxBracketConfig> {
        val brackets = mutableListOf<TaxBracketConfig>()
        var index = 0
        while (true) {
            val value = properties.getProperty("bracket.$index") ?: break
            val parts = value.split("|")
            brackets.add(
                TaxBracketConfig(
                    minLimitUvt = parts[0].toBigDecimal(),
                    maxLimitUvt = if (parts[1] == "null") null else parts[1].toBigDecimal(),
                    marginalRate = parts[2].toDouble(),
                    baseOffsetUvt = parts[3].toBigDecimal()
                )
            )
            index++
        }
        return brackets
    }

    override fun getDependentsDeductionMaxUVT(): BigDecimal =
        properties.getProperty("deduction.dependents.max.uvt")?.toBigDecimal() ?: BigDecimal("384")

    override fun getDependentsDeductionRate(): BigDecimal =
        properties.getProperty("deduction.dependents.rate")?.toBigDecimal() ?: BigDecimal("0.10")

    override fun getPrepaidHealthMaxUVT(): BigDecimal =
        properties.getProperty("deduction.prepaid_health.max.uvt")?.toBigDecimal() ?: BigDecimal("192")

    override fun getMortgageInterestMaxUVT(): BigDecimal =
        properties.getProperty("deduction.mortgage_interest.max.uvt")?.toBigDecimal() ?: BigDecimal("1200")

    override fun getExemptIncomeRate(): BigDecimal =
        properties.getProperty("exempt.income.rate")?.toBigDecimal() ?: BigDecimal("0.25")

    override fun getExemptIncomeMaxUVT(): BigDecimal =
        properties.getProperty("exempt.income.max.uvt")?.toBigDecimal() ?: BigDecimal("948")

    override fun getLimit40PercentRate(): BigDecimal =
        properties.getProperty("limit.40percent.rate")?.toBigDecimal() ?: BigDecimal("0.40")

    override fun getLimitFlatUVT(): BigDecimal =
        properties.getProperty("limit.flat.uvt")?.toBigDecimal() ?: BigDecimal("1340")

    override fun getNextYearAdvanceRate(): BigDecimal =
        properties.getProperty("advance.next_year.rate")?.toBigDecimal() ?: BigDecimal("0.75")

    override fun getRoundingFactor(): BigDecimal =
        properties.getProperty("rounding.factor")?.toBigDecimal() ?: BigDecimal("1000")

    override fun getWithholdingKeyword(): String =
        properties.getProperty("keyword.withholding") ?: "Retención"

    override fun getHealthKeyword(): String =
        properties.getProperty("keyword.health") ?: "Salud"

    override fun getPensionKeyword(): String =
        properties.getProperty("keyword.pension") ?: "Pensión"

    override fun getPrepaidKeyword(): String =
        properties.getProperty("keyword.prepaid") ?: "Prepagada"

    override fun getMortgageKeyword(): String =
        properties.getProperty("keyword.mortgage") ?: "Vivienda"
}
