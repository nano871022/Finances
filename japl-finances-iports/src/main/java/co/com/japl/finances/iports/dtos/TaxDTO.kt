package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

data class TaxDTO (
    var id: Int = 0,
    var month: Short = 0,
    var year: Int = 0,
    var status: Short = 0,
    var codCreditCard: Int = 0,
    var create: LocalDateTime = LocalDateTime.now(),
    var value: Double = 0.0,
    var kind: KindInterestRateEnum = KindInterestRateEnum.CREDIT_CARD,
    var period: Short = 0,
    var kindOfTax: KindOfTaxEnum? = null,
    var cashAdvanceValue: Double? = null
)
