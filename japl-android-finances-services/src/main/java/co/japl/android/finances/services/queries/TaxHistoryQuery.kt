package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.dto.PatrimonyDB
import co.japl.android.finances.services.dto.TaxHistoryDB

object TaxHistoryQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE IF NOT EXISTS ${TaxHistoryDB.TaxHistoryEntry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE} DATE,
            ${TaxHistoryDB.TaxHistoryEntry.COLUMN_TAX_VALUE_COP} NUMBER,
            ${TaxHistoryDB.TaxHistoryEntry.COLUMN_FISCAL_YEAR} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_08_01_175
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TaxHistoryDB.TaxHistoryEntry.TABLE_NAME}"

}