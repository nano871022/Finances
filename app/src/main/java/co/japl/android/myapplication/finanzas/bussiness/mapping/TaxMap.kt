package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.TaxDB
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IMapper
import co.japl.android.myapplication.utils.DateUtils

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
            put(TaxDB.TaxEntry.COLUMN_KIND,dto.kind)
            put(TaxDB.TaxEntry.COLUMN_PERIOD,dto.period)
            put(TaxDB.TaxEntry.COLUMN_KIND_OF_TAX,dto.kindOfTax)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapping(cursor:Cursor):TaxDTO{
        val tax = cursor.getDouble(1)
        val month = cursor.getShort(2)
        val year = cursor.getInt(3)
        val status = cursor.getShort(4)
        val codCreditCard = cursor.getInt(5)
        val createDate = DateUtils.toLocalDateTime(cursor.getString(6))
        val kind = cursor.getShort(7)
        val period = cursor.getShort(8)
        val kindOfTax = cursor.getString(9)
        val id = cursor.getString(0).toInt()
        return  TaxDTO(
            id,
            month,
            year,
            status,
            codCreditCard,
            createDate,
            tax,
            kind,
            period,
            kindOfTax
        )
    }

}