package co.japl.android.myapplication.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.Config
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.utils.DateUtils
import java.util.*

class CreditCardMap {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto:CreditCardDTO ):ContentValues{
        return ContentValues().apply {
            put(CreditCardDB.CreditCardEntry.COLUMN_NAME,dto.name)
            put(CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY,dto.cutOffDay)
            put(CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE,dto.warningValue.toString())
            put(CreditCardDB.CreditCardEntry.COLUMN_STATUS,dto.status)
            put(CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToString(dto.create))

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor:Cursor):CreditCardDTO{
        val name = cursor.getString(1)
        val cutOffDay = cursor.getShort(2)
        val warningValue = cursor.getString(3).toBigDecimal()
        val status = cursor.getShort(4)
        val createDate = DateUtils.toLocalDateTime(cursor.getString(5))
        val id = cursor.getString(0).toInt()
        return  CreditCardDTO(
            id,
            name,
            cutOffDay,
            warningValue,
            createDate,
            status
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
        return pojo
    }

}