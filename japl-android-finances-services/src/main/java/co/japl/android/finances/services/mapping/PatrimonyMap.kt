package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.japl.android.finances.services.dto.PatrimonyDB
import co.japl.android.finances.services.dto.PatrimonyDTO

object PatrimonyMap {

    fun mapping(asset: PatrimonyDTO):ContentValues{
        return ContentValues().apply {
            put(PatrimonyDB.PatrimonyEntry.COLUMN_NAME, asset.name)
            put(PatrimonyDB.PatrimonyEntry.COLUMN_VALUE, asset.value.toPlainString())
            put(PatrimonyDB.PatrimonyEntry.COLUMN_TYPE, asset.type)
        }
    }

    fun mapping(cursor: Cursor): PatrimonyDTO{
        return PatrimonyDTO(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_NAME)),
            value = cursor.getDouble(cursor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_VALUE)).toBigDecimal(),
            type = cursor.getString(cursor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_TYPE))
        )
    }

    fun restore(crsor: Cursor):ContentValues{
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(crsor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_ID)))
            put(PatrimonyDB.PatrimonyEntry.COLUMN_NAME, crsor.getString(crsor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_NAME)))
            put(PatrimonyDB.PatrimonyEntry.COLUMN_VALUE, crsor.getDouble(crsor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_VALUE)))
            put(PatrimonyDB.PatrimonyEntry.COLUMN_TYPE, crsor.getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_TYPE))
        }
    }
}