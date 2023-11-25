package co.japl.finances.core.dto

import android.provider.BaseColumns
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreditCardDTO(
    var id:Int,
    var name:String,
    var maxQuotes:Short,
    var cutOffDay:Short,
    var warningValue:BigDecimal,
    var create:LocalDateTime,
    var status:Boolean,
    var interest1Quote:Boolean,
    var interest1NotQuote:Boolean
)
