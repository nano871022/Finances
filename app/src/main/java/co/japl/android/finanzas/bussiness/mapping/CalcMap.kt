package co.japl.android.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import co.japl.android.finanzas.bussiness.DTO.CalcDB
import co.japl.android.finanzas.bussiness.DTO.CalcDTO

class CalcMap {
    fun mapping(dto:CalcDTO ):ContentValues{
        return ContentValues().apply {
            put(CalcDB.CalcEntry.COLUMN_ALIAS,dto.name)
            put(CalcDB.CalcEntry.COLUMN_INTEREST,dto.interest)
            put(CalcDB.CalcEntry.COLUMN_PERIOD,dto.period)
            put(CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT,dto.quoteCredit.toDouble())
            put(CalcDB.CalcEntry.COLUMN_VALUE_CREDIT,dto.valueCredit.toDouble())
            put(CalcDB.CalcEntry.COLUMN_TYPE,dto.type)
            put(CalcDB.CalcEntry.COLUMN_INTEREST_VALUE,dto.interestValue.toDouble())
            put(CalcDB.CalcEntry.COLUMN_CAPITAL_VALUE,dto.capitalValue.toDouble())
        }
    }

    fun mapping(cursor:Cursor):CalcDTO{
        val name = cursor.getString(1)
        val value = cursor.getString(6).toString().toBigDecimal()
        val interest = cursor.getString(3).toDouble()
        val period = cursor.getString(4).toLong()
        val quote = cursor.getString(5).toBigDecimal()
        var type = cursor.getString(2)
        var interestValue = cursor.getString(7).toBigDecimal()
        var capitalValue = cursor.getString(8).toBigDecimal()
        if(type == null || type.isBlank()){
            type = "fix"
        }
        val id = cursor.getString(0).toInt()
        return CalcDTO(name
            ,value
            ,interest
            ,period
            ,quote
            ,type
            ,id
            ,interestValue
            ,capitalValue )
    }
}