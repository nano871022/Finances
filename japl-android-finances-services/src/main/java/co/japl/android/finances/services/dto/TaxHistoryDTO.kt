package co.japl.android.finances.services.dto

import java.math.BigDecimal
import java.time.LocalDate

data class TaxHistoryDTO(
    val id: Long?,
    val fiscalYear: Int,
    val taxValueCOP: BigDecimal,
    val date: LocalDate
)

object TaxHistoryDB {
    object TaxHistoryEntry {
        const val TABLE_NAME = "dian_tax_declarations_history"
        const val COLUMN_ID = "_id"
        const val COLUMN_FISCAL_YEAR = "fiscal_year"
        const val COLUMN_TAX_VALUE_COP = "tax_value_cop"
        const val COLUMN_DATE = "date"
    }
}
