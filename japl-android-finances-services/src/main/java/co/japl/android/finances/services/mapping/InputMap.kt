package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.R
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils
import java.time.LocalDate
import javax.inject.Inject

class InputMap @Inject constructor(context:Context?) {
    private val monthly = context?.resources?.getStringArray(R.array.kind_of_pay_list)?.get(0) ?:""

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):InputDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val accountCode = cursor.getInt(2)
        val kindof = cursor.getString(3) ?: monthly
        val name = cursor.getString(4)
        val value = cursor.getString(5).toBigDecimal()
        val startCol = cursor.getString(6)
        val endCol = cursor.getString(7)
        val start = if(startCol == "NOW") LocalDate.now() else DateUtils.toLocalDate(startCol)
        val end  = if(endCol == "9999/12/31") LocalDate.MAX  else DateUtils.toLocalDate(endCol)
        return InputDTO(id,date,accountCode,kindof,name,value,start,end)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: InputDTO): ContentValues {
        return ContentValues().apply {
            put(InputDB.Entry.COLUMN_NAME,dto.name)
            put(InputDB.Entry.COLUMN_ACCOUNT_CODE,dto.accountCode)
            put(InputDB.Entry.COLUMN_KIND_OF,dto.kindOf)
            put(InputDB.Entry.COLUMN_VALUE,dto.value.toDouble())
            put(InputDB.Entry.COLUMN_DATE_INPUT, DateUtils.localDateToString(dto.date))
            put(InputDB.Entry.COLUMN_END_DATE, DateUtils.localDateToString(dto.dateEnd))
        }
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(InputDB.Entry.COLUMN_DATE_INPUT, crsor.getString(1))
            put(InputDB.Entry.COLUMN_NAME, crsor.getString(4))
            put(InputDB.Entry.COLUMN_ACCOUNT_CODE, crsor.getInt(2))
            put(InputDB.Entry.COLUMN_KIND_OF, crsor.getString(3))
            put(InputDB.Entry.COLUMN_VALUE, crsor.getString(5).toDouble())
            put(InputDB.Entry.COLUMN_START_DATE, crsor.getString(6))
            put(InputDB.Entry.COLUMN_END_DATE, crsor.getString(7))
        }
    }
}