package co.japl.android.finances.services.utils

import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.Period
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {

        val DEFAULT_DATE_TIME = LocalDateTime.of(2099, 12, 31, 23, 59, 59)

        fun toSeconds(date:LocalDateTime?):Long?{
            return date?.toInstant(ZoneOffset.UTC)?.epochSecond
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(value: String): LocalDateTime {
            require(value.isNotBlank()) { "Value cannot be blank" }
            if (value.contains("/")) {
                val date = value.split("/")
                require (date.size > 1) { "Value is not valid $date" }
                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            } else {
                val value = value.contains("T")?.takeIf{ it }?.let{value.substring(0,value.indexOf("T"))}?:value
                val date = value.split("-")
                if (date.size > 1) {
                    return LocalDateTime.of(
                        date[0].toInt(),
                        date[1].toInt(),
                        date[2].toInt(),
                        23,
                        59,
                        59,
                        999
                    )
                }else{
                    return LocalDateTime.now()
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(value: String,default:LocalDateTime): LocalDateTime {
            if("^[0-9]+$".toRegex().matches(value)){
                val instant = Instant.ofEpochSecond(value.toLong())
                return LocalDateTime.ofInstant(instant,ZoneOffset.UTC)
            }
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
                return LocalDateTime.of(date[0].toInt(),date[1].toInt(),date[2].toInt(),23,59,59,999)
            }
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDate(value: String): LocalDate {
            var date = value.split("/")
            if(date[0].length == 4){
                return LocalDate.of(date[0].toInt(), date[1].toInt(), date[2].toInt())
            }

            if(date.size > 1) {
                return LocalDate.of(date[2].toInt(), date[1].toInt(), date[0].toInt())
            }
            date = value.split("-")
            if(date.size > 1) {
                return LocalDate.of(date[0].toInt(), date[1].toInt(), date[2].toInt())
            }
            if(value == "1"){
                return LocalDate.now()
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
            return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(value).also { Log.d(javaClass.name,"<<<=== FINISH:localDateToStringDate Response: $it") }
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
        fun startDateFromCutoff(cutOffDay:Short,cutOff:LocalDateTime):LocalDateTime{
            val month = cutOff.month
            val day = dayOfMonthByRangeDaysAndMonth(cutOff.year,month.value,cutOffDay)

            val start = LocalDateTime.of(cutOff.minusMonths(1).year,cutOff.minusMonths(1).monthValue,1,0,0)
            val date =  if(start.plusMonths(1).minusDays(1).dayOfMonth < cutOffDay){
                 start.plusMonths(1)
            }else{
                 cutOff.minusMonths(1)
            }

            val dateEnd = checkWeekendDay(date.toLocalDate()).let { LocalDateTime.of(it,date.toLocalTime()) }

            return dateEnd.plusDays(1).also { Log.d(this::class.java.name,"<<<=== FINISH:startDateFromCutoff Response: $it Month: $month Cutoff Day: $cutOffDay Day: $day CutOff: $cutOff") }
        }

        private fun checkWeekendDay(date:LocalDate):LocalDate{
            return when(date.dayOfWeek ){
                DayOfWeek.SATURDAY-> date.minusDays(1)
                DayOfWeek.SUNDAY-> date.plusDays(1)
                else -> date
            }
        }

        private fun dayOfMonthByRangeDaysAndMonth(year:Int,month:Int,cutOffDay:Short,repeat:Int=0):Short{
            val date = LocalDate.of(year,month,1)
            if(repeat>2 || cutOffDay <= 0)   {
                Log.e(this.javaClass.simpleName,"=== error: $repeat $year $month $cutOffDay")
                return date.dayOfMonth.toShort()
            }
            try{
                return date.withDayOfMonth(cutOffDay.toInt()).dayOfMonth.toShort()
            }catch(e:DateTimeException){
                return dayOfMonthByRangeDaysAndMonth(year,month,(cutOffDay -1).toShort(), repeat = repeat + 1)
            }
        }

        fun cutoffDate(cutoffDay:Short,month:Short,year:Int):LocalDateTime{
            val day = dayOfMonthByRangeDaysAndMonth(year,month.toInt(),cutoffDay)
            val date =  LocalDate.of(year,month.toInt(),day.toInt())
            return checkWeekendDay(date).let { LocalDateTime.of(it, LocalTime.MAX) }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffLastMonth(cutOffDay:Short, cutOff:LocalDateTime):LocalDateTime{

            val cutOffEndMonth = cutOff.withDayOfMonth(1).minusDays(1)
            var cutOffResponse = if(cutOffEndMonth.dayOfMonth < cutOffDay){
                 cutOffEndMonth
            }else {
                cutOff.minusMonths(1)
            }
            cutOffResponse = when(cutOffResponse.dayOfWeek ){
                DayOfWeek.SATURDAY-> cutOffResponse.minusDays(1)
                DayOfWeek.SUNDAY-> cutOffResponse.plusDays(1)
                else -> cutOffResponse
            }
            return cutOffResponse
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffLastMonth(cutOffDay: Short):LocalDateTime{
            val now = LocalDate.now()
            var month = now.month
            var cutOff = now.withDayOfMonth(1).minusDays(1).plusDays(cutOffDay.toLong())
            if(cutOff.month != month){
                cutOff = cutOff.withDayOfMonth(1).minusDays(1)
            }
            month = cutOff.month
            cutOff = cutOff.minusMonths(1)
            if(month == cutOff.month){
                cutOff = cutOff.withDayOfMonth(1).minusDays(1)
            }
            cutOff = when(cutOff.dayOfWeek ){
                DayOfWeek.SATURDAY-> cutOff.minusDays(1)
                DayOfWeek.SUNDAY-> cutOff.plusDays(1)
                else -> cutOff
            }
            return LocalDateTime.of(cutOff, LocalTime.MAX).also { Log.d(javaClass.name,"<<<=== FINISH:cutOffLastMonth Day: $cutOffDay ${cutOff.dayOfWeek } Response: $it") }
        }
    }

}

