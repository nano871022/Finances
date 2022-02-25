package co.japl.android.myapplication.bussiness

import java.math.BigDecimal

interface CalcInterest {

    fun calc(value:BigDecimal,period:Long,tax:Double,quoteNumber:Long):BigDecimal
}