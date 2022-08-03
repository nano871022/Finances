package co.japl.android.finanzas.bussiness.DTO

import android.provider.BaseColumns
import java.math.BigDecimal

data class CalcDTO (
    var name:String,
    var valueCredit:BigDecimal
    ,var interest:Double
    ,var period:Long
    ,var quoteCredit:BigDecimal
    ,var type:String
    ,var id:Int
    ,var interestValue:BigDecimal
    ,var capitalValue:BigDecimal
)

object CalcDB{
    object CalcEntry:BaseColumns{
        const val TABLE_NAME = "TB_CALC"
        const val COLUMN_ALIAS = "str_alias"
        const val COLUMN_VALUE_CREDIT = "num_value_credit"
        const val COLUMN_INTEREST = "num_interest"
        const val COLUMN_INTEREST_VALUE = "num_interest_value"
        const val COLUMN_PERIOD = "num_period"
        const val COLUMN_QUOTE_CREDIT = "num_quote_credit"
        const val COLUMN_TYPE = "str_type"
        const val COLUMN_CAPITAL_VALUE = "num_capital_value"
    }
}