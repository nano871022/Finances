package co.japl.android.myapplication.bussiness.impl

import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class Config @Inject constructor() : ConfigSvc {

    override fun variableTaxCreditMonthly(): Double {
        var yearTax = 28.8
        return yearTax / 12
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun nextCutOff(cutOffDay:Int): LocalDateTime {
        val now = LocalDateTime.now()
        var cutOff =  LocalDateTime.now()
        val lastDayOfMonth = LocalDateTime.of(now.year, now.month,1,LocalTime.MIN.hour,LocalTime.MIN.minute,LocalTime.MIN.second).plusMonths(1).minusDays(1).dayOfMonth
        cutOff = if(lastDayOfMonth < cutOffDay){
            LocalDateTime.of(now.year, now.month, lastDayOfMonth, LocalTime.MAX.hour,LocalTime.MAX.minute,LocalTime.MAX.second)
        }else {
            LocalDateTime.of(now.year, now.month, cutOffDay, LocalTime.MAX.hour,LocalTime.MAX.minute,LocalTime.MAX.second)
        }
        var date = if(now.isBefore(cutOff) || now == cutOff){
            cutOff
        }else {
            cutOff.plusMonths(1)
        }
        date = when(date.dayOfWeek){
            DayOfWeek.SATURDAY -> date.minusDays(1)
            DayOfWeek.SUNDAY   ->date.plusDays(1)
            else -> date
        }
        return date
    }
}