package co.japl.android.myapplication.bussiness.impl

import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import java.time.LocalDateTime

class Config : ConfigSvc {

    override fun variableTaxCreditMonthly(): Double {
        var yearTax = 28.8
        return yearTax / 12
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun nextCutOff(cutOffDay:Int): LocalDateTime {
        val now = LocalDateTime.now()
        val cutOff =  LocalDateTime.of(now.year, now.month, cutOffDay, 23, 59, 59)
        if(now.isBefore(cutOff) || now == cutOff){
            return cutOff
        }
        return  cutOff.plusMonths(1)
    }
}