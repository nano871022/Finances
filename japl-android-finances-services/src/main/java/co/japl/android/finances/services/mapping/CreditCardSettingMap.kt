package co.japl.android.finances.services.mapping

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.CreditCardSettingDB
import co.japl.android.finances.services.dto.CreditCardSettingDTO
import co.japl.android.finances.services.utils.DateUtils
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
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.create))

        }
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):Optional<CreditCardSettingDTO>{
        if(cursor.columnCount == 7) {
            val codeCreditCard = cursor.getInt(cursor.getColumnIndex(CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD))
            val name = cursor.getString(cursor.getColumnIndex(CreditCardSettingDB.CreditCardEntry.COLUMN_NAME))
            val value = cursor.getString(cursor.getColumnIndex(CreditCardSettingDB.CreditCardEntry.COLUMN_VALUE))
            val type = cursor.getString(cursor.getColumnIndex(CreditCardSettingDB.CreditCardEntry.COLUMN_TYPE))
            val active = cursor.getShort(cursor.getColumnIndex(CreditCardSettingDB.CreditCardEntry.COLUMN_ACTIVE))
            val createDate = DateUtils.toLocalDateTime(cursor.getString(cursor.getColumnIndex(CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE)))
            val id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID)).toInt()
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

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_NAME, crsor.getString(1))
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_VALUE, crsor.getString(2))
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_TYPE, crsor.getString(3))
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_ACTIVE, crsor.getShort(4))
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD, crsor.getInt(5))
            put(CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE, crsor.getString(6))
        }
    }
}