package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.InputDB

object InputQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${InputDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${InputDB.Entry.COLUMN_DATE_INPUT} DATE,
            ${InputDB.Entry.COLUMN_ACCOUNT_CODE} INTEGER,
            ${InputDB.Entry.COLUMN_KIND_OF} TEXT,
            ${InputDB.Entry.COLUMN_NAME} TEXT,
            ${InputDB.Entry.COLUMN_VALUE} NUMBER,
            ${InputDB.Entry.COLUMN_START_DATE} DATE DEFAULT NOW,
            ${InputDB.Entry.COLUMN_END_DATE} DATE  DEFAULT "9999/12/31"
        )
    """
    const val DATA_BASE_VERSION_MINUS = 28
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${InputDB.Entry.TABLE_NAME}"
}