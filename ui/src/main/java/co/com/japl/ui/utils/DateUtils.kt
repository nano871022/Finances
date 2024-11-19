package co.com.japl.ui.utils

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
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateUtils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(value: String): LocalDateTime {
            require(value.isNotBlank()) { "Value cannot be blank" }
            if (value.contains("/")) {
                val date = value.split("/")
                require (date.size > 1) { "Value is not valid $date" }
                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            } else {
                val date = value.split("-")
                require (date.size > 1) { "Value is not valid $date" }
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
        fun toLocalDateTime2(value: String): LocalDateTime {
            require(value.isNotBlank()) { "Value cannot be blank" }
            if (value.contains("/")) {
                val date = value.split("/")
                require (date.size > 1) { "Value is not valid $date" }
                return LocalDateTime.of(date[2].toInt(), date[1].toInt(), date[0].toInt(), 0, 0, 0)
            } else {
                val date = value.split("-")
                require (date.size > 1) { "Value is not valid $date" }
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

        fun toLocalDate(milles: Long): LocalDate =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(milles),ZoneId.of("UTC")).toLocalDate()

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

        fun localDateToStringDB(value: LocalDate): String {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(value)
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

        private fun getDayOfMonthValid(day: Int, date: LocalDateTime): Int {
            try {
                return date.withDayOfMonth(day).dayOfMonth
            } catch (e: DateTimeException) {
                return getDayOfMonthValid(day - 1, date)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun startDateFromCutoff(cutOffDay: Short, cutOff: LocalDateTime): LocalDateTime {
            var day = cutOff.dayOfMonth
            val month = cutOff.month

            day = getDayOfMonthValid(cutOffDay.toInt(), cutOff)

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

        fun cutOff(cutOffDay: Short, date: LocalDate): LocalDateTime {
            var dateTime = LocalDateTime.of(date, LocalTime.MAX)
            try {
                if (date.dayOfMonth <= cutOffDay) {
                    dateTime = LocalDateTime.of(
                        LocalDate.of(
                            date.year,
                            date.monthValue,
                            cutOffDay.toInt()
                        ), LocalTime.MAX
                    )

                } else {
                    dateTime = LocalDateTime.of(
                        LocalDate.of(
                            date.year,
                            date.monthValue,
                            cutOffDay.toInt()
                        ), LocalTime.MAX
                    ).plusMonths(1)
                }
            } catch (e: DateTimeException) {
                return cutOff((cutOffDay.toInt() - 1).toShort(), date)
            }
            return when (date.dayOfWeek) {
                DayOfWeek.SATURDAY -> dateTime.minusDays(1)
                DayOfWeek.SUNDAY -> dateTime.plusDays(1)
                else -> dateTime
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
    }

}

