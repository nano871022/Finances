package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils

class BuyCreditCardSettingMap {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:BuyCreditCardSettingDTO ):ContentValues{
        return ContentValues().apply {
            put(BuyCreditCardSettingDB.Entry.COLUMN_COD_BUY_CREDIT_CARD,dto.codeBuyCreditCard)
            put(BuyCreditCardSettingDB.Entry.COLUMN_COD_CREDIT_CARD_SETTING,dto.codeCreditCardSetting)
            put(BuyCreditCardSettingDB.Entry.COLUMN_ACTIVE,dto.active)
            put(BuyCreditCardSettingDB.Entry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.create))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):BuyCreditCardSettingDTO{
        val codeBuyCreditCard = cursor.getInt(1)
        val codeCreditCardSetting = cursor.getInt(2)
        val active = cursor.getShort(3)
        val createDate = DateUtils.toLocalDateTime(cursor.getString(4))
        val id = cursor.getString(0).toInt()
        return  BuyCreditCardSettingDTO(
            id,
            codeBuyCreditCard,
            codeCreditCardSetting,
            createDate,
            active
        )
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(BuyCreditCardSettingDB.Entry.COLUMN_COD_BUY_CREDIT_CARD, crsor.getInt(1))
            put(BuyCreditCardSettingDB.Entry.COLUMN_COD_CREDIT_CARD_SETTING, crsor.getInt(2))
            put(BuyCreditCardSettingDB.Entry.COLUMN_ACTIVE, crsor.getInt(3))
            put(BuyCreditCardSettingDB.Entry.COLUMN_CREATE_DATE, crsor.getString(4))
        }
    }

}