package co.com.japl.finances.iports.enums

import androidx.annotation.StringRes
import co.com.japl.finances.iports.R

enum class KindInterestRateEnum(private val code:Short,@StringRes private val title:Int) {
    CREDIT_CARD(0, R.string.credit_Card),
    WALLET_BUY(1,R.string.wallet_buy),
    CASH_ADVANCE(2,R.string.cash_advance);


    fun getCode()=code
    fun getName()=title
    companion object {
        fun findByOrdinal(ordinal:Short): KindInterestRateEnum {
            return KindInterestRateEnum.entries.firstOrNull { it.code == ordinal} ?: CREDIT_CARD
        }
    }
}