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
import co.japl.android.finances.services.utils.DateUtils
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
        val name = cursor.getString(1)
        val maxQuotes = cursor.getShort(2)
        val cutOffDay = cursor.getShort(3)
        val warningValue = cursor.getString(4).toBigDecimal()
        val status = cursor.getShort(5) > 0
        val interest1Quote = cursor.getShort(6) > 0
        val interest1NotQuote = cursor.getShort(7) > 0
        val createDate = DateUtils.toLocalDateTime(cursor.getString(8))
        val id = cursor.getString(0).toInt()
        return  CreditCardDTO(
            id,
            name,
            maxQuotes,
            cutOffDay,
            warningValue,
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
            put(CreditCardDB.CreditCardEntry.COLUMN_NAME, crsor.getString(1))
            put(CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES, crsor.getShort(2))
            put(CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY, crsor.getShort(3))
            put(CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE, crsor.getString(4))
            put(CreditCardDB.CreditCardEntry.COLUMN_STATUS, crsor.getShort(5))
            put(CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q, crsor.getShort(6))
            put(CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ, crsor.getShort(7))
            put(CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE, crsor.getString(8))
        }
    }
}