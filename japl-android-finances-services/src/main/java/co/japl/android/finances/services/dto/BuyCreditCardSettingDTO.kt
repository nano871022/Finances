package co.japl.android.finances.services.dto

import java.time.LocalDateTime

data class BuyCreditCardSettingDTO (
    var id:Int,
    var codeBuyCreditCard:Int,
    var codeCreditCardSetting:Int,
    var create: LocalDateTime,
    var active: Short
    )

object BuyCreditCardSettingDB{
    object Entry{
        const val TABLE_NAME = "TB_BUY_CREDIT_CARD_SETTING"
        const val COLUMN_COD_BUY_CREDIT_CARD = "bcs_str_cod_buy_credit_card"
        const val COLUMN_COD_CREDIT_CARD_SETTING = "bcs_str_cod_credit_card_setting"
        const val COLUMN_CREATE_DATE = "bcs_dt_create"
        const val COLUMN_ACTIVE = "bcs_num_active"
    }
}