package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.utils.DateUtils

class InputMap(view:View) {
    private val monthly = view.resources.getStringArray(R.array.kind_of_pay_list)[0]

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):InputDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val accountCode = cursor.getInt(2)
        val kindof = cursor.getString(3) ?: monthly
        val name = cursor.getString(4)
        val value = cursor.getString(5).toBigDecimal()
        return InputDTO(id,date,accountCode,kindof,name,value)
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