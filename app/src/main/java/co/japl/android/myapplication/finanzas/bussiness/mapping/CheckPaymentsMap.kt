package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDateTime

class CheckPaymentsMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):CheckPaymentsDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDateTime(cursor.getString(1))
        val check = cursor.getShort(2)
        val period = cursor.getString(3)
        val codePaid = cursor.getInt(4)
        return CheckPaymentsDTO(id,date,check,period,codePaid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: CheckPaymentsDTO): ContentValues {
        return ContentValues().apply {
            put(CheckPaymentsDB.Entry.COLUMN_COD_PAID,dto.codPaid)
            put(CheckPaymentsDB.Entry.COLUMN_CHECK, dto.check )
            put(CheckPaymentsDB.Entry.COLUMN_DATE_CREATE, DateUtils.localDateTimeToString(dto.date))
            put(CheckPaymentsDB.Entry.COLUMN_PERIOD, dto.period)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapper(pojo:CheckPaymentsPOJO):CheckPaymentsDTO{
        val check = if(pojo.checkValue) 1 else 0
        val id = pojo.id ?: 0
        val date = pojo.date ?: LocalDateTime.now()
        val period = pojo.period ?: "${date.year}${if(date.monthValue <10 )"0"+date.monthValue else date.monthValue}"
        val codPaid = pojo.codPaid ?: 0
        return CheckPaymentsDTO(id.toInt(),date,check.toShort(),period, codPaid.toInt())
    }
}