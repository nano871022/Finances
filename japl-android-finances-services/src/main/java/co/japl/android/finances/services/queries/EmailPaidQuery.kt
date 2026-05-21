package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.entities.EmailPaidDB

object EmailPaidQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE IF NOT EXISTS ${EmailPaidDB.EmailPaidEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_SENDER} TEXT,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN} TEXT,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN} TEXT,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT} INTEGER,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT} TEXT,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE} INTEGER,
        ${EmailPaidDB.EmailPaidEntry.COLUMN_CREATE_DATE} TEXT
    )"""

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${EmailPaidDB.EmailPaidEntry.TABLE_NAME}"
}
