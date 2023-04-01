package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.utils.DateUtils

class InputMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):InputDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val name = cursor.getString(2)
        val value = cursor.getString(3).toBigDecimal()
        return InputDTO(id,date,name,value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: InputDTO): ContentValues {
        return ContentValues().apply {
            put(InputDB.Entry.COLUMN_NAME,dto.name)
            put(InputDB.Entry.COLUMN_VALUE,dto.value.toDouble())
            put(InputDB.Entry.COLUMN_DATE_INPUT, DateUtils.localDateToString(dto.date))
        }
    }
}