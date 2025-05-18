package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.Period

data class RecapCreditDTO(
    val id:Int,
    val month:Int,
    val year:Int,
    val name:String,
    val quoteValue:BigDecimal,
    val interestValue:BigDecimal,
    val capitalValue:BigDecimal,
    val pendingPerPaid:BigDecimal,
    val additionalAmount:BigDecimal
)
