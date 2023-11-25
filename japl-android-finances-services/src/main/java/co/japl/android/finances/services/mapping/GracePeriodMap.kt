package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils

class GracePeriodMap() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):GracePeriodDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val end = DateUtils.toLocalDate(cursor.getString(2))
        val codeCredit = cursor.getLong(3)
        val periods = cursor.getShort(4)
        return GracePeriodDTO(id,date,end, codeCredit, periods)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: GracePeriodDTO): ContentValues {
        return ContentValues().apply {
            put(GracePeriodDB.Entry.COLUMN_DATE_CREATE,DateUtils.localDateToStringDate(dto.create))
            put(GracePeriodDB.Entry.COLUMN_DATE_END,DateUtils.localDateToStringDate(dto.end))
            put(GracePeriodDB.Entry.COLUMN_CODE_CREDIT,dto.codeCredit)
            put(GracePeriodDB.Entry.COLUMN_PERIODS,dto.periods)
        }
    }
}