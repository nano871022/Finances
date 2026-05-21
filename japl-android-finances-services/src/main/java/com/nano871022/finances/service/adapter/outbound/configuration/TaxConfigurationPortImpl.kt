package com.nano871022.finances.service.adapter.outbound.configuration

import android.content.Context
import com.nano871022.finances.iport.ports.outbound.TaxConfigurationPort
import com.nano871022.finances.iport.ports.outbound.TaxBracketConfig
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
}
