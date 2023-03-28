package co.japl.android.myapplication.finanzas.bussiness.impl

import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO

class PeriodCreditImpl(val dbConnect: SQLiteOpenHelper) {

    fun getAll():List<PeriodCreditDTO>{
        return arrayListOf()
    }
}