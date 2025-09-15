package co.com.japl.module.credit.views.fakesSvc

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import java.math.BigDecimal
import java.time.YearMonth

class CreditPortFake : ICreditPort {
    override fun getCredit(code: Int): CreditDTO? {
        return null
    }

    override fun getCredits(): List<CreditDTO> {
        return emptyList()
    }

    override fun getCreditEnable(period: YearMonth): List<CreditDTO> {
        return emptyList()
    }

    override fun delete(code: Int): Boolean {
        return true
    }

    override fun create(credit: CreditDTO): Int {
        return 1
    }

    override fun update(credit: CreditDTO): Boolean {
        return true
    }

    override fun getTotalQuote(period: YearMonth): BigDecimal {
        return BigDecimal.ZERO
    }
}
