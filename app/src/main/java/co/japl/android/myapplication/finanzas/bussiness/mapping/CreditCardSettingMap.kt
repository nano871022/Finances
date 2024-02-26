package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.com.japl.ui.utils.DateUtils
import java.util.Optional

class CreditCardSettingMap {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditCardSettingDTO ):ContentValues{
        return ContentValues().apply {
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_NAME,dto.name)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_VALUE,dto.value)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_TYPE,dto.type)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_ACTIVE,dto.active)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD,dto.codeCreditCard)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE, DateUtils.localDateTimeToString(dto.create))

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):Optional<CreditCardSettingDTO>{
        if(cursor.columnCount == 7) {
            val codeCreditCard = cursor.getInt(1)
            val name = cursor.getString(2)
            val value = cursor.getString(3)
            val type = cursor.getString(4)
            val active = cursor.getShort(5)
            val createDate = DateUtils.toLocalDateTime(cursor.getString(6))
            val id = cursor.getString(0).toInt()
            return Optional.of(CreditCardSettingDTO(
                id,
                codeCreditCard,
                name,
                value,
                type,
                createDate,
                active
            ))
        }
        Log.d(this.javaClass.name,"records ${cursor.columnNames.reduce { acc, s -> "$acc, $s" }}")
        return Optional.empty()
    }

}