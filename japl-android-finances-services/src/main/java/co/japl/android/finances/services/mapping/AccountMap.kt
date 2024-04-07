package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.utils.DateUtils

class AccountMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):AccountDTO{
        val id  = cursor.getInt(0)
        val create = DateUtils.toLocalDate(cursor.getString(1))
        val name = cursor.getString(2)
        val active = cursor.getInt(3) != 0
        return AccountDTO(id,create,name,active)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: AccountDTO): ContentValues {
        return ContentValues().apply {
            put(AccountDB.Entry.COLUMN_NAME,dto.name)
            put(AccountDB.Entry.COLUMN_ACTIVE,if(dto.active)1 else 0)
            put(AccountDB.Entry.COLUMN_DATE_CREATE, DateUtils.localDateToString(dto.create))
        }
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(AccountDB.Entry.COLUMN_NAME, crsor.getString(2))
            put(AccountDB.Entry.COLUMN_ACTIVE, crsor.getInt(3))
            put(AccountDB.Entry.COLUMN_DATE_CREATE, crsor.getString(1))
        }
    }
}