package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class CheckQuoteDTO(
    var id:Int,
    var date:LocalDateTime,
    var check:Short,
    var period:String,
    var codQuote:Int
) : ICheck

object CheckQuoteDB{
    object Entry{
        const val TABLE_NAME = "TB_CHECK_QUOTE"
        const val COLUMN_DATE_CREATE = "dt_paid"
        const val COLUMN_PERIOD = "str_period"
        const val COLUMN_COD_QUOTE = "cod_quote"
        const val COLUMN_CHECK = "num_check"
    }
}
