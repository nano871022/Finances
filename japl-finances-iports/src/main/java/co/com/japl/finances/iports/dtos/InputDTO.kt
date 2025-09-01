package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class InputDTO(
    val id:Int,
    val date:LocalDate,
    val accountCode:Int,
    val kindOf:String,
    val name:String,
    val value:BigDecimal,
    val dateStart: LocalDate,
    val dateEnd: LocalDate
)
