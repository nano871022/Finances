package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class ProjectionDTO(
    var id:Int,
    var create:LocalDate,
    var end:LocalDate,
    var name:String,
    var type:String,
    var value:BigDecimal,
    var quote:BigDecimal,
    var active:Short
)
