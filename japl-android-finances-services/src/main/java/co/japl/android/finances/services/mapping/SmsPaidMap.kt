package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.japl.android.finances.services.entities.SmsPaid
import co.japl.android.finances.services.entities.SmsPaidDB
import co.japl.android.finances.services.utils.DateUtils

class SmsPaidMap {

    fun mapping(cursor:Cursor):SmsPaid{
        return SmsPaid(
            id= cursor.getInt(0),
            codeAccount = cursor.getInt(1),
            nameAccount = "",
            phoneNumber = cursor.getString(2),
            pattern = cursor.getString(3),
            active = cursor.getInt(5) == 1,
            create = DateUtils.toLocalDateTime(cursor.getString(4))
        )
    }

    fun mapping(dto:SmsPaid):ContentValues{
        return ContentValues().apply {
            put(SmsPaidDB.Entry.COLUMN_CODE_ACCOUNT,dto.codeAccount)
            put(SmsPaidDB.Entry.COLUMN_PHONE_NUMBER,dto.phoneNumber)
            put(SmsPaidDB.Entry.COLUMN_PATTERN,dto.pattern)
            put(SmsPaidDB.Entry.COLUMN_ACTIVE,if(dto.active!!) 1 else 0)
            put(SmsPaidDB.Entry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToStringDB(dto.create!!))
        }
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(SmsPaidDB.Entry.COLUMN_CODE_ACCOUNT, crsor.getInt(1))
            put(SmsPaidDB.Entry.COLUMN_PHONE_NUMBER, crsor.getString(2))
            put(SmsPaidDB.Entry.COLUMN_PATTERN, crsor.getString(3))
            put(SmsPaidDB.Entry.COLUMN_ACTIVE, crsor.getInt(4))
            put(SmsPaidDB.Entry.COLUMN_CREATE_DATE, crsor.getString(5))
        }
    }
}