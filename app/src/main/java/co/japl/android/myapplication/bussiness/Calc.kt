package co.japl.android.myapplication.bussiness

import java.math.BigDecimal

interface Calc {

    fun calc(value:BigDecimal,period:Long,tax:Double):BigDecimal
}