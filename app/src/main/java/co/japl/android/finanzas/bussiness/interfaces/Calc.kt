package co.japl.android.finanzas.bussiness.interfaces

import java.math.BigDecimal

interface Calc {

    fun calc(value:BigDecimal,period:Long,tax:Double):BigDecimal
}