package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.entities.SmsCreditCardDB

object SmsCreditCardQuery {

    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${SmsCreditCardDB.Entry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${SmsCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD} INTEGER,
            ${SmsCreditCardDB.Entry.COLUMN_PHONE_NUMBER} TEXT,
            ${SmsCreditCardDB.Entry.COLUMN_PATTERN} TEXT,
            ${SmsCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE} INTEGER,
            ${SmsCreditCardDB.Entry.COLUMN_ACTIVE} INTEGER,
            ${SmsCreditCardDB.Entry.COLUMN_CREATE_DATE} DATE
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_05_05_076
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${SmsCreditCardDB.Entry.TABLE_NAME}"
    /**
     * map for alter table query to modify structure table
     * key = version
     * value = list of alter queries
     * */
    val SQL_ALTER = mapOf<String,List<String>>()


}