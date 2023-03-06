package co.japl.android.myapplication.bussiness.DTO

import java.time.LocalDateTime

data class CreditCardSettingDTO (
    var id:Int,
    var codeCreditCard:Int,
    var name:String,
    var value: String,
    var type: String,
    var create: LocalDateTime,
    var active: Short
    )

object CreditCardSettingDB{
    object CreditCardEntry{
        const val TABLE_NAME = "TB_CREDIT_CARD_SETTING"
        const val COLUMN_COD_CREDIT_CARD = "ccs_str_cod_credit_card"
        const val COLUMN_NAME = "ccs_str_name"
        const val COLUMN_VALUE = "ccs_str_value"
        const val COLUMN_TYPE = "ccs_str_type"
        const val COLUMN_CREATE_DATE = "ccs_dt_create"
        const val COLUMN_ACTIVE = "ccs_num_active"
    }
}