package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.dto.CheckPaymentsPOJO
import co.japl.android.finances.services.utils.DateUtils
import java.time.LocalDateTime

class CheckQuoteMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):CheckQuoteDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDateTime(cursor.getString(1))
        val check = cursor.getShort(2)
        val period = cursor.getString(3)
        val codePaid = cursor.getInt(4)
        return CheckQuoteDTO(id,date,check,period,codePaid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: CheckQuoteDTO): ContentValues {
        return ContentValues().apply {
        put(CheckQuoteDB.Entry.COLUMN_COD_QUOTE,dto.codQuote)
            put(CheckQuoteDB.Entry.COLUMN_CHECK, dto.check )
            put(CheckQuoteDB.Entry.COLUMN_DATE_CREATE, DateUtils.localDateTimeToString(dto.date))
            put(CheckQuoteDB.Entry.COLUMN_PERIOD, dto.period)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapper(pojo:CheckPaymentsPOJO):CheckQuoteDTO{
        val check = if(pojo.checkValue) 1 else 0
        val id = pojo.id ?: 0
        val date = pojo.date ?: LocalDateTime.now()
        val period = pojo.period ?: "${date.year}${if(date.monthValue <10 )"0"+date.monthValue else date.monthValue}"
        val codPaid = pojo.codPaid ?: 0
        return CheckQuoteDTO(id.toInt(),date,check.toShort(),period, codPaid.toInt())
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(CheckQuoteDB.Entry.COLUMN_CHECK, crsor.getInt(1))
            put(CheckQuoteDB.Entry.COLUMN_DATE_CREATE, crsor.getString(2))
            put(CheckQuoteDB.Entry.COLUMN_PERIOD, crsor.getString(3))
            put(CheckQuoteDB.Entry.COLUMN_COD_QUOTE, crsor.getInt(4))
        }
    }
}