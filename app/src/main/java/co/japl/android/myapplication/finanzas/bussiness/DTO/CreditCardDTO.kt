package co.japl.android.myapplication.bussiness.DTO

import android.provider.BaseColumns
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreditCardDTO(
    var id:Int,
    var name:String,
    var cutOffDay:Short,
    var warningValue:BigDecimal,
    var create:LocalDateTime,
    var status:Short
)

object CreditCardDB{
    object CreditCardEntry:BaseColumns{
        const val TABLE_NAME = "TB_CREDIT_CARD"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_CUT_OFF_DAY = "num_cut_off_day"
        const val COLUMN_WARNING_VALUE = "num_warning_quote"
        const val COLUMN_CREATE_DATE = "dt_create"
        const val COLUMN_STATUS = "num_status"
    }
}
