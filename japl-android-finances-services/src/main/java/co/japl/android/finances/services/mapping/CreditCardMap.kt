package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.CreditCardDB
import co.japl.android.finances.services.dto.CreditCardDTO
import co.japl.android.finances.services.implement.Config
import co.japl.android.finances.services.dto.CreditCard
import co.japl.android.finances.services.utils.CursorUtils
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.util.*

class CreditCardMap {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditCardDTO ):ContentValues{
        return ContentValues().apply {
            put(CreditCardDB.CreditCardEntry.COLUMN_NAME,dto.name)
            put(CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES,dto.maxQuotes)
            put(CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY,dto.cutOffDay)
            put(CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE,dto.warningValue.toString())
            put(CreditCardDB.CreditCardEntry.COLUMN_STATUS,if(dto.status) 1 else 0)
            put(CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q,if(dto.interest1Quote) 1 else 0)
            put(CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ,if(dto.interest1NotQuote)1 else 0)
            put(CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.create))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):CreditCardDTO{
        val name = CursorUtils.getString(cursor,CreditCardDB.CreditCardEntry.COLUMN_NAME)
        val maxQuotes = CursorUtils.getShort(cursor,CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES)
        val cutOffDay = CursorUtils.getShort(cursor,CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY)
        val warningValue = CursorUtils.getBigDecimal(cursor,CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE)
        val status = (CursorUtils.getShort(cursor, CreditCardDB.CreditCardEntry.COLUMN_STATUS) ?: 0) > 0
        val interest1Quote = (CursorUtils.getShort(cursor, CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q)?:0) > 0
        val interest1NotQuote = (CursorUtils.getShort(cursor, CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ)?:0) > 0
        val createDate = DateUtils.toLocalDateTime(cursor.getString(8))
        val id = CursorUtils.getInt(cursor,BaseColumns._ID)
        return  CreditCardDTO(
            id?:0,
            name?:"",
            maxQuotes?:0,
            cutOffDay?:0,
            warningValue?: BigDecimal.ZERO,
            createDate,
            status,
            interest1Quote,
            interest1NotQuote
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapper(creditCard:CreditCardDTO): CreditCard {
        val pojo = CreditCard()
        pojo.codeCreditCard = Optional.ofNullable(creditCard.id)
        pojo.nameCreditCard = Optional.ofNullable(creditCard.name)
        Log.d(this.javaClass.name,"CutOffDay: ${creditCard.cutOffDay}")
        pojo.cutoffDay = Optional.ofNullable(creditCard.cutOffDay)
        pojo.cutOff =
            Optional.ofNullable(creditCard.cutOffDay.toInt()
                ?.let { it1 -> Config().nextCutOff( it1) })
        pojo.warningValue = Optional.ofNullable(creditCard.warningValue)
        pojo.maxQuotes = Optional.ofNullable(creditCard.maxQuotes)
        return pojo
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(CreditCardDB.CreditCardEntry.COLUMN_NAME, CursorUtils.getString(crsor, CreditCardDB.CreditCardEntry.COLUMN_NAME))
            put(CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES, CursorUtils.getShort(crsor, CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES))
            put(CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY, CursorUtils.getShort(crsor, CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY))
            put(CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE, CursorUtils.getString(crsor, CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE))
            put(CreditCardDB.CreditCardEntry.COLUMN_STATUS, CursorUtils.getShort(crsor, CreditCardDB.CreditCardEntry.COLUMN_STATUS))
            put(CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q, CursorUtils.getShort(crsor, CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q))
            put(CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ, CursorUtils.getShort(crsor, CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ))
            put(CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE, CursorUtils.getString(crsor, CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE))
        }
    }
}