package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.CreditDB
import co.japl.android.finances.services.dto.CreditDTO
import co.japl.android.finances.services.utils.DateUtils

class CreditMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):CreditDTO{
        val id  = cursor.getInt(0)
        val kindOf = cursor.getString(1)
        val name = cursor.getString(2)
        val value = cursor.getString(3).toBigDecimal()
        val periods = cursor.getInt(4)
        val taxs = cursor.getDouble(5)
        val quote = cursor.getString(6).toBigDecimal()
        val date = DateUtils.toLocalDate(cursor.getString(7))
        val kindOfTax = cursor.getString(8)
        return CreditDTO(id,name,date,taxs,periods,value,quote,kindOf,kindOfTax)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditDTO):ContentValues{
        return ContentValues().apply {
            put(CreditDB.Entry.COLUMN_NAME,dto.name)
            put(CreditDB.Entry.COLUMN_VALUE,dto.value.toDouble())
            put(CreditDB.Entry.COLUMN_KIND_OF,dto.kindOf)
            put(CreditDB.Entry.COLUMN_DATE,DateUtils.localDateToString(dto.date))
            put(CreditDB.Entry.COLUMN_QUOTE,dto.quoteValue.toDouble())
            put(CreditDB.Entry.COLUMN_PERIODS,dto.periods)
            put(CreditDB.Entry.COLUMN_TAX,dto.tax)
            put(CreditDB.Entry.COLUMN_KIND_OF,dto.kindOf)
            put(CreditDB.Entry.COLUMN_KIND_OF_TAX,dto.kindOfTax)
        }
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(CreditDB.Entry.COLUMN_NAME, crsor.getString(1))
            put(CreditDB.Entry.COLUMN_VALUE, crsor.getString(2).toDouble())
            put(CreditDB.Entry.COLUMN_KIND_OF, crsor.getString(3))
            put(CreditDB.Entry.COLUMN_DATE, crsor.getString(4))
            put(CreditDB.Entry.COLUMN_QUOTE, crsor.getString(5).toDouble())
            put(CreditDB.Entry.COLUMN_PERIODS, crsor.getInt(6))
            put(CreditDB.Entry.COLUMN_TAX, crsor.getDouble(7))
            put(CreditDB.Entry.COLUMN_KIND_OF, crsor.getString(8))
            put(CreditDB.Entry.COLUMN_KIND_OF_TAX, crsor.getString(9))
        }
    }
}