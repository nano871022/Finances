package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.*
import co.japl.android.myapplication.bussiness.interfaces.IMapper
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaxMap : IMapper<TaxDTO>{
    @RequiresApi(Build.VERSION_CODES.O)
   override fun mapping(dto:TaxDTO ):ContentValues{
        return ContentValues().apply {
            put(TaxDB.TaxEntry.COLUMN_TAX,dto.value)
            put(TaxDB.TaxEntry.COLUMN_MONTH,dto.month)
            put(TaxDB.TaxEntry.COLUMN_YEAR,dto.year.toString())
            put(TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD,dto.codCreditCard.toString())
            put(TaxDB.TaxEntry.COLUMN_status,dto.status)
            put(TaxDB.TaxEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.create))

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapping(cursor:Cursor):TaxDTO{
        Log.d(this.javaClass.name,"${cursor.getString(6)}")
        val tax = cursor.getDouble(1)
        val month = cursor.getShort(2)
        val year = cursor.getInt(3)
        val status = cursor.getShort(4)
        val createDate = DateUtils.toLocalDateTime(cursor.getString(6))
        val codCreditCard = cursor.getInt(5)
        val id = cursor.getString(0).toInt()
        return  TaxDTO(
            id,
            month,
            year,
            status,
            codCreditCard,
            createDate,
            tax
        )
    }

}