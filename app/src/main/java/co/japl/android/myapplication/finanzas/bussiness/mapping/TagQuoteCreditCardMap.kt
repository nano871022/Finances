package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.IMapper
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagsQuoteCreditCardDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagsQuoteCreditCardDTO
import co.japl.android.myapplication.utils.DateUtils

class TagQuoteCreditCardMap : IMapper<TagsQuoteCreditCardDTO>{
    @RequiresApi(Build.VERSION_CODES.O)
   override fun mapping(dto:TagsQuoteCreditCardDTO ):ContentValues{
        return ContentValues().apply {
            put(TagsQuoteCreditCardDB.Entry.COLUMN_CODE_QUOTE_CREDIT_CARD,dto.codQuote)
            put(TagsQuoteCreditCardDB.Entry.COLUMN_CODE_TAG,dto.codTag)
            put(TagsQuoteCreditCardDB.Entry.COLUMN_DATE_CREATE,DateUtils.localDateToStringDate(dto.create))
            put(TagsQuoteCreditCardDB.Entry.COLUMN_ACTIVE,if(dto.active) 1 else 0)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapping(cursor:Cursor):TagsQuoteCreditCardDTO{
        val id = cursor.getString(0).toInt()
        val codeTag = cursor.getInt(1)
        val codeQuote = cursor.getInt(2)
        val createDate = DateUtils.toLocalDate(cursor.getString(3))
        val active = cursor.getShort(4)
        return  TagsQuoteCreditCardDTO(
            id = id,
            codTag = codeTag,
            codQuote = codeQuote,
            create = createDate,
            active = active.toInt() == 1
        )
    }

}