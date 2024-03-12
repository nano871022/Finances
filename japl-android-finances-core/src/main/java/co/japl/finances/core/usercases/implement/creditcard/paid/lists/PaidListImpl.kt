package co.japl.finances.core.usercases.implement.creditcard.paid.lists

import android.util.Log
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.model.Tax
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.implement.common.ListBoughts
import co.japl.finances.core.usercases.interfaces.creditcard.paid.lists.IPaidList
import co.japl.finances.core.usercases.mapper.CreditCardMap
import co.japl.finances.core.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class PaidListImpl @Inject constructor(
                                        private val creditCardSvc: ICreditCardPort
                                       , private val quoteCCSvc: IQuoteCreditCardPort
                                       , private val interestCalculation: InterestCalculations
                                       , private val differQuotesSvc:IDifferInstallmentRecapPort
                                       , private val listBoughts: ListBoughts
) :IPaidList{

    override fun getBoughtPeriodList(idCreditCard: Int,cache:Boolean): List<BoughtCreditCardPeriodDTO>? {
        val creditCard = creditCardSvc.get(idCreditCard)?.let{CreditCardMap.mapper(it)}
        val list =  quoteCCSvc.getBoughtPeriodList(idCreditCard)


        return list?.map{cutoff->
            val periodStart = DateUtils.startDateFromCutoff(creditCard?.cutoffDay!!,cutoff)

            val period = YearMonth.of(cutoff.year,cutoff.month)
            val list = listBoughts.getList(creditCard!!,cutoff,cache)
            val taxQuote = interestCalculation.getTax(creditCard.codeCreditCard,KindInterestRateEnum.CREDIT_CARD,period)
            val taxAdvance = interestCalculation.getTax(creditCard.codeCreditCard,KindInterestRateEnum.CASH_ADVANCE,period)
            val differQuotes = differQuotesSvc.get(cutoff.toLocalDate())

            list.map{
                listBoughts.calculateValues(it,creditCard,taxQuote,taxAdvance,cutoff,differQuotes)
            }

            createPeriod(list,creditCard,periodStart,cutoff)
        }
    }

    private fun createPeriod(list:List<CreditCardBoughtItemDTO>, creditCard: CreditCard,periodStart:LocalDateTime, periodEnd:LocalDateTime): BoughtCreditCardPeriodDTO {
        return BoughtCreditCardPeriodDTO(
            creditCardId=creditCard.codeCreditCard
            ,periodStart= periodStart
            ,periodEnd=periodEnd
            ,interest= list.sumOf { it.interestValue }.toBigDecimal()
            ,capital= list.sumOf { it.capitalValue }.toBigDecimal()
            ,total= list.sumOf { it.quoteValue }.toBigDecimal()
            , count = list.count().toLong()
            , cutoffDay = creditCard.cutoffDay
        )
    }
}