package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.ExtraValueAmortizationCreditDB
import co.japl.android.finances.services.dto.ExtraValueAmortizationCreditDTO
import co.japl.android.finances.services.utils.DateUtils

class ExtraValueAmortizationCreditMap {
     @RequiresApi(Build.VERSION_CODES.O)
     fun mapping(cursor: Cursor): ExtraValueAmortizationCreditDTO {
         val id = cursor.getInt(0)
         val code = cursor.getInt(1)
         val nbrQuote = cursor.getLong(2)
         val value = cursor.getDouble(3)
         val create = DateUtils.toLocalDate(cursor.getString(4))
         return ExtraValueAmortizationCreditDTO(id, create, code, nbrQuote, value)
     }

     @RequiresApi(Build.VERSION_CODES.O)
     fun mapping(dto: ExtraValueAmortizationCreditDTO): ContentValues {
         return ContentValues().apply {
             put(ExtraValueAmortizationCreditDB.Entry.COLUMN_CODE, dto.code)
             put(ExtraValueAmortizationCreditDB.Entry.COLUMN_NBR_QUOTE, dto.nbrQuote)
             put(ExtraValueAmortizationCreditDB.Entry.COLUMN_VALUE, dto.value)
             put(
                 ExtraValueAmortizationCreditDB.Entry.COLUMN_DATE_CREATE,
                 DateUtils.localDateToStringDate(dto.create)
             )
         }
     }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(ExtraValueAmortizationCreditDB.Entry.COLUMN_CODE, crsor.getInt(1))
            put(ExtraValueAmortizationCreditDB.Entry.COLUMN_NBR_QUOTE, crsor.getLong(2))
            put(ExtraValueAmortizationCreditDB.Entry.COLUMN_VALUE, crsor.getDouble(3))
            put(ExtraValueAmortizationCreditDB.Entry.COLUMN_DATE_CREATE, crsor.getString(4))
        }
    }
}