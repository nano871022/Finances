package co.japl.android.finances.services.mapping

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime

class ProjectionMap {

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):ProjectionDTO{
        val id  = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
        val name = cursor.getString(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_NAME))
        val value = cursor.getString(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_VALUE)).toBigDecimal()
        val type = cursor.getString(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_TYPE))
        val active = cursor.getShort(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_ACTIVE))
        val quote = cursor.getString(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_QUOTE)).toBigDecimal()
        var  start = DateUtils.toLocalDate(cursor.getString(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_DATE_CREATE)))
        var end = DateUtils.toLocalDate(cursor.getString(cursor.getColumnIndex(ProjectionDB.Entry.COLUMN_DATE_END)))
        return ProjectionDTO(
            id=id,
            create=start,
            end=end,
            name=name,
            type=type,
            value=value,
            quote=quote,
            active=active)
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
            put(ProjectionDB.Entry.COLUMN_VALUE, crsor.getString(4).takeIf { it.isNotBlank() && it.isDigitsOnly() }?.toDouble()?:0.0)
            put(ProjectionDB.Entry.COLUMN_QUOTE, crsor.getString(5).toDouble())
            put(ProjectionDB.Entry.COLUMN_DATE_CREATE, crsor.getString(6))
            put(ProjectionDB.Entry.COLUMN_DATE_END, crsor.getString(7))
        }
    }
}