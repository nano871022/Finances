package co.japl.android.finances.services.interfaces

import java.math.BigDecimal

interface alcInterest {

    fun calc(value:BigDecimal,period:Long,tax:Double,quoteNumber:Long):BigDecimal
}