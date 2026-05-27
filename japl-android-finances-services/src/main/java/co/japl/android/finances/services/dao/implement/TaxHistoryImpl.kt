package co.japl.android.finances.services.dao.implement

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.finances.services.dao.interfaces.ITaxHistoryDAO
import co.japl.android.finances.services.dto.TaxHistoryDB
import co.japl.android.finances.services.dto.TaxHistoryDTO
import co.japl.android.finances.services.mapping.TaxHistoryMap
import java.time.LocalDate
import javax.inject.Inject

class TaxHistoryImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : ITaxHistoryDAO {

    override fun save(history: TaxHistoryDTO): Long {
        val db = dbConnect.writableDatabase
        val values = ContentValues().apply {
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_FISCAL_YEAR, history.fiscalYear)
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_TAX_VALUE_COP, history.taxValueCOP.toPlainString())
            put(TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE, history.date.toString())
        }
        return db.insert(TaxHistoryDB.TaxHistoryEntry.TABLE_NAME, null, values)
    }

    override fun getAll(): List<TaxHistoryDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            TaxHistoryDB.TaxHistoryEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${TaxHistoryDB.TaxHistoryEntry.COLUMN_DATE} DESC"
        )
        val historyList = mutableListOf<TaxHistoryDTO>()
        with(cursor) {
            while (moveToNext()) {
                historyList.add(TaxHistoryMap.mapping(this))
            }
            close()
        }
        return historyList
    }
}
