package co.japl.android.finances.services.dto

import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    var id:Int,
    var name:String,
    var date:LocalDate,
    var tax:Double,
    var periods:Int,
    var value:BigDecimal,
    var quoteValue:BigDecimal,
    var kindOf:String,
    var kindOfTax:String
)

object CreditDB{
    object Entry{
        const val TABLE_NAME = "TB_CREDIT"
        const val COLUMN_NAME = "cdt_str_name"
        const val COLUMN_DATE = "cdt_dt_credit"
        const val COLUMN_TAX = "cdt_num_tax"
        const val COLUMN_PERIODS = "cdt_num_periods"
        const val COLUMN_VALUE = "cdt_num_value"
        const val COLUMN_QUOTE = "cdt_num_quote"
        const val COLUMN_KIND_OF = "cdt_str_kindof"
        const val COLUMN_KIND_OF_TAX = "cdt_str_kind_of_tax"
    }
}
