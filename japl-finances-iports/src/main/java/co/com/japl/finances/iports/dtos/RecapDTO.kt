package co.com.japl.finances.iports.dtos

data class RecapDTO(
    val projectionSaved:Double,
    val projectionNext:Double,
    val totalPaid:Double,
    val totalQuoteCredit:Double,
    val totalInputs:Double,
    val totalQuoteCreditCard:Double,
    val warningValueCreditCard:Double
)
