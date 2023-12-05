package co.com.japl.finances.iports.enums

enum class KindInterestRateEnum(private val code:Short) {
    CREDIT_CARD(0),
    CASH_ADVANCE(2),
    WALLET_BUY(1);

    companion object {
        fun findByOrdinal(ordinal:Short): KindInterestRateEnum {
            return KindInterestRateEnum.values().firstOrNull { it.code == ordinal} ?: CREDIT_CARD
        }
    }
}