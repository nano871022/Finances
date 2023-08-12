package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class CheckCreditDTO(
    var id:Int,
    var date:LocalDateTime,
    var check:Short,
    var period:String,
    var codCredit:Int
) : ICheck

object CheckCreditDB{
    object Entry{
        const val TABLE_NAME = "TB_CHECK_CREDIT"
        const val COLUMN_DATE_CREATE = "dt_paid"
        const val COLUMN_PERIOD = "str_period"
        const val COLUMN_COD_CREDIT = "cod_credit"
        const val COLUMN_CHECK = "num_check"
    }
}
