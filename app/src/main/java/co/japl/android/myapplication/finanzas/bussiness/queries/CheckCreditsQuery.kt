package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.*

object CheckCreditsQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${CheckCreditDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${CheckCreditDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${CheckCreditDB.Entry.COLUMN_COD_CREDIT} INTEGER,
            ${CheckCreditDB.Entry.COLUMN_PERIOD} TEXT,
            ${CheckCreditDB.Entry.COLUMN_CHECK} SHORT
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_04_12_036
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CheckCreditDB.Entry.TABLE_NAME}"
}