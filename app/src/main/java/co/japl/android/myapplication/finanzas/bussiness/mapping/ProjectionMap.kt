package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate

class ProjectionMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):ProjectionDTO{
        val id  = cursor.getInt(0)
        val name = cursor.getString(1)
        val value = cursor.getString(2).toBigDecimal()
        val type = cursor.getString(3)
        val active = cursor.getShort(4)
        val quote = cursor.getString(5).toBigDecimal()
        var start : LocalDate = LocalDate.now()
        try {
            start = co.japl.android.finances.services.utils.DateUtils.toLocalDate(cursor.getString(6))
        }catch(e:Exception){
            if(e.message?.contains("Invalid date") == false){
                Log.e("Projectionmap",e.message.toString())
            }
        }
        var end: LocalDate = LocalDate.now()
        try{
            co.japl.android.finances.services.utils.DateUtils.toLocalDate(cursor.getString(7))
        }catch(e:Exception){
            if(e.message?.contains("Invalid date") == false){
                Log.e("Projectionmap",e.message.toString())
            }
        }
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
}