package co.japl.finances.core.adapters.inbound.implement.recap

import co.japl.finances.core.adapters.inbound.interfaces.recap.IQuoteCreditCardPort
import co.japl.finances.core.usercases.interfaces.recap.IQuoteCreditCard
import java.math.BigDecimal
import javax.inject.Inject

class QuoteCreditCardImpl @Inject constructor(private val quoteCCImpl: IQuoteCreditCard): IQuoteCreditCardPort {
    override fun getTotalQuoteTC(): BigDecimal {
        return quoteCCImpl.getTotalQuote()
    }
}