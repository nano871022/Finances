package co.japl.android.myapplication.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB

object CreditCardBoughtQuery {
    const val SQL_CREDIT_CARD_CREATE_ENTRIES = """CREATE TABLE ${CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME}(
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} INTEGER,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM} TEXT,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM} DOUBLE,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST} SHORT,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH} NUMBER,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} DATE,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE} DATE,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE} DATE,
        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT} SHORT
        )"""
    const val SQL_CREDIT_CARD_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME}"
}