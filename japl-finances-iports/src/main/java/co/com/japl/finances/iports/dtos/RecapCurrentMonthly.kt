package co.com.japl.finances.iports.dtos

data class RecapCurrentMonthly(
    val totalQuote:Double,
    val numQuotes:Int,
    val numOneQuote :Int,
    val capitalValue:Double,
    val interestValue:Double
)
