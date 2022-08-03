package co.japl.android.finanzas.bussiness.interfaces

import java.math.BigDecimal

interface CalcInterest {

    fun calc(value:BigDecimal,period:Long,tax:Double,quoteNumber:Long):BigDecimal
}