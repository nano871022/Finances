package co.japl.finances.core.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
@RequiresApi(Build.VERSION_CODES.N)
data class CreditCard (
    val nameCreditCard:String,
    val codeCreditCard:Int,
    val maxQuotes:Short ,
    val cutOff: LocalDateTime,
    val cutOffLast: LocalDateTime,
    val capital: Double ,
    val capitalQuotes: Double,
    val capitalQuote: Double,

    val interest: Double,
    val interestQuote: Double,
    val interestQuotes: Double,
    val interest1NotQuote:Boolean,
    val quotes: Long ,
    val oneQuote:Long ,
    val quotesPending:Long,
    val lastTax:Double ,
    val cutoffDay:Short,
    val warningValue:Double,
    val interest1Quote:Boolean
)