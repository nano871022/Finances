package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class CreditFormImpl @Inject constructor(val svc:ICredit) : ICreditFormPort {
    override fun save(credit: CreditDTO): Int {
        require(credit.name.isNotBlank()){"Name should be exits"}
        require(credit.value > BigDecimal.ZERO){"Value should be more than zero"}
        require(credit.periods>0){"Month should be more than zero"}
        require(credit.tax > 0){"Tax should be more than zero"}
        require(credit.date.isBefore(LocalDate.now().withDayOfMonth(1).plusMonths(1))){"Date should be less than current date ${credit.date}"}
        require(credit.quoteValue > BigDecimal.ZERO){"Quote should be more than zero"}
        return svc.save(credit)
    }

    override fun calculateQuoteCredit(
        value: BigDecimal,
        rate: Double,
        kindRate: KindOfTaxEnum,
        month: Int
    ): BigDecimal {
        return svc.calculateQuoteCredit(value, rate, kindRate, month)
    }

    override fun findCreditById(id: Int): CreditDTO? {
        return svc.findCreditById(id)
    }

}