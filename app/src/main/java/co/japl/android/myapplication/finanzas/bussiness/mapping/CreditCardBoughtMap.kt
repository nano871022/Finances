package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardBought
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDateTime

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
            quote.boughtDate!!.plusMonths(quote.month!!.toLong()),
            0,
            0,
            quote.kind!!,
            quote.kindOfTax!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditCardBoughtDTO ):ContentValues{
        return ContentValues().apply {
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD,dto.codeCreditCard)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE,DateUtils.localDateTimeToString(dto.boughtDate))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST,dto.interest)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH,dto.month)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.createDate))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM,dto.nameItem)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM,dto.valueItem.toDouble())
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE,DateUtils.localDateTimeToString(dto.cutOutDate))
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT,dto.recurrent)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND,dto.kind)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX,dto.kindOfTax)
            put(CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE,DateUtils.localDateTimeToString(dto.endDate))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):CreditCardBoughtDTO{
        val creditCardCode = cursor.getInt(1)
        val etProductName = cursor.getString(2)
        val etProductValue = cursor.getString(3).toBigDecimal()
        val etTax = cursor.getDouble(4)
        val etMonths = cursor.getInt(5)
        val boughtDate = DateUtils.toLocalDateTime( cursor.getString(6))
        val cutOffDate = DateUtils.toLocalDateTime(cursor.getString(7))
        val createDate = DateUtils.toLocalDateTime(cursor.getString(8))
        val endDate = DateUtils.toLocalDateTime(cursor.getString(12),LocalDateTime.of(9999,12,31,23,59,59))
        val recurrent = cursor.getShort(9)
        val kind = cursor.getShort(10)
        val kindOfTax = cursor.getString(11)
        val id = cursor.getString(0).toInt()
        return  CreditCardBoughtDTO(
            creditCardCode,
            "",
            etProductName,
            etProductValue,
            etTax,
            etMonths,
            boughtDate,
            cutOffDate,
            createDate,
            endDate,
            id,
            recurrent,
            kind,
            kindOfTax)
    }
}