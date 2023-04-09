package co.japl.android.myapplication.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.TaxDB

object TaxQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE if not exists ${TaxDB.TaxEntry.TABLE_NAME}(
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${TaxDB.TaxEntry.COLUMN_TAX} DOUBLE,
        ${TaxDB.TaxEntry.COLUMN_MONTH} SHORT,
        ${TaxDB.TaxEntry.COLUMN_YEAR} INTEGER,
        ${TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD} INTEGER,
        ${TaxDB.TaxEntry.COLUMN_status} SHORT,
        ${TaxDB.TaxEntry.COLUMN_CREATE_DATE} DATE,
        ${TaxDB.TaxEntry.COLUMN_KIND} SHORT DEFAULT 0,
        ${TaxDB.TaxEntry.COLUMN_PERIOD} SHORT DEFAULT 0,
        ${TaxDB.TaxEntry.COLUMN_KIND_OF_TAX} TEXT DEFAULT "EM"
        )"""
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TaxDB.TaxEntry.TABLE_NAME}"

    val SQL_ALTER = mapOf(
        "28" to "ALTER TABLE ${TaxDB.TaxEntry.TABLE_NAME} ADD ${TaxDB.TaxEntry.COLUMN_KIND_OF_TAX} TEXT DEFAULT \"EM\"",
        "18" to "ALTER TABLE ${TaxDB.TaxEntry.TABLE_NAME} ADD ${TaxDB.TaxEntry.COLUMN_KIND} SHORT DEFAULT 0",
                          "17" to "ALTER TABLE ${TaxDB.TaxEntry.TABLE_NAME} ADD ${TaxDB.TaxEntry.COLUMN_PERIOD} SHORT DEFAULT 0;")

}