package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

import co.japl.android.finances.services.dto.AddToCapitalCreditDB

class AddToCapitalMap {

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(AddToCapitalCreditDB.Entry.COLUMN_VALUE, crsor.getDouble(1))
            put(AddToCapitalCreditDB.Entry.COLUMN_CREDIT_CODE, crsor.getInt(2))
            put(AddToCapitalCreditDB.Entry.COLUMN_DATE, crsor.getString(3))
        }
    }
}