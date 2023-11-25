package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.TagsQuoteCreditCardDB

object TagsQuoteCreditCardQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${TagsQuoteCreditCardDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${TagsQuoteCreditCardDB.Entry.COLUMN_CODE_TAG} INTEGER,
            ${TagsQuoteCreditCardDB.Entry.COLUMN_CODE_QUOTE_CREDIT_CARD} INTEGER,
            ${TagsQuoteCreditCardDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${TagsQuoteCreditCardDB.Entry.COLUMN_ACTIVE} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_05_03_040
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TagsQuoteCreditCardDB.Entry.TABLE_NAME}"
    val SQL_ALTER = emptyMap<String,String>()
}