package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.interfaces.IMapper
import co.japl.android.finances.services.dto.TagDB
import co.japl.android.finances.services.dto.TagDTO
import co.japl.android.finances.services.utils.DateUtils

class TagMap : IMapper<TagDTO>{
    @RequiresApi(Build.VERSION_CODES.O)
   override fun mapping(dto:TagDTO ):ContentValues{
        return ContentValues().apply {
            put(TagDB.Entry.COLUMN_NAME,dto.name)
            put(TagDB.Entry.COLUMN_DATE_CREATE,DateUtils.localDateToStringDate(dto.create))
            put(TagDB.Entry.COLUMN_ACTIVE,if(dto.active) 1 else 0)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapping(cursor:Cursor):TagDTO{
        val id = cursor.getString(0).toInt()
        val name = cursor.getString(1)
        val createDate = DateUtils.toLocalDate(cursor.getString(2))
        val active = cursor.getShort(3)
        return  TagDTO(
            id = id,
            name = name,
            create = createDate,
            active = active.toInt() == 1
        )
    }

}