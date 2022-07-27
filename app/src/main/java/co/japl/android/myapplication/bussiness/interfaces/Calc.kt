package co.japl.android.myapplication.bussiness.interfaces

import java.math.BigDecimal

interface Calc {

    fun calc(value:BigDecimal,period:Long,tax:Double):BigDecimal
}