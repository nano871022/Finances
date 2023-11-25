package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.AddAmortizationDB
import co.japl.android.finances.services.dto.AddAmortizationDTO
import co.japl.android.finances.services.utils.DateUtils

class AddAmortizationMap {
     @RequiresApi(Build.VERSION_CODES.O)
     fun mapping(cursor: Cursor): AddAmortizationDTO {
         val id = cursor.getInt(0)
         val code = cursor.getInt(1)
         val nbrQuote = cursor.getLong(2)
         val value = cursor.getDouble(3)
         val create = DateUtils.toLocalDate(cursor.getString(4))
         return AddAmortizationDTO(id, create, code, nbrQuote, value)
     }

     @RequiresApi(Build.VERSION_CODES.O)
     fun mapping(dto: AddAmortizationDTO): ContentValues {
         return ContentValues().apply {
             put(AddAmortizationDB.Entry.COLUMN_CODE, dto.code)
             put(AddAmortizationDB.Entry.COLUMN_NBR_QUOTE, dto.nbrQuote)
             put(AddAmortizationDB.Entry.COLUMN_VALUE, dto.value)
             put(
                 AddAmortizationDB.Entry.COLUMN_DATE_CREATE,
                 DateUtils.localDateToStringDate(dto.create)
             )
         }
     }
}