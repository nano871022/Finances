package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.japl.android.finances.services.dto.CalcDB

class CalculationMap {

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(CalcDB.CalcEntry.COLUMN_ALIAS, crsor.getString(1))
            put(CalcDB.CalcEntry.COLUMN_TYPE, crsor.getString(2))
            put(CalcDB.CalcEntry.COLUMN_INTEREST, crsor.getDouble(3))
            put(CalcDB.CalcEntry.COLUMN_PERIOD, crsor.getShort(4))
            put(CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT, crsor.getDouble(5))
            put(CalcDB.CalcEntry.COLUMN_VALUE_CREDIT, crsor.getDouble(6))
            put(CalcDB.CalcEntry.COLUMN_INTEREST_VALUE, crsor.getDouble(7))
            put(CalcDB.CalcEntry.COLUMN_CAPITAL_VALUE, crsor.getDouble(8))
            put(CalcDB.CalcEntry.COLUMN_KIND_OF_TAX, crsor.getString(9))
        }
    }
}