package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreditCardBoughtDTO (
    var codeCreditCard: Int,
    var nameCreditCard: String,
    var nameItem: String,
    var valueItem: BigDecimal,
    var interest:Double,
    var month: Int,
    var boughtDate: LocalDateTime,
    var cutOutDate: LocalDateTime,
    var createDate: LocalDateTime,
    var endDate: LocalDateTime,
    var id:Int,
    var recurrent:Short,
    var kind:KindInterestRateEnum,
    var kindOfTax:KindOfTaxEnum
)