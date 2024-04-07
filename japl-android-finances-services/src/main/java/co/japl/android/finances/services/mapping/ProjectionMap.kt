package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils

class ProjectionMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):ProjectionDTO{
        val id  = cursor.getInt(0)
        val name = cursor.getString(1)
        val value = cursor.getString(2).toBigDecimal()
        val type = cursor.getString(3)
        val active = cursor.getShort(4)
        val quote = cursor.getString(5).toBigDecimal()
        val start = DateUtils.toLocalDate(cursor.getString(6))
        val end = DateUtils.toLocalDate(cursor.getString(7))
        return ProjectionDTO(id,start,end,name,type,value,quote,active)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: ProjectionDTO): ContentValues {
        return ContentValues().apply {
            put(ProjectionDB.Entry.COLUMN_NAME,dto.name)
            put(ProjectionDB.Entry.COLUMN_TYPE,dto.type)
            put(ProjectionDB.Entry.COLUMN_ACTIVE,dto.active)
            put(ProjectionDB.Entry.COLUMN_VALUE,dto.value.toDouble())
            put(ProjectionDB.Entry.COLUMN_QUOTE,dto.quote.toDouble())
            put(ProjectionDB.Entry.COLUMN_DATE_CREATE, DateUtils.localDateToString(dto.create))
            put(ProjectionDB.Entry.COLUMN_DATE_END, DateUtils.localDateToString(dto.end))
        }
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(ProjectionDB.Entry.COLUMN_NAME, crsor.getString(1))
            put(ProjectionDB.Entry.COLUMN_TYPE, crsor.getString(2))
            put(ProjectionDB.Entry.COLUMN_ACTIVE, crsor.getInt(3))
            put(ProjectionDB.Entry.COLUMN_VALUE, crsor.getString(4).toDouble())
            put(ProjectionDB.Entry.COLUMN_QUOTE, crsor.getString(5).toDouble())
            put(ProjectionDB.Entry.COLUMN_DATE_CREATE, crsor.getString(6))
            put(ProjectionDB.Entry.COLUMN_DATE_END, crsor.getString(7))
        }
    }
}