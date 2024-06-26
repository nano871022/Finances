package co.japl.android.myapplication.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDB
import co.japl.android.myapplication.bussiness.DTO.TaxDB

object BuyCreditCardSettingQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE if not exists ${BuyCreditCardSettingDB.Entry.TABLE_NAME}(
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${BuyCreditCardSettingDB.Entry.COLUMN_COD_CREDIT_CARD_SETTING} NUMBER,
        ${BuyCreditCardSettingDB.Entry.COLUMN_COD_BUY_CREDIT_CARD} NUMBER,
        ${BuyCreditCardSettingDB.Entry.COLUMN_ACTIVE} SHORT DEFAULT 1,
        ${BuyCreditCardSettingDB.Entry.COLUMN_CREATE_DATE} DATETIME DEFAULT current_timestamp
        )"""
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${BuyCreditCardSettingDB.Entry.TABLE_NAME}"
}