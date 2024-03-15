package co.japl.android.finances.services.enums

enum class TaxEnum {
    CREDIT_CARD,
    WALLET_BUY,
    CASH_ADVANCE;

    companion object {
        fun findByOrdinal(ordinal:Short):TaxEnum{
            return TaxEnum.values()[ordinal.toInt()]
        }
    }
}