package co.japl.android.finances.services.queries

import android.provider.BaseColumns
import co.japl.android.finances.services.dto.CalcDB

object CalculationQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE ${CalcDB.CalcEntry.TABLE_NAME}(
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${CalcDB.CalcEntry.COLUMN_ALIAS} TEXT,
        ${CalcDB.CalcEntry.COLUMN_TYPE} TEXT,
        ${CalcDB.CalcEntry.COLUMN_INTEREST} DOUBLE,
        ${CalcDB.CalcEntry.COLUMN_PERIOD} SHORT,
        ${CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT} NUMBER,
        ${CalcDB.CalcEntry.COLUMN_VALUE_CREDIT} NUMBER,
        ${CalcDB.CalcEntry.COLUMN_INTEREST_VALUE} NUMBER,
        ${CalcDB.CalcEntry.COLUMN_CAPITAL_VALUE} NUMBER,
        ${CalcDB.CalcEntry.COLUMN_KIND_OF_TAX} TEXT DEFAULT "EM" NOT NULL
        )"""
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CalcDB.CalcEntry.TABLE_NAME}"
    val SQL_ALTER = mapOf("27" to "ALTER TABLE ${CalcDB.CalcEntry.TABLE_NAME} ADD ${CalcDB.CalcEntry.COLUMN_KIND_OF_TAX} SHORT DEFAULT \"EM\" NOT NULL")

}