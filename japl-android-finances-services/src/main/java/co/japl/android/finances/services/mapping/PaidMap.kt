package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
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
        val endDate = cursor.getString(6)?.let { DateUtils.toLocalDate(it) } ?: LocalDate.of(9999,12,31)
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
            val endDate = LocalDate.of(9999,12,31)
            if(dto.id == 0 && dto.recurrent.toInt() == 1) {
                put(PaidDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(endDate))
            }else if(dto.id == 0 && dto.recurrent.toInt() == 0) {
                put(PaidDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(dto.date.plusMonths(1).withDayOfMonth(1).minusDays(1)))
            }else if(dto.date == dto.end && dto.recurrent.toInt() == 1) {
                put(PaidDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(endDate))
            }else if(dto.date == dto.end && dto.recurrent.toInt() == 0) {
                put(PaidDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(dto.end.plusMonths(1).withMonth(1).minusDays(1)))
            } else{
                put(PaidDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(dto.end))
            }
        }
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(PaidDB.Entry.COLUMN_NAME, crsor.getString(3))
            put(PaidDB.Entry.COLUMN_VALUE, crsor.getString(4).toDouble())
            put(PaidDB.Entry.COLUMN_ACCOUNT, crsor.getString(2))
            put(PaidDB.Entry.COLUMN_DATE_PAID, crsor.getString(1))
            put(PaidDB.Entry.COLUMN_RECURRENT, crsor.getInt(5))
            put(PaidDB.Entry.COLUMN_END_DATE, crsor.getString(6))
        }
    }
}