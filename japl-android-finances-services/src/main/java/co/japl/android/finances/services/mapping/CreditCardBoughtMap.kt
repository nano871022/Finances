package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getStringOrNull
import co.japl.android.finances.services.dto.CreditCardBought
import co.japl.android.finances.services.dto.CreditCardBoughtDB
import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import co.japl.android.finances.services.utils.CursorUtils
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreditCardBoughtMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(quote:CreditCardBought):CreditCardBoughtDTO{
        return  CreditCardBoughtDTO(
            quote.codeCreditCard!!,
            "",
            quote.nameItem!!,
            quote.valueItem!!,
            quote.interest!!,
            quote.month!!,
            quote.boughtDate!!,
            quote.cutOutDate!!,
            LocalDateTime.now(),
            LocalDateTime.of(9999,12,31,23,59,59,999),
            0,
            0,
            quote.kind!!,
            quote.kindOfTax!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditCardBoughtDTO ):ContentValues{
        return ContentValues().apply {
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD,dto.codeCreditCard)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE,dto.boughtDate.toInstant(
                ZoneOffset.UTC).epochSecond)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST,dto.interest)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH,dto.month)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToStringDate(dto.createDate))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM,dto.nameItem)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM,dto.valueItem.toDouble())
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE,DateUtils.localDateTimeToStringDate(dto.cutOutDate))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT,dto.recurrent)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND,dto.kind)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX,dto.kindOfTax)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE,dto.endDate.toInstant(
                ZoneOffset.UTC).epochSecond)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):CreditCardBoughtDTO{

        val creditCardCode = CursorUtils.getInt(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD)
        val etProductName  = CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM)
        val etProductValue = CursorUtils.getBigDecimal(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM)
        val etTax          = CursorUtils.getDouble(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST)
        val etMonths       = CursorUtils.getInt(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH)
        val boughtDate     = CursorUtils.getLocalDateTime(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE)
        val cutOffDate     = CursorUtils.getLocalDateTime(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE)
        val createDate     = CursorUtils.getLocalDateTime(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE)
        val endDate        = CursorUtils.getLocalDateTime(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE)
        val recurrent      = CursorUtils.getShort(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT)
        val kind           = CursorUtils.getShort(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND)
        val kindOfTax      = CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX)
        val id             = CursorUtils.getInt(cursor,BaseColumns._ID)
        return  CreditCardBoughtDTO(
            creditCardCode?:0,
            "",
            etProductName?:"",
            etProductValue?:BigDecimal.ZERO,
            etTax?:0.0,
            etMonths?:1,
            boughtDate?:DateUtils.DEFAULT_DATE_TIME,
            cutOffDate?:DateUtils.DEFAULT_DATE_TIME,
            createDate?:DateUtils.DEFAULT_DATE_TIME,
            endDate?:DateUtils.DEFAULT_DATE_TIME,
            id?:0,
            recurrent?:0,
            kind?:0,
            kindOfTax?:"")
    }

    fun restore(cursor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, cursor.getLong(0))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD, CursorUtils.getInt(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST, CursorUtils.getDouble(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH, CursorUtils.getInt(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT, CursorUtils.getInt(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND, CursorUtils.getInt(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE, CursorUtils.getString(cursor,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE))
        }
    }

}