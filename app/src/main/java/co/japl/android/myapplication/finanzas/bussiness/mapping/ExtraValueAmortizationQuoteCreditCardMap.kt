package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationQuoteCreditCardDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationQuoteCreditCardDTO
import co.com.japl.ui.utils.DateUtils

class ExtraValueAmortizationQuoteCreditCardMap {
     @RequiresApi(Build.VERSION_CODES.O)
     fun mapping(cursor: Cursor): ExtraValueAmortizationQuoteCreditCardDTO {
         val id = cursor.getInt(0)
         val code = cursor.getInt(1)
         val nbrQuote = cursor.getLong(2)
         val value = cursor.getDouble(3)
         val create = DateUtils.toLocalDate(cursor.getString(4))
         return ExtraValueAmortizationQuoteCreditCardDTO(id, create, code, nbrQuote, value)
     }

     @RequiresApi(Build.VERSION_CODES.O)
     fun mapping(dto: ExtraValueAmortizationQuoteCreditCardDTO): ContentValues {
         return ContentValues().apply {
             put(ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_CODE, dto.code)
             put(ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_NBR_QUOTE, dto.nbrQuote)
             put(ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_VALUE, dto.value)
             put(
                 ExtraValueAmortizationQuoteCreditCardDB.Entry.COLUMN_DATE_CREATE,
                 DateUtils.localDateToStringDate(dto.create)
             )
         }
     }
}