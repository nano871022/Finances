package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.GracePeriodDB

object GracePeriodQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${GracePeriodDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${GracePeriodDB.Entry.COLUMN_DATE_CREATE} DATE NOT NULL,
            ${GracePeriodDB.Entry.COLUMN_DATE_END} DATE NOT NULL,
            ${GracePeriodDB.Entry.COLUMN_CODE_CREDIT} INTEGER NOT NULL,
            ${GracePeriodDB.Entry.COLUMN_PERIODS} NUMBER NOT NULL
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_04_09_033
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${GracePeriodDB.Entry.TABLE_NAME}"
}