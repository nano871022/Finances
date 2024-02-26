package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.*

object ProjectionsQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${ProjectionDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${ProjectionDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${ProjectionDB.Entry.COLUMN_DATE_END} DATE,
            ${ProjectionDB.Entry.COLUMN_NAME} TEXT,
            ${ProjectionDB.Entry.COLUMN_TYPE} TEXT,
            ${ProjectionDB.Entry.COLUMN_VALUE} NUMBER,
            ${ProjectionDB.Entry.COLUMN_QUOTE} NUMBER,
            ${ProjectionDB.Entry.COLUMN_ACTIVE} SHORT
        )
    """
    const val DATA_BASE_VERSION_MINUS = 3_02_01_023
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ProjectionDB.Entry.TABLE_NAME}"
}