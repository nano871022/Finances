package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

data class TaxDTO (
    var id:Int,
    var month:Short,
    var year:Int,
    var status:Short,
    var codCreditCard:Int,
    var create: LocalDateTime,
    var value: Double,
    var kind: KindInterestRateEnum,
    var period: Short,
    var kindOfTax: KindOfTaxEnum?
    )
