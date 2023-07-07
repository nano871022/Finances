package co.japl.android.myapplication.utils

import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.time.LocalDate
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
        fun toLocalDateTime(value: String,default:LocalDateTime): LocalDateTime {
            if(value.isBlank()){
                return default
            }
            if(value.contains("/")) {
                val date = value.split("/")
                if (date.size <= 1) {
                    return default
                }
                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            }else{
                val date = value.split("-")
                if (date.size <= 1) {
                    return default
                }
                return LocalDateTime.of(date[0].toInt(),date[1].toInt(),date[1].toInt(),23,59,59,999)
            }
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDate(value: String): LocalDate {
            var date = value.split("/")
            if(date.size > 1) {
                return LocalDate.of(date[2].toInt(), date[1].toInt(), date[0].toInt())
            }
            date = value.split("-")
            if(date.size > 1) {
                return LocalDate.of(date[0].toInt(), date[1].toInt(), date[2].toInt())
            }
            throw Exception("Invalid date: $value");
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDate(value: String, default:LocalDate): LocalDate {
            if(value == null || value == ""){
                return default
            }
            val date = value.split("/")
            if(date.size < 2){
                return default
            }
            return LocalDate.of(date[2].toInt(), date[1].toInt(), date[0].toInt())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToString(value: LocalDateTime): String {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(value)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateToString(value: LocalDate): String {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(value).replace("+","").substring(0,10)
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
        fun localDateToStringDate(value: LocalDate): String {
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
        fun getMonths(startDate:LocalDate, endDate:LocalDateTime):Long{
            val period = Period.between(startDate,endDate.toLocalDate())
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

            var start = LocalDateTime.of(cutOff.minusMonths(1).year,cutOff.minusMonths(1).monthValue,1,0,0)
            return if(start.plusMonths(1).minusDays(1).dayOfMonth < cutOffDay){
                 start.plusMonths(1)
            }else{
                 cutOff.minusMonths(1).plusDays(1)
            }.also { Log.d(javaClass.name,"<<<=== FINISH:startDateFromCutoff Response: $it Month: $month Cutoff Day: $cutOffDay Day: $day CutOff: $cutOff") }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffLastMonth(cutOffDay:Short, cutOff:LocalDateTime):LocalDateTime{
            val cutOffEndMonth = cutOff.withDayOfMonth(1).minusDays(1)
            if(cutOffEndMonth.dayOfMonth < cutOffDay){
                return cutOffEndMonth
            }
            return cutOff.minusMonths(1)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffAddMonth(cutOffDay:Short, cutOff:LocalDateTime,months:Long):LocalDateTime{
            val cutOffEndMonth = cutOff.withDayOfMonth(1).plusMonths(months).minusDays(1)
            if(cutOffEndMonth.dayOfMonth < cutOffDay){
                return cutOffEndMonth
            }
            return cutOffEndMonth.withDayOfMonth(cutOffDay.toInt())
        }
    }

}

