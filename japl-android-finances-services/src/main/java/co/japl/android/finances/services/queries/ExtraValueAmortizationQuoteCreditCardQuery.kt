package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.ExtraValueAmortizationQuoteCreditCardDB

object ExtraValueAmortizationQuoteCreditCardQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${ExtraValueAmortizationQuoteCreditCardDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_CODE} INTEGER,
            ${ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_NBR_QUOTE} NUMBER,
            ${ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_VALUE} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_05_01_037
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ExtraValueAmortizationQuoteCreditCardDB.Entry.TABLE_NAME}"
}