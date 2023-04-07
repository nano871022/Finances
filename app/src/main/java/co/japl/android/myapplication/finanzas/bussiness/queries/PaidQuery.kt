package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDB

object PaidQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${PaidDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${PaidDB.Entry.COLUMN_DATE_PAID} DATE,
            ${PaidDB.Entry.COLUMN_ACCOUNT} TEXT,
            ${PaidDB.Entry.COLUMN_NAME} TEXT,
            ${PaidDB.Entry.COLUMN_VALUE} NUMBER,
            ${PaidDB.Entry.COLUMN_RECURRENT} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 28
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PaidDB.Entry.TABLE_NAME}"
}