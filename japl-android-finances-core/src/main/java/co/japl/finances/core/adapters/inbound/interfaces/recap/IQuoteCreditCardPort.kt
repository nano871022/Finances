package co.japl.finances.core.adapters.inbound.interfaces.recap

import java.math.BigDecimal

interface IQuoteCreditCardPort {

    fun getTotalQuoteTC(): BigDecimal
}