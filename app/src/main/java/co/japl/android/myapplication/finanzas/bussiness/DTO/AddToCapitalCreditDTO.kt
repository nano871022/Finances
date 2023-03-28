package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class AddToCapitalCreditDTO(
    var id:Int,
    var value:BigDecimal,
    var creditCode:Int,
    var date:LocalDate
)

object AddToCapitalCreditDB{
    object Entry{
        const val TABLE_NAME = "TB_ADD_TO_CAPITAL_CREDIT"
        const val COLUMN_VALUE = "acc_num_value"
        const val COLUMN_CREDIT_CODE = "acc_num_credit"
        const val COLUMN_DATE = "acc_dt_add"
    }
}
