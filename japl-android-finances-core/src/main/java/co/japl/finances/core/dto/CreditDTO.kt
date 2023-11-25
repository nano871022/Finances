package co.japl.finances.core.dto

import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    var id:Int,
    var name:String,
    var date:LocalDate,
    var tax:Double,
    var periods:Int,
    var value:BigDecimal,
    var quoteValue:BigDecimal,
    var kindOf:String,
    var kindOfTax:String
)

