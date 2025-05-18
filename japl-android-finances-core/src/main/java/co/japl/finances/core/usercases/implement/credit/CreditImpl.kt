package co.japl.finances.core.usercases.implement.credit

import android.util.Log
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.outbounds.IAdditionalRecapPort
import co.com.japl.finances.iports.outbounds.ICreditFixRecapPort
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.InterestRateCalculation
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

class CreditImpl @Inject constructor(private val creditSvc:ICreditPort, private val additionalSvc:IAdditionalRecapPort, private val interestCalculation: InterestCalculations, private val valuesCalculation: ValuesCalculation): ICredit {
    override fun getAllEnable(period:YearMonth): List<CreditDTO> {
        return creditSvc.getAllActive(period).map{
            it.quoteValue += additionalSvc.getListByIdCredit(it.id.toLong()).sumOf { it.value }
            it
        }
    }

    override fun delete(id: Int): Boolean {
        return creditSvc.delete(id)
    }

    override fun getCreditsEnables(period: YearMonth): List<RecapCreditDTO> = creditSvc.getAllActive(period).map (this::getRecapCreditByCredit)


    private fun getRecapCreditByCredit(creditDTO: CreditDTO):RecapCreditDTO {
        val additionalAmount = additionalSvc.getListByIdCredit(creditDTO.id.toLong()).sumOf { it.value }
        val capitalValue = valuesCalculation.getCapital(
            codeBought = creditDTO.id,
            valueItem = creditDTO.value.toDouble(),
            month = creditDTO.periods.toShort(),
            differQuotes = null
        )
        val quotesPaid = valuesCalculation.getQuotesPaid(
            codeBought = creditDTO.id,
            month = creditDTO.periods.toShort(),
            boughtDate = LocalDateTime.of(creditDTO.date, LocalTime.MAX),
            cutOff = LocalDateTime.now().withDayOfMonth(1).plusMonths(1).minusDays(1),
            differQuotes = null
        )
        val pendingPerPaid = valuesCalculation.getPendingToPay(
            codeBought = creditDTO.id,
            month = creditDTO.periods.toShort(),
            monthPaid = quotesPaid,
            valueItem = creditDTO.value.toDouble(),
            capitalValue = capitalValue,
            differQuotes = null
        )
        val kindOfRate = KindOfTaxEnum.findByValue(creditDTO.kindOfTax)
        val tax = InterestRateCalculation.getNM(creditDTO.tax, kindOfRate)
        val interestValue = interestCalculation.getInterestValue(
            month = creditDTO.date.monthValue.toShort(),
            monthPaid = quotesPaid,
            pendingToPay = pendingPerPaid,
            valueItem = creditDTO.value.toDouble(),
            interest = tax,
            kind = KindInterestRateEnum.CREDIT_CARD,
            kindOfRate = kindOfRate,
            interest1Quote = false,
            interest1NotQuote = false,
            rediffer = false
        )
       return RecapCreditDTO(
            id = creditDTO.id,
            month = creditDTO.date.monthValue,
            year = creditDTO.date.year,
            name = creditDTO.name,
            quoteValue = creditDTO.quoteValue + additionalAmount,
            capitalValue = creditDTO.quoteValue - interestValue.toBigDecimal(),
            interestValue = interestValue.toBigDecimal(),
            additionalAmount = additionalAmount,
            pendingPerPaid = pendingPerPaid.toBigDecimal()
        )
    }

}