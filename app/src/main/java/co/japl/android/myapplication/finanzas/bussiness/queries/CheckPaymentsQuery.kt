package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.*

object CheckPaymentsQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${CheckPaymentsDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${CheckPaymentsDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${CheckPaymentsDB.Entry.COLUMN_COD_PAID} INTEGER,
            ${CheckPaymentsDB.Entry.COLUMN_PERIOD} TEXT,
            ${CheckPaymentsDB.Entry.COLUMN_CHECK} SHORT
        )
    """
    const val DATA_BASE_VERSION_MINUS = 3_04_03_026
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CheckPaymentsDB.Entry.TABLE_NAME}"
}