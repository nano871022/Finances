package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.outbounds.ISimulatorCreditPort
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.InterestRateCalculation
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import java.math.BigDecimal
import javax.inject.Inject

class SimulatorCreditImpl @Inject constructor(private val simulatorCreditSvc: ISimulatorCreditPort,
                                                private val valuesCalculation: ValuesCalculation
                                               ,private val interestCalculation: InterestCalculations): ISimulatorCredit {
    override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
        val calculate = if(dto.isCreditVariable) {
                calculateQuote(dto.value, dto.tax, dto.kindOfTax, 0, dto.periods.toInt(), 1)
        }else{
            val (interes,capital,pair) = calculateFixQuote(dto.value, dto.tax, dto.kindOfTax, 0, dto.periods.toInt(), 1)
            val (quote, pending) = pair
            Triple(interes,capital,quote)
        }
        return dto.copy(interestValue = calculate.first, capitalValue = calculate.second, quoteValue = calculate.third)
    }

    override fun calculateQuote(valueCredit: BigDecimal,creditRate:Double,kindOfRate: KindOfTaxEnum,monthPaid:Int,months:Int,quoteNum:Int): Triple<BigDecimal,BigDecimal,BigDecimal>{
        val interestRate = InterestRateCalculation.getNM(creditRate,kindOfRate)
        val capital = valuesCalculation.getCapital(
            codeBought = 0,
            valueItem = valueCredit.toDouble(),
            month = months.toShort(),
            differQuotes = null
        )
        val interestValue = interestCalculation.getInterestValue(
            month = months.toShort(),
            monthPaid = monthPaid.toShort(),
            kind = KindInterestRateEnum.CREDIT_CARD,
            pendingToPay = valueCredit.toDouble() - (capital * monthPaid),
            valueItem = valueCredit.toDouble(),
            interest = interestRate,
            kindOfRate = KindOfTaxEnum.MONTLY_NOMINAL,
            interest1Quote = true,
            interest1NotQuote = false,
            rediffer = false
        )

        return Triple(interestValue.toBigDecimal(),capital.toBigDecimal(),interestValue.toBigDecimal() + capital.toBigDecimal())
    }

    override fun calculateFixQuote(valueCredit: BigDecimal,creditRate:Double,kindOfRate: KindOfTaxEnum,monthPaid:Int,months:Int,quoteNum:Int): Triple<BigDecimal,BigDecimal,Pair<BigDecimal,BigDecimal>>{
        val interestRate = InterestRateCalculation.getNM(creditRate,kindOfRate)
        val quote = valuesCalculation.calculateQuoteCredit(
            value = valueCredit.toDouble(),
            rateEM = interestRate,
            months = months.toShort())
        val pending = monthPaid.takeIf{it > 0}?.let {
            valuesCalculation.calculatePendingCapital(
                taxNM = interestRate,
                totalPeriods = months.toShort(),
                quoteValue = quote.toDouble(),
                monthPaid = monthPaid.toShort()
            )
        }?:valueCredit.toDouble()
        val interestValue = interestCalculation.getInterestValue(
            month = months.toShort(),
            monthPaid = monthPaid.toShort(),
            kind = KindInterestRateEnum.CREDIT_CARD,
            pendingToPay = pending ,
            valueItem = valueCredit.toDouble(),
            interest = interestRate,
            kindOfRate = KindOfTaxEnum.MONTLY_NOMINAL,
            interest1Quote = true,
            interest1NotQuote = false,
            rediffer = false
        )

        return Triple(interestValue.toBigDecimal(),(quote - interestValue).toBigDecimal(),Pair(quote.toBigDecimal(),pending.toBigDecimal()))
    }

    override fun save(dto: SimulatorCreditDTO,cache:Boolean): Long {
        return simulatorCreditSvc.save(dto,cache)
    }

    override fun getAll(isVariable:Boolean): List<SimulatorCreditDTO> {
        return simulatorCreditSvc.getAll(isVariable)
    }

    override fun delete(code: Int): Boolean {
        return simulatorCreditSvc.delete(code)
    }
}