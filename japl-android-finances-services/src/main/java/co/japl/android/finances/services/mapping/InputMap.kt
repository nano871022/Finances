package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.R
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils
import java.time.LocalDate
import javax.inject.Inject

class InputMap @Inject constructor(context:Context) {
    private val monthly = context.resources.getStringArray(R.array.kind_of_pay_list)[0]

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):InputDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val accountCode = cursor.getInt(2)
        val kindof = cursor.getString(3) ?: monthly
        val name = cursor.getString(4)
        val value = cursor.getString(5).toBigDecimal()
        val start = LocalDate.now()
        val end  = LocalDate.of(9999,12,31)
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
        }
    }
}