package co.japl.android.finances.services.enums

enum class TaxEnum {
    CREDIT_CARD,
    CASH_ADVANCE,
    WALLET_BUY;

    companion object {
        fun findByOrdinal(ordinal:Short):TaxEnum{
            return TaxEnum.values()[ordinal.toInt()]
        }
    }
}