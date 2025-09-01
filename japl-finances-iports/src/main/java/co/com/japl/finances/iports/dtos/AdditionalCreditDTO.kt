package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class AdditionalCreditDTO (
    var id:Int,
    var name:String,
    var value:BigDecimal,
    var creditCode:Long,
    var startDate:LocalDate,
    var endDate:LocalDate
)


