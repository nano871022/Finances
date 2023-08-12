package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.*

object CheckQuoteQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${CheckQuoteDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${CheckQuoteDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${CheckQuoteDB.Entry.COLUMN_COD_QUOTE} INTEGER,
            ${CheckQuoteDB.Entry.COLUMN_PERIOD} TEXT,
            ${CheckQuoteDB.Entry.COLUMN_CHECK} SHORT
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_04_12_036
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CheckQuoteDB.Entry.TABLE_NAME}"
}