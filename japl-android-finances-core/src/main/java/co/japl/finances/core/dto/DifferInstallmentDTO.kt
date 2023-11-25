package co.japl.finances.core.dto

import java.time.LocalDate

data class DifferInstallmentDTO(
    var id:Int,
    var create:LocalDate,
    var cdBoughtCreditCard:Long,
    var pendingValuePayable:Double,
    var originValue:Double,
    var newInstallment:Double,
    var oldInstallment:Double
)