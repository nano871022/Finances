package co.japl.android.myapplication.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB

object CreditCardQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE ${CreditCardDB.CreditCardEntry.TABLE_NAME}(
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${CreditCardDB.CreditCardEntry.COLUMN_NAME} TEXT,
        ${CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES} SHORT,
        ${CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY} SHORT,
        ${CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE} DOUBLE,
        ${CreditCardDB.CreditCardEntry.COLUMN_STATUS} SHORT,
        ${CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q} SHORT,
        ${CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ} SHORT,
        ${CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE} DATE
        )"""
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CreditCardDB.CreditCardEntry.TABLE_NAME}"
    val SQL_ALTER = mapOf(
        "40406030" to listOf(
            "ALTER TABLE ${CreditCardDB.CreditCardEntry.TABLE_NAME} ADD ${CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES} SHORT DEFAULT 36",
            "ALTER TABLE ${CreditCardDB.CreditCardEntry.TABLE_NAME} ADD ${CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ} SHORT DEFAULT 0",
            "ALTER TABLE ${CreditCardDB.CreditCardEntry.TABLE_NAME} ADD ${CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q} SHORT DEFAULT 0"
        )
    )
}