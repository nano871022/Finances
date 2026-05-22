package co.japl.android.finances.services.dao.implement

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.finances.services.dao.interfaces.IPatrimonyDAO
import co.japl.android.finances.services.dto.PatrimonyDB
import co.japl.android.finances.services.dto.PatrimonyDTO
import javax.inject.Inject

class PatrimonyImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IPatrimonyDAO {

    override fun save(asset: PatrimonyDTO): Long {
        val db = dbConnect.writableDatabase
        val values = ContentValues().apply {
            put(PatrimonyDB.PatrimonyEntry.COLUMN_NAME, asset.name)
            put(PatrimonyDB.PatrimonyEntry.COLUMN_VALUE, asset.value.toPlainString())
            put(PatrimonyDB.PatrimonyEntry.COLUMN_TYPE, asset.type)
        }
        return if (asset.id != null) {
            db.update(
                PatrimonyDB.PatrimonyEntry.TABLE_NAME,
                values,
                "${PatrimonyDB.PatrimonyEntry.COLUMN_ID} = ?",
                arrayOf(asset.id.toString())
            ).toLong()
        } else {
            db.insert(PatrimonyDB.PatrimonyEntry.TABLE_NAME, null, values)
        }
    }

    override fun getAll(): List<PatrimonyDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(PatrimonyDB.PatrimonyEntry.TABLE_NAME, null, null, null, null, null, null)
        val assets = mutableListOf<PatrimonyDTO>()
        with(cursor) {
            while (moveToNext()) {
                assets.add(
                    PatrimonyDTO(
                        id = getLong(getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_ID)),
                        name = getString(getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_NAME)),
                        value = getString(getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_VALUE)).toBigDecimal(),
                        type = getString(getColumnIndexOrThrow(PatrimonyDB.PatrimonyEntry.COLUMN_TYPE))
                    )
                )
            }
            close()
        }
        return assets
    }

    override fun delete(id: Long): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(
            PatrimonyDB.PatrimonyEntry.TABLE_NAME,
            "${PatrimonyDB.PatrimonyEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        ) > 0
    }
}
