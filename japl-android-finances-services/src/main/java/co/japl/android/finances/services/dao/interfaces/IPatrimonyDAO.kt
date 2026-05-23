package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.PatrimonyDTO
import android.database.sqlite.SQLiteOpenHelper

interface IPatrimonyDAO {
    var dbConnect: SQLiteOpenHelper
    fun save(asset: PatrimonyDTO): Long
    fun getAll(): List<PatrimonyDTO>
    fun delete(id: Long): Boolean
}
