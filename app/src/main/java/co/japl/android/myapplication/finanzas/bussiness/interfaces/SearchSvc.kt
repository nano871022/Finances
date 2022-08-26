package co.japl.android.myapplication.bussiness.interfaces

import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

interface SearchSvc<T>  {
    var dbConnect: SQLiteOpenHelper

    fun getToDate(key:Int,localDateTime:LocalDateTime):List<T>
    fun getPendingQuotes(key:Int,cutoffCurrent:LocalDateTime):List<T>
    fun getCapital(key:Int,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getInterest(key:Int,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getCapitalPendingQuotes(key:Int,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getInterestPendingQuotes(key:Int,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getBought(key:Int,cutOff:LocalDateTime):Optional<Long>
    fun getBoughtQuotes(key:Int,cutOff:LocalDateTime):Optional<Long>
    fun getBoughtPendingQuotes(key:Int,cutOff:LocalDateTime):Optional<Long>
    fun getPendingToPay(key:Int,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getPendingToPayQuotes(key:Int,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getRecurrentBuys(key:Int, cutOff:LocalDateTime):List<T>

}