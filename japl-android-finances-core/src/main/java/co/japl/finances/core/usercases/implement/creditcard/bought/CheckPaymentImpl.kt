package co.japl.finances.core.usercases.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.ICheckPayment
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.mapper.CreditCardMap
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:ICreditCardCheckPaymentPort, private val quoteSvc:IQuoteCreditCardPort, private val boughtListsvc:IBoughtList, private val creditCardSvc:ICreditCardPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        val list = mutableListOf<CheckPaymentDTO>()
        val checks = svc.getCheckPayments(period).takeIf { it.isNotEmpty() }?.map{check->
            creditCardSvc.get(check.codPaid.toInt())?.let { creditCardDto ->
                check.name = creditCardDto.name
                val creditCard = CreditCardMap.mapper(creditCardDto)
                val cutoff = DateUtils.cutOff(creditCardDto.cutOffDay,period)
                boughtListsvc.getBoughtList(creditCard, cutoff, false).let{
                        check.amount = it.recap.quoteValue
                    }
                }
            check
        }?: emptyList()
        list.addAll(checks)
        creditCardSvc.getAll().takeIf { it.isNotEmpty() }?.map {creditCardDto->
            val creditCard = CreditCardMap.mapper(creditCardDto)
            val cutoff = DateUtils.cutOff(creditCardDto.cutOffDay,period)
            boughtListsvc.getBoughtList(creditCard, cutoff, false).let{
                CheckPaymentDTO(
                    id = 0,
                    name = creditCardDto.name,
                    amount = it.recap.quoteValue,
                    date = cutoff,
                    codPaid = creditCardDto.id.toLong(),
                    period = period,
                    type = CheckPaymentsEnum.QUOTE_CREDIT_CARD
                )
            }.takeIf {
                checks.none { it.codPaid == creditCardDto.id.toLong() }
            }
        }?.takeIf { it.isNotEmpty() }?.filterNotNull()?.let(list::addAll)
        return list
    }

    override fun update(check: CheckPaymentDTO): Boolean {
        return svc.save(check).id > 0
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        return svc.save(check)
    }
}