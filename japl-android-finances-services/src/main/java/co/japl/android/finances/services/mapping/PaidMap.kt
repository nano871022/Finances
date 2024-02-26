package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.dto.PaidDTO
import co.japl.android.finances.services.utils.DateUtils
import java.time.LocalDate

class PaidMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):PaidDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val account = cursor.getString(2)
        val name = cursor.getString(3)
        val value = cursor.getString(4).toBigDecimal()
        val recurrent = cursor.getInt(5)
        val endDate = DateUtils.toLocalDate(cursor.getString(6),LocalDate.of(9999,12,31))
        return PaidDTO(id,date,account,name,value,recurrent.toShort(),endDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: PaidDTO): ContentValues {
        return ContentValues().apply {
            put(PaidDB.Entry.COLUMN_NAME,dto.name)
            put(PaidDB.Entry.COLUMN_VALUE,dto.value.toDouble())
            put(PaidDB.Entry.COLUMN_ACCOUNT,dto.account)
            put(PaidDB.Entry.COLUMN_DATE_PAID, DateUtils.localDateToString(dto.date))
            put(PaidDB.Entry.COLUMN_RECURRENT,dto.recurrent)
            put(PaidDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(dto.end))
        }
    }
}