package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.japl.android.finances.services.dto.PatrimonyDB
import co.japl.android.finances.services.dto.TaxHistoryDB
import co.japl.android.finances.services.dto.TaxHistoryDTO
import java.time.LocalDate

object TaxHistoryMap{

    fun mapping(history: TaxHistoryDTO): ContentValues{
        return   ContentValues().apply {
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_FISCAL_YEAR, history.fiscalYear)
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_TAX_VALUE_COP, history.taxValueCOP.toPlainString())
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE, history.date.toString())
        }
    }

    fun mapping(cursor: Cursor): TaxHistoryDTO{
        return TaxHistoryDTO(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_ID)),
            fiscalYear = cursor.getInt(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_FISCAL_YEAR)),
            taxValueCOP = cursor.getDouble(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_TAX_VALUE_COP)).toBigDecimal(),
            date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE)))
        )
    }

    fun restore(cursor: Cursor): ContentValues{
        return   ContentValues().apply {
            put(BaseColumns._ID, cursor.getLong(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_ID)))
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_FISCAL_YEAR, cursor.getInt(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_FISCAL_YEAR)))
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_TAX_VALUE_COP, cursor.getDouble(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_TAX_VALUE_COP)))
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE)))
        }
    }
}