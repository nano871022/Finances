package co.japl.android.myapplication.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(value: String): LocalDateTime {
            val date = value.split("/")
            return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToString(value: LocalDateTime): String {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(value)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToStringDB(value: LocalDateTime): String {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'00:00").format(value)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToStringDate(value: LocalDateTime): String {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(value)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getMonths(startDate:LocalDateTime, endDate:LocalDateTime):Long{
            val period = Period.between(startDate.toLocalDate(),endDate.toLocalDate())
            val month = period.months
            val years = period.years
            var value = (years * 12) + month - 1
            if(value < 0){
                value = 0
            }
            return value.toLong()
        }
    }
}

