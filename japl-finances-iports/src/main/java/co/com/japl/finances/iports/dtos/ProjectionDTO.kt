package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindPaymentsEnums
import java.math.BigDecimal
import java.time.LocalDate

data class ProjectionDTO(
    var id:Int,
    var create:LocalDate,
    var end:LocalDate,
    var name:String,
    var type: KindPaymentsEnums = KindPaymentsEnums.MONTHLY,
    var value:BigDecimal,
    var quote:BigDecimal,
    var monthsLeft:Int = 0,
    var amountSaved:BigDecimal = BigDecimal.ZERO,
    var active:Boolean = true
)
