package co.japl.finances.core.dto

import java.time.LocalDateTime

data class TaxDTO (
    var id:Int,
    var month:Short,
    var year:Int,
    var status:Short,
    var codCreditCard:Int,
    var create: LocalDateTime,
    var value: Double,
    var kind: Short,
    var period: Short,
    var kindOfTax: String?
    )
