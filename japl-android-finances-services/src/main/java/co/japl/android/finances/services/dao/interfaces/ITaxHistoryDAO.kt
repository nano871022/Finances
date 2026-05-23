package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.TaxHistoryDTO
import android.database.sqlite.SQLiteOpenHelper

interface ITaxHistoryDAO {
    var dbConnect: SQLiteOpenHelper
    fun save(history: TaxHistoryDTO): Long
    fun getAll(): List<TaxHistoryDTO>
}
