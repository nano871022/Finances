package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.com.japl.ui.utils.DateUtils
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
}