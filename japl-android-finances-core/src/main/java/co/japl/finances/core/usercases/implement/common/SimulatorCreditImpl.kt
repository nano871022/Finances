package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.InterestRateCalculation
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.interfaces.common.IQuoteCreditCard
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import javax.inject.Inject

class SimulatorCreditImpl @Inject constructor(private val valuesCalculation: ValuesCalculation
                                               ,private val interestCalculation: InterestCalculations): ISimulatorCredit {
    override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
        val interestRate = InterestRateCalculation.getNM(dto.tax,dto.kindOfTax)
        val interestValue = interestCalculation.getInterestValue(
            month = dto.periods,
            monthPaid = 0,
            kind = KindInterestRateEnum.CREDIT_CARD,
            pendingToPay = dto.value.toDouble(),
            valueItem = dto.value.toDouble(),
            interest = interestRate,
            kindOfRate = KindOfTaxEnum.MONTLY_NOMINAL,
            interest1Quote = true,
            interest1NotQuote = false,
            rediffer = false
        )
        val capital = valuesCalculation.getCapital(
            codeBought = 0,
            valueItem = dto.value.toDouble(),
            month = dto.periods,
            differQuotes = null
        )
        return dto.copy(interestValue = interestValue.toBigDecimal(), capitalValue = capital.toBigDecimal(), quoteValue = interestValue.toBigDecimal() + capital.toBigDecimal())
    }

    override fun save(dto: SimulatorCreditDTO): Boolean {
        return false
    }
}