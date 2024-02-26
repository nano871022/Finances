package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.AddToCapitalCreditDB

object AddToCapitalQuery {
    const val DATA_BASE_VERSION_MINUS_SETTING = 27
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${AddToCapitalCreditDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${AddToCapitalCreditDB.Entry.COLUMN_VALUE} NUMBER,
            ${AddToCapitalCreditDB.Entry.COLUMN_CREDIT_CODE} INTEGER,
            ${AddToCapitalCreditDB.Entry.COLUMN_DATE} DATE
        )
    """
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${AddToCapitalCreditDB.Entry.TABLE_NAME}"
}