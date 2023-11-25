package co.japl.android.finances.services.dto

import java.time.LocalDateTime

data class CheckPaymentsDTO(
    var id:Int,
    var date:LocalDateTime,
    var check:Short,
    var period:String,
    var codPaid:Int,
) : ICheck

object CheckPaymentsDB{
    object Entry{
        const val TABLE_NAME = "TB_CHECK_PAYMENTS"
        const val COLUMN_DATE_CREATE = "dt_paid"
        const val COLUMN_PERIOD = "str_period"
        const val COLUMN_COD_PAID = "cod_paid"
        const val COLUMN_CHECK = "num_check"
    }
}
