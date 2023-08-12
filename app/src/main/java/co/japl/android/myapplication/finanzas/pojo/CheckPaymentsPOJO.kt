package co.japl.android.myapplication.finanzas.pojo

import co.japl.android.myapplication.finanzas.enums.CheckPaymentsEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Period

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
