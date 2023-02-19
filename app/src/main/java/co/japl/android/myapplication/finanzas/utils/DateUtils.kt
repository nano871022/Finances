package co.japl.android.myapplication.utils

import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.time.LocalDateTime
import java.time.Month
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
            var value = (years * 12) + month
            if(value < 0){
                value = 0
            }
            return value.toLong()
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getLocalDateTimeByString(date:EditText):LocalDateTime{
            try {
                val bought = date.text.toString()
                val date = bought.split("/")

                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            }catch(e:Exception){
                date.error = "Invalid value"
            }
            return LocalDateTime.now()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun startDateFromCutoff(cutOffDay:Short,cutOff:LocalDateTime):LocalDateTime{
            var dayIncrease:Long = 1
            var day = cutOff.dayOfMonth
            val month = cutOff.month
            if(month == Month.FEBRUARY && cutOffDay > 28 && cutOffDay < 31){
                day = cutOffDay.toInt()
            }else if(cutOffDay.toInt() == 31){
                day = cutOffDay - 1
                dayIncrease = 2
            }
            if(month == Month.MARCH && day.toInt() == 30){
                day = 28
            }
            println("$month $cutOffDay $day")
            return LocalDateTime.of(cutOff.minusMonths(1).year,cutOff.minusMonths(1).monthValue,day,0,0,0,0).plusDays(dayIncrease)
        }

    }

}

