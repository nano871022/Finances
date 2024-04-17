package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import java.time.LocalDateTime
import java.time.YearMonth

data class CheckPaymentDTO (
    val id:Int,
    var name:String,
    val period: YearMonth,
    val codPaid:Long,
    var amount:Double=0.0,
    var date:LocalDateTime?=null,
    var check:Boolean=false,
    var update:Boolean=false,
    val type:CheckPaymentsEnum
)