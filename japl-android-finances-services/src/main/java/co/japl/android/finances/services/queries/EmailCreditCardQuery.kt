package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.entities.EmailCreditCardDB

object EmailCreditCardQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE IF NOT EXISTS ${EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SENDER} TEXT,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SUBJECT_PATTERN} TEXT,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_BODY_PATTERN} TEXT,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CODE_CREDIT_CARD} INTEGER,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_NAME_CREDIT_CARD} TEXT,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_KIND_INTEREST_RATE} TEXT,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_ACTIVE} INTEGER,
        ${EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CREATE_DATE} TEXT
    )"""

    const val DATA_BASE_VERSION_MINUS = 4_08_01_168
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME}"
}
