package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
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

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(GracePeriodDB.Entry.COLUMN_DATE_CREATE, crsor.getString(1))
            put(GracePeriodDB.Entry.COLUMN_DATE_END, crsor.getString(2))
            put(GracePeriodDB.Entry.COLUMN_CODE_CREDIT, crsor.getLong(3))
            put(GracePeriodDB.Entry.COLUMN_PERIODS, crsor.getInt(4))
        }
    }
}