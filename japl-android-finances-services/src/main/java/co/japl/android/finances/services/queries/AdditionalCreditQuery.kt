package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.AdditionalCreditDB

object AdditionalCreditQuery {
    const val DATA_BASE_VERSION_MINUS_SETTING = 27
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${AdditionalCreditDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${AdditionalCreditDB.Entry.COLUMN_NAME} TEXT,
            ${AdditionalCreditDB.Entry.COLUMN_VALUE} NUMBER,
            ${AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE} INTEGER,
            ${AdditionalCreditDB.Entry.COLUMN_START_DATE} DATE,
            ${AdditionalCreditDB.Entry.COLUMN_END_DATE} DATE
        )
    """
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${AdditionalCreditDB.Entry.TABLE_NAME}"
}