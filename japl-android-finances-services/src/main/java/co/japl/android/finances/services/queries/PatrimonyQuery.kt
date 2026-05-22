package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.dto.PatrimonyDB

object PatrimonyQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${PatrimonyDB.PatrimonyEntry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${PatrimonyDB.PatrimonyEntry.COLUMN_NAME} TEXT,
            ${PatrimonyDB.PatrimonyEntry.COLUMN_VALUE} NUMBER,
            ${PatrimonyDB.PatrimonyEntry.COLUMN_TYPE} TEXT
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_08_01_174
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PatrimonyDB.PatrimonyEntry.TABLE_NAME}"

}