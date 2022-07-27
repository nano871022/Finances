package co.japl.android.myapplication.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.CalcDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB

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
        ${CalcDB.CalcEntry.COLUMN_CAPITAL_VALUE} NUMBER
        )"""
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CalcDB.CalcEntry.TABLE_NAME}"
}