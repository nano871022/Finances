package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.utils.DateUtils

class CreditCardSettingMap {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditCardSettingDTO ):ContentValues{
        return ContentValues().apply {
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_NAME,dto.name)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_VALUE,dto.value)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_TYPE,dto.type)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_ACTIVE,dto.active)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD,dto.codeCreditCard)
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.create))

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):CreditCardSettingDTO{
        var codeCreditCard = cursor.getInt(1)
        val name = cursor.getString(2)
        val value = cursor.getString(3)
        val type = cursor.getString(4)
        val active = cursor.getShort(5)
        val createDate = DateUtils.toLocalDateTime(cursor.getString(6))
        val id = cursor.getString(0).toInt()
        return  CreditCardSettingDTO(
            id,
            codeCreditCard,
            name,
            value,
            type,
            createDate,
            active
        )
    }

}