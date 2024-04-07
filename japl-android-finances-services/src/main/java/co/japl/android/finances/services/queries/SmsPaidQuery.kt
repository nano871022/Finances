package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.entities.SmsPaidDB

object SmsPaidQuery {

    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${SmsPaidDB.Entry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${SmsPaidDB.Entry.COLUMN_CODE_ACCOUNT} INTEGER,
            ${SmsPaidDB.Entry.COLUMN_PHONE_NUMBER} TEXT,
            ${SmsPaidDB.Entry.COLUMN_PATTERN} TEXT,
            ${SmsPaidDB.Entry.COLUMN_ACTIVE} INTEGER,
            ${SmsPaidDB.Entry.COLUMN_CREATE_DATE} DATE
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_05_05_082
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${SmsPaidDB.Entry.TABLE_NAME}"
    /**
     * map for alter table query to modify structure table
     * key = version
     * value = list of alter queries
     * */
    val SQL_ALTER = mapOf<String,List<String>>()


}