package co.japl.finances.core.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

object Config {

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextCutOff(cutOffDay:Int): LocalDateTime {
        try {
            val now = LocalDateTime.now()
            var cutOff = LocalDateTime.now()
            val lastDayOfMonth = LocalDateTime.of(
                now.year,
                now.month,
                1,
                LocalTime.MIN.hour,
                LocalTime.MIN.minute,
                LocalTime.MIN.second
            ).plusMonths(1).minusDays(1).dayOfMonth
            cutOff = if (lastDayOfMonth < cutOffDay) {
                LocalDateTime.of(
                    now.year,
                    now.month,
                    lastDayOfMonth,
                    LocalTime.MAX.hour,
                    LocalTime.MAX.minute,
                    LocalTime.MAX.second
                )
            } else {
                LocalDateTime.of(
                    now.year,
                    now.month,
                    cutOffDay,
                    LocalTime.MAX.hour,
                    LocalTime.MAX.minute,
                    LocalTime.MAX.second
                )
            }
            var date = if (now.isBefore(cutOff) || now == cutOff) {
                cutOff
            } else {
                cutOff.plusMonths(1)
            }
            date = when (date.dayOfWeek) {
                DayOfWeek.SATURDAY -> date.minusDays(1)
                DayOfWeek.SUNDAY -> date.plusDays(1)
                else -> date
            }
            return date
        }catch(e:Exception){
            Log.e(this.javaClass.simpleName,"Error: ${e.message}")
            return LocalDateTime.now()
        }
    }
}