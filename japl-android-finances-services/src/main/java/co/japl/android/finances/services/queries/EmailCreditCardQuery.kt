package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.entities.EmailCreditCardDB

object EmailCreditCardQuery {

    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${EmailCreditCardDB.Entry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${EmailCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD} INTEGER,
            ${EmailCreditCardDB.Entry.COLUMN_SENDER} TEXT,
            ${EmailCreditCardDB.Entry.COLUMN_SUBJECT_PATTERN} TEXT,
            ${EmailCreditCardDB.Entry.COLUMN_BODY_PATTERN} TEXT,
            ${EmailCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE} INTEGER,
            ${EmailCreditCardDB.Entry.COLUMN_CREATE_DATE} DATE,
            ${EmailCreditCardDB.Entry.COLUMN_ACTIVE} INTEGER
        )
    """

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${EmailCreditCardDB.Entry.TABLE_NAME}"

    val SQL_ALTER = mapOf<String, List<String>>()
}
