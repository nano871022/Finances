package co.japl.finances.core.adapters.inbound.implement.dian

import co.com.japl.finances.iports.dtos.TaxProjectionDTO
import co.com.japl.finances.iports.dtos.LimitStatus
import co.com.japl.finances.iports.inbounds.common.GetTaxProjectionUseCase
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import javax.inject.Inject

class GetTaxProjectionUseCaseImpl @Inject constructor(
    private val configPort: TaxConfigurationPort,
    private val financialDataPort: ExternalFinancialDataPort
) : GetTaxProjectionUseCase {

    override suspend fun getProjection(year: Int): List<TaxProjectionDTO> {
        val uvt = configPort.getUVTValue(year)
        val incomeYTD = financialDataPort.getIncomeYTD(year)
        val projectedIncome = financialDataPort.getProjectedIncome(year)
        val thresholdIncome = configPort.getIncomeThresholdUVT().multiply(uvt)
        val creditDebt = financialDataPort.getCreditDebt(LocalDate.now())
        val creditCardDebt = financialDataPort.getCreditCardDebt(LocalDate.now())

        return listOf(
            TaxProjectionDTO(
                year = year,
                currentYTD = incomeYTD,
                projectedEndOfYear = projectedIncome,
                threshold = thresholdIncome,
                limitStatus = calculateStatus(projectedIncome, thresholdIncome),
                creditDebt = creditDebt,
                creditCardDebt = creditCardDebt
            )
        )
    }

    private fun calculateStatus(projected: BigDecimal, threshold: BigDecimal): LimitStatus {
        val ratio = projected.divide(threshold, 2, RoundingMode.HALF_UP).toDouble()
        return when {
            ratio >= 1.0 -> LimitStatus.EXCEEDED
            ratio >= 0.8 -> LimitStatus.WARNING
            else -> LimitStatus.SAFE
        }
    }
}
