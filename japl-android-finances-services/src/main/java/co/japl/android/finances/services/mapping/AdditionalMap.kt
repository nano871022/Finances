package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.AdditionalCreditDB
import co.japl.android.finances.services.dto.AdditionalCreditDTO
import co.japl.android.finances.services.utils.DateUtils

class AdditionalMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):AdditionalCreditDTO{
        val id  = cursor.getInt(0)
        val name = cursor.getString(1)
        val value = cursor.getString(2).toBigDecimal()
        val creditCode = cursor.getLong(3)
        val startDate = DateUtils.toLocalDate(cursor.getString(4))
        val endDate = DateUtils.toLocalDate(cursor.getString(5))
        return AdditionalCreditDTO(id,name,value,creditCode,startDate,endDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:AdditionalCreditDTO):ContentValues{
        return ContentValues().apply {
            put(AdditionalCreditDB.Entry.COLUMN_NAME,dto.name)
            put(AdditionalCreditDB.Entry.COLUMN_VALUE,dto.value.toDouble())
            put(AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE,dto.creditCode)
            put(AdditionalCreditDB.Entry.COLUMN_START_DATE,DateUtils.localDateToString(dto.startDate))
            put(AdditionalCreditDB.Entry.COLUMN_END_DATE,DateUtils.localDateToString(dto.endDate))
        }
    }
}