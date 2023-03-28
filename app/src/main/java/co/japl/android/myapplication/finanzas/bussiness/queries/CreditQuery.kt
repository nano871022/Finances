package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB

object CreditQuery {
    const val DATA_BASE_VERSION_MINUS_SETTING = 27
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${CreditDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${CreditDB.Entry.COLUMN_NAME} TEXT,
            ${CreditDB.Entry.COLUMN_VALUE} NUMBER,
            ${CreditDB.Entry.COLUMN_TAX} DOUBLE,
            ${CreditDB.Entry.COLUMN_PERIODS} INTEGER,
            ${CreditDB.Entry.COLUMN_QUOTE} NUMBER,
            ${CreditDB.Entry.COLUMN_KIND_OF} TEXT,
            ${CreditDB.Entry.COLUMN_KIND_OF_TAX} TEXT,
            ${CreditDB.Entry.COLUMN_DATE} DATE
        )
    """
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CreditDB.Entry.TABLE_NAME}"
}