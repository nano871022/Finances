package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.PaidDB

object PaidQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${PaidDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${PaidDB.Entry.COLUMN_DATE_PAID} DATE,
            ${PaidDB.Entry.COLUMN_ACCOUNT} TEXT,
            ${PaidDB.Entry.COLUMN_NAME} TEXT,
            ${PaidDB.Entry.COLUMN_VALUE} NUMBER,
            ${PaidDB.Entry.COLUMN_RECURRENT} NUMBER,
            ${PaidDB.Entry.COLUMN_END_DATE} DATE NOT NULL DEFAULT '9999-12-31'
        )
    """
    const val DATA_BASE_VERSION_MINUS = 28
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PaidDB.Entry.TABLE_NAME}"
    val SQL_ALTER_TABLE = mapOf(
        "40407031" to listOf("ALTER TABLE ${PaidDB.Entry.TABLE_NAME} ADD COLUMN ${PaidDB.Entry.COLUMN_END_DATE} DATE NOT NULL DEFAULT '9999-12-31'")
    )
}