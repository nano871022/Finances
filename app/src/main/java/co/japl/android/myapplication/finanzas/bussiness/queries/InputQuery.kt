package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDB

object InputQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${InputDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${InputDB.Entry.COLUMN_DATE_INPUT} DATE,
            ${InputDB.Entry.COLUMN_ACCOUNT_CODE} INTEGER,
            ${InputDB.Entry.COLUMN_KIND_OF} TEXT,
            ${InputDB.Entry.COLUMN_NAME} TEXT,
            ${InputDB.Entry.COLUMN_VALUE} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 28
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${InputDB.Entry.TABLE_NAME}"
}