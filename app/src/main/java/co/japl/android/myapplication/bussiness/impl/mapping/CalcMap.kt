package co.japl.android.myapplication.bussiness.impl.mapping

import android.content.ContentValues
import co.japl.android.myapplication.bussiness.DTO.CalcDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO

class CalcMap {
    fun mapping(dto:CalcDTO ):ContentValues{
        return ContentValues().apply {
            put(CalcDB.CalcEntry.COLUMN_ALIAS,dto.name)
            put(CalcDB.CalcEntry.COLUMN_INTEREST,dto.interest)
            put(CalcDB.CalcEntry.COLUMN_PERIOD,dto.period)
            put(CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT,dto.quoteCredit.toDouble())
            put(CalcDB.CalcEntry.COLUMN_VALUE_CREDIT,dto.valueCredit.toDouble())
        }
    }
}