package co.japl.finances.core.utils

import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.Period
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateUtils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(value: String): LocalDateTime {
            require(value.isNotBlank()) { "Value cannot be blank" }
            if (value.contains("/")) {
                val date = value.split("/")
                require(date.size > 1) { "Value is not valid $date" }
                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            } else {
                val date = value.split("-")
                require(date.size > 1) { "Value is not valid $date" }
                return LocalDateTime.of(
                    date[0].toInt(),
                    date[1].toInt(),
                    date[2].toInt(),
                    23,
                    59,
                    59,
                    999
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(value: String, default: LocalDateTime): LocalDateTime {
            if (value.isBlank()) {
                return default
            }
            if (value.contains("/")) {
                val date = value.split("/")
                if (date.size <= 1) {
                    return default
                }
                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            } else {
                val date = value.split("-")
                if (date.size <= 1) {
                    return default
                }
                return LocalDateTime.of(
                    date[0].toInt(),
                    date[1].toInt(),
                    date[2].toInt(),
                    23,
                    59,
                    59,
                    999
                )
            }
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDate(value: String): LocalDate {
            var date = value.split("/")
            if (date.size > 1) {
                return LocalDate.of(date[2].toInt(), date[1].toInt(), date[0].toInt())
            }
            date = value.split("-")
            if (date.size > 1) {
                return LocalDate.of(date[0].toInt(), date[1].toInt(), date[2].toInt())
            }
            if (value == "1") {
                return LocalDate.now()
            }
            throw Exception("Invalid date: $value");
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDate(value: String, default: LocalDate): LocalDate {
            if (value == null || value == "") {
                return default
            }
            val date = value.split("/")
            if (date.size < 2) {
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
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(value).replace("+", "")
                .substring(0, 10)
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
                .also { Log.d(javaClass.name, "<<<=== FINISH:localDateToStringDate Response: $it") }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getMonths(startDate: LocalDateTime, endDate: LocalDateTime): Long {
            val period = Period.between(startDate.toLocalDate(), endDate.toLocalDate())
            val month = period.months
            val years = period.years
            var value = (years * 12) + month
            if (value < 0) {
                value = 0
            }
            return value.toLong()
        }

        fun withDayOfMonth(date:LocalDateTime,day:Int):LocalDateTime{
            val maxDayOfMonth = date.plusMonths(1).withMonth(1).minusDays(1).dayOfMonth
            return if(maxDayOfMonth >= day){
                date.withDayOfMonth(day)
            }else{
                date.plusDays((day  - date.dayOfMonth).toLong())
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getMonths(startDate: LocalDate, endDate: LocalDateTime): Long {
            val period = Period.between(startDate, endDate.toLocalDate())
            val month = period.months
            val years = period.years
            var value = (years * 12) + month
            if (value < 0) {
                value = 0
            }
            return value.toLong()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getLocalDateTimeByString(date: EditText): LocalDateTime {
            try {
                val bought = date.text.toString()
                val date = bought.split("/")

                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            } catch (e: Exception) {
                date.error = "Invalid value"
            }
            return LocalDateTime.now()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun startDateFromCutoff(cutOffDay: Short, cutOff: LocalDateTime): LocalDateTime {
            var day = cutOff.dayOfMonth
            val month = cutOff.month

            if (month == Month.FEBRUARY && cutOffDay > 28 && cutOffDay < 31) {
                day = cutOffDay.toInt()
            } else if (cutOffDay.toInt() == 31) {
                day = cutOffDay - 1
            }
            if (month == Month.MARCH && day.toInt() == 30) {
                day = 28
            }

            val start = LocalDateTime.of(
                cutOff.minusMonths(1).year,
                cutOff.minusMonths(1).monthValue,
                1,
                0,
                0
            )
            var date = if (start.plusMonths(1).minusDays(1).dayOfMonth < cutOffDay) {
                start.plusMonths(1)
            } else {
                cutOff.minusMonths(1)
            }

            date = when (date.dayOfWeek) {
                DayOfWeek.SATURDAY -> date.minusDays(1)
                DayOfWeek.SUNDAY -> date.plusDays(1)
                else -> date
            }
            return date.plusDays(1).also {
                Log.d(
                    this::class.java.name,
                    "<<<=== FINISH:startDateFromCutoff Response: $it Month: $month Cutoff Day: $cutOffDay Day: $day CutOff: $cutOff"
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffLastMonth(cutOffDay: Short, cutOff: LocalDateTime): LocalDateTime {

            val cutOffEndMonth = cutOff.withDayOfMonth(1).minusDays(1)
            var cutOffResponse = if (cutOffEndMonth.dayOfMonth < cutOffDay) {
                cutOffEndMonth
            } else {
                cutOff.minusMonths(1)
            }
            cutOffResponse = when (cutOffResponse.dayOfWeek) {
                DayOfWeek.SATURDAY -> cutOffResponse.minusDays(1)
                DayOfWeek.SUNDAY -> cutOffResponse.plusDays(1)
                else -> cutOffResponse
            }
            return cutOffResponse
        }

        fun cutOff(cutOffDay: Short,period:YearMonth): LocalDateTime {
            var now = LocalDate.of(period.year,period.monthValue,1)
            var month = now.month
            var cutOff = now.withDayOfMonth(1).minusDays(1).plusDays(cutOffDay.toLong())
            if (cutOff.month != month) {
                cutOff = cutOff.withDayOfMonth(1).minusDays(1)
            }
            month = cutOff.month
            cutOff = cutOff.minusMonths(1)
            if (month == cutOff.month) {
                cutOff = cutOff.withDayOfMonth(1).minusDays(1)
            }
            cutOff = when (cutOff.dayOfWeek) {
                DayOfWeek.SATURDAY -> cutOff.minusDays(1)
                DayOfWeek.SUNDAY -> cutOff.plusDays(1)
                else -> cutOff
            }
            return LocalDateTime.of(cutOff, LocalTime.MAX).also {
                Log.d(
                    javaClass.name,
                    "<<<=== FINISH:cutOff Day: $cutOffDay ${cutOff.dayOfWeek} Response: $it"
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffLastMonth(cutOffDay: Short): LocalDateTime {
            val now = LocalDate.now()
            var month = now.month
            var cutOff = now.withDayOfMonth(1).minusDays(1).plusDays(cutOffDay.toLong())
            if (cutOff.month != month) {
                cutOff = cutOff.withDayOfMonth(1).minusDays(1)
            }
            month = cutOff.month
            cutOff = cutOff.minusMonths(1)
            if (month == cutOff.month) {
                cutOff = cutOff.withDayOfMonth(1).minusDays(1)
            }
            cutOff = when (cutOff.dayOfWeek) {
                DayOfWeek.SATURDAY -> cutOff.minusDays(1)
                DayOfWeek.SUNDAY -> cutOff.plusDays(1)
                else -> cutOff
            }
            return LocalDateTime.of(cutOff, LocalTime.MAX).also {
                Log.d(
                    javaClass.name,
                    "<<<=== FINISH:cutOffLastMonth Day: $cutOffDay ${cutOff.dayOfWeek} Response: $it"
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cutOffAddMonth(cutOffDay: Short, cutOff: LocalDateTime, months: Long): LocalDateTime {
            val cutOffEndMonth = cutOff.withDayOfMonth(1).plusMonths(months).minusDays(1)
            if (cutOffEndMonth.dayOfMonth < cutOffDay) {
                return cutOffEndMonth
            }
            return cutOffEndMonth.withDayOfMonth(cutOffDay.toInt())
        }

        fun isDateValid(value: String): Boolean {
            return try {
                if (value.contains("/")) {
                    val date = value.split("/")
                    if (date.size > 1) {
                        return true
                    }
                } else {
                    val date = value.split("-")
                    if (date.size > 1) {
                        return true
                    }
                }
                return false
            } catch (e: DateTimeParseException) {
                return false
            }
        }

        fun isLocalDateRegex(value: String): Boolean {
            return     "\\d{2}\\/\\d{2}\\/\\d{4}".toRegex().matches(value)
                    || "\\d{2}\\-\\d{2}\\-\\d{4}".toRegex().matches(value)
                    || "\\d{4}\\/\\d{2}\\/\\d{2}".toRegex().matches(value)
                    || "\\d{4}\\-\\d{2}\\-\\d{2}".toRegex().matches(value)
                    || "\\d{2}\\/\\d{2}\\/\\d{2}".toRegex().matches(value)
                    || "\\d{2}\\-\\d{2}\\-\\d{2}".toRegex().matches(value)
                    || "\\d{2}\\/[10-31]\\/\\d{4}".toRegex().matches(value)
        }

        fun toLocalDateRegex(value:String):LocalDate?{
               return if("\\d{2}\\/\\d{2}\\/\\d{4}".toRegex().matches(value)){
                    LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
               }else if("\\d{2}\\-\\d{2}\\-\\d{4}".toRegex().matches(value)){
                   LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
               }else if("\\d{4}\\/\\d{2}\\/\\d{2}".toRegex().matches(value)){
                   LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
               }else if("\\d{4}\\-\\d{2}\\-\\d{2}".toRegex().matches(value)){
                   LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
               }else if("\\d{2}\\/\\d{2}\\/\\d{2}".toRegex().matches(value)){
                   LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yy"))
               }else if("\\d{2}\\-\\d{2}\\-\\d{2}".toRegex().matches(value)){
                   LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MM-yy"))
               }else if("\\d{2}\\/[10-31]\\/\\d{4}".toRegex().matches(value)){
                   LocalDate.parse(value, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
               }else {
                   null
               }
        }

    }}

