package co.japl.android.myapplication.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDB
import co.japl.android.myapplication.bussiness.DTO.TaxDB

object CreditCardSettingQuery {
    const val SQL_CREATE_ENTRIES = """CREATE TABLE if not exists ${CreditCardSettingDB.CreditCardEntry.TABLE_NAME}(
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD} NUMBER,
        ${CreditCardSettingDB.CreditCardEntry.COLUMN_ACTIVE} SHORT DEFAULT 1,
        ${CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE} DATETIME DEFAULT current_timestamp,
        ${CreditCardSettingDB.CreditCardEntry.COLUMN_NAME} TEXT,
        ${CreditCardSettingDB.CreditCardEntry.COLUMN_VALUE} TEXT,
        ${CreditCardSettingDB.CreditCardEntry.COLUMN_TYPE} TEXT,
        )"""
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CreditCardSettingDB.CreditCardEntry.TABLE_NAME}"
}