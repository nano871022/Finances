package co.japl.android.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finanzas.bussiness.DTO.CalcDB
import co.japl.android.finanzas.bussiness.DTO.CalcDTO
import co.japl.android.finanzas.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.finanzas.utils.DateUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreditCardBoughtMap {
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
        val recurrent = cursor.getShort(9)
        val kind = cursor.getShort(10)
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
            id,
            recurrent,
            kind)
    }

}