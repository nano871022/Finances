package co.japl.finances.core.usercases.implement.creditcard.bought

import android.util.Log
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
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:ICreditCardCheckPaymentPort, private val quoteSvc:IQuoteCreditCardPort, private val boughtListsvc:IBoughtList, private val creditCardSvc:ICreditCardPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
      return  svc.getPeriodsPayment().map{
            val period = it[0] as String
            val codCreditCard = it[1] as Int
            val count = it[2] as Long
            val check = it[3] as Boolean
            creditCardSvc.get(codCreditCard)?.let{
                val creditCard = CreditCardMap.mapper(it)
                val period = YearMonth.parse(period,DateTimeFormatter.ofPattern("yyyyMM"))
                val cutoff = DateUtils.cutOff(it.cutOffDay, period)
                boughtListsvc.getBoughtList(creditCard,cutoff,false).let{
                    PeriodCheckPaymentDTO(period,count.toInt(),it.recap.quoteValue ,if (check) it.recap.quoteValue else 0.0)
                }
            }
        }.groupBy { it?.period }.map{
            PeriodCheckPaymentDTO(it.key!!,
                it.value.sumOf { it?.count?:0 },
                it.value.sumOf { it?.amount?:0.0 },
                it.value.sumOf { it?.paid?:0.0 })
        }
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