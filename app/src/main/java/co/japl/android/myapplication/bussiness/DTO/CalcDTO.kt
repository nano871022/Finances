package co.japl.android.myapplication.bussiness.DTO

import android.provider.BaseColumns
import java.math.BigDecimal

data class CalcDTO (
    var name:String,
    var valueCredit:BigDecimal
    ,var interest:Double
    ,var period:Long
    ,var quoteCredit:BigDecimal

)

object CalcDB{
    object CalcEntry:BaseColumns{
        const val TABLE_NAME = "TB_CALC"
        const val COLUMN_ALIAS = "str_alias"
        const val COLUMN_VALUE_CREDIT = "num_value_credit"
        const val COLUMN_INTEREST = "num_interest"
        const val COLUMN_PERIOD = "num_period"
        const val COLUMN_QUOTE_CREDIT = "num_quote_credit"
    }
}