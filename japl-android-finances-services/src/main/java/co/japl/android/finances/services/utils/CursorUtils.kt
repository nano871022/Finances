package co.japl.android.finances.services.utils

import android.database.Cursor
import android.util.Log
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getShortOrNull
import androidx.core.database.getStringOrNull
import co.japl.android.finances.services.dto.CreditCardBoughtDB
import java.math.BigDecimal
import java.time.LocalDateTime

object CursorUtils {

    fun getString(cursor: Cursor, nameColumn:String?):String?{
        return cursor.getColumnIndex(nameColumn).let { cursor.getStringOrNull(it) }
    }
    fun getDouble(cursor: Cursor, nameColumn:String?):Double?{
        return cursor.getColumnIndex(nameColumn).let { cursor.getDoubleOrNull(it) }
    }

    fun getBigDecimal(cursor: Cursor, nameColumn:String?): BigDecimal?{
        return cursor.getColumnIndex(nameColumn).let { cursor.getStringOrNull(it)?.toBigDecimal() }
    }
    fun getInt(cursor: Cursor, nameColumn:String?): Int?{
        return cursor.getColumnIndex(nameColumn).let { cursor.getIntOrNull(it) }
    }

    fun getShort(cursor: Cursor, nameColumn:String?): Short?{
        return cursor.getColumnIndex(nameColumn).let { cursor.getShortOrNull(it) }
    }

    fun getLocalDateTime(cursor: Cursor, nameColumn:String?):LocalDateTime?{
        return cursor.getColumnIndex(nameColumn).let {
            val date = cursor.getString(it)
            DateUtils.toLocalDateTime(date,DateUtils.DEFAULT_DATE_TIME)
        }
    }
}