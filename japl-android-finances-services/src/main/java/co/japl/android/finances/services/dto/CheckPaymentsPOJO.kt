package co.japl.android.finances.services.dto

import co.japl.android.finances.services.enums.CheckPaymentsEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class CheckPaymentsPOJO(
    var id:Long,
    var codPaid:Long,
    var checkValue:Boolean,
    var name:String,
    var value:BigDecimal,
    var date:LocalDateTime?,
    var period:String?,
    var type:CheckPaymentsEnum?
)
