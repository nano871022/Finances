package co.japl.android.myapplication.bussiness.interfaces

import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

interface SearchSvc<T>  {
    var dbConnect: SQLiteOpenHelper

    fun getToDate(key:Int,startDate:LocalDateTime,endDate:LocalDateTime):List<T>
    fun getPendingQuotes(key:Int,startDate: LocalDateTime,cutoffCurrent:LocalDateTime):List<T>
    fun getCapital(key:Int,startDate:LocalDateTime,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getInterest(key:Int,startDate:LocalDateTime,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getCapitalPendingQuotes(key:Int,startDate: LocalDateTime,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getInterestPendingQuotes(key:Int,startDate: LocalDateTime,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getBought(key:Int,startDate:LocalDateTime,cutOff:LocalDateTime):Optional<Long>
    fun getBoughtQuotes(key:Int,startDate:LocalDateTime,cutOff:LocalDateTime):Optional<Long>
    fun getBoughtPendingQuotes(key:Int,startDate: LocalDateTime,cutOff:LocalDateTime):Optional<Long>
    fun getPendingToPay(key:Int,startDate:LocalDateTime,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getPendingToPayQuotes(key:Int,startDate: LocalDateTime,cutOff:LocalDateTime):Optional<BigDecimal>
    fun getRecurrentBuys(key:Int, cutOff:LocalDateTime):List<T>

}