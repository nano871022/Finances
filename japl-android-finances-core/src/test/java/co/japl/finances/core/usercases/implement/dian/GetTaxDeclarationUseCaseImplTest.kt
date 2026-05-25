package co.japl.finances.core.usercases.implement.dian

import android.content.Context
import co.com.japl.finances.iports.dtos.FinancialItemDTO
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.com.japl.finances.iports.outbounds.TaxBracketConfig
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.time.LocalDate

class GetTaxDeclarationUseCaseImplTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var configPort: TaxConfigurationPort

    @Mock
    private lateinit var financialDataPort: ExternalFinancialDataPort

    private lateinit var useCase: GetTaxDeclarationUseCaseImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetTaxDeclarationUseCaseImpl(context, configPort, financialDataPort)
    }

    @Test
    fun testRounding() = runBlocking {
        val uvt = BigDecimal("47065")
        `when`(configPort.getUVTValue(2024)).thenReturn(uvt)

        val brackets = listOf(
            TaxBracketConfig(BigDecimal.ZERO, BigDecimal("1090"), 0.0, BigDecimal.ZERO),
            TaxBracketConfig(BigDecimal("1090"), BigDecimal("1700"), 0.19, BigDecimal.ZERO)
        )
        `when`(configPort.getTaxBrackets()).thenReturn(brackets)
        `when`(configPort.getDependentsDeductionMaxUVT()).thenReturn(BigDecimal("384"))
        `when`(configPort.getDependentsDeductionRate()).thenReturn(BigDecimal("0.10"))
        `when`(configPort.getPrepaidHealthMaxUVT()).thenReturn(BigDecimal("192"))
        `when`(configPort.getMortgageInterestMaxUVT()).thenReturn(BigDecimal("1200"))
        `when`(configPort.getExemptIncomeRate()).thenReturn(BigDecimal("0.25"))
        `when`(configPort.getExemptIncomeMaxUVT()).thenReturn(BigDecimal("948"))
        `when`(configPort.getLimit40PercentRate()).thenReturn(BigDecimal("0.40"))
        `when`(configPort.getLimitFlatUVT()).thenReturn(BigDecimal("1340"))
        `when`(configPort.getNextYearAdvanceRate()).thenReturn(BigDecimal("0.75"))
        `when`(configPort.getRoundingFactor()).thenReturn(BigDecimal("1000"))

        val grossIncome = uvt.multiply(BigDecimal("2000"))

        `when`(financialDataPort.getAssetsAt(LocalDate.of(2024, 12, 31))).thenReturn(emptyList())
        `when`(financialDataPort.getLiabilitiesAt(LocalDate.of(2024, 12, 31))).thenReturn(emptyList())
        `when`(financialDataPort.getIncomeDetails(2024)).thenReturn(listOf(
            FinancialItemDTO("Salary", grossIncome, LocalDate.now(), "INCOME")
        ))
        `when`(financialDataPort.getDeductionDetails(2024)).thenReturn(emptyList())
        `when`(financialDataPort.getWithholdingDetails(2024)).thenReturn(emptyList())

        `when`(configPort.getWealthThresholdUVT()).thenReturn(BigDecimal("4500"))
        `when`(configPort.getIncomeThresholdUVT()).thenReturn(BigDecimal("1400"))
        `when`(configPort.getConsumptionThresholdUVT()).thenReturn(BigDecimal("1400"))
        `when`(configPort.getPatrimonyThresholdUVT()).thenReturn(BigDecimal("4500"))

        `when`(context.getString(anyInt())).thenReturn("Mock String")

        val result = useCase.getTaxDeclaration(2024)

        assertEquals(0, BigDecimal("2325000").compareTo(result.taxOnTaxableBase))
    }
}
