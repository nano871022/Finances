package co.japl.android.myapplication.bussiness.DTO

import android.provider.BaseColumns
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreditCardBoughtDTO (
    var codeCreditCard: Int,
    var nameCreditCard: String,
    var nameItem: String,
    var valueItem: BigDecimal,
    var interest:Double,
    var month: Int,
    var boughtDate: LocalDateTime,
    var cutOutDate: LocalDateTime,
    var createDate: LocalDateTime,
    var id:Int,
    var recurrent:Short,
    var kind:Short,
    var kindOfTax:String
)

object CreditCardBoughtDB{
    object CreditCardBoughtEntry:BaseColumns{
        const val TABLE_NAME = "TB_CREDIT_CARD_BOUGHT"
        const val COLUMN_CODE_CREDIT_CARD = "str_code_credit_card"
        const val COLUMN_NAME_ITEM = "str_name_item"
        const val COLUMN_VALUE_ITEM = "num_value_item"
        const val COLUMN_INTEREST = "num_interest"
        const val COLUMN_MONTH = "num_month"
        const val COLUMN_BOUGHT_DATE = "dt_bought"
        const val COLUMN_CUT_OUT_DATE = "dt_cut_out"
        const val COLUMN_CREATE_DATE = "dt_create"
        const val COLUMN_RECURRENT = "num_recurrent"
        const val COLUMN_KIND = "num_kind"
        const val COLUMN_KIND_OF_TAX = "str_kind_of_tax"
    }
}