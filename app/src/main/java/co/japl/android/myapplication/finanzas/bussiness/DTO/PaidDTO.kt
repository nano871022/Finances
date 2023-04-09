package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class PaidDTO(
    var id:Int,
    var date:LocalDate,
    var account:String,
    var name:String,
    var value:BigDecimal,
    var recurrent:Short
)

object PaidDB{
    object Entry{
        const val TABLE_NAME = "TB_PAID"
        const val COLUMN_DATE_PAID = "dt_paid"
        const val COLUMN_ACCOUNT = "str_account"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_VALUE = "nbr_value"
        const val COLUMN_RECURRENT = "nbr_recurrent"
    }
}
