package co.japl.android.finances.services.implement

import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.finances.services.dto.PeriodCreditDTO

class PeriodCreditImpl(val dbConnect: SQLiteOpenHelper) {

    fun getAll():List<PeriodCreditDTO>{
        return arrayListOf()
    }
}