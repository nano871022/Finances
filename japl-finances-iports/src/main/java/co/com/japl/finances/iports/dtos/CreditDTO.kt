package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
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
    var kindOf: KindPaymentsEnums,
    var kindOfTax: KindOfTaxEnum
)

