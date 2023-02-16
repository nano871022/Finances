package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PeriodsMap {

    @RequiresApi(Build.VERSION_CODES.O)
    public  fun maping(cursor:Cursor):PeriodDTO{
        val boughtDate = LocalDate.parse(cursor.getString(4), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val cutoffDate = cursor.getInt(3)
        val lastDayOfMonth = boughtDate.withDayOfMonth(1).plusMonths(1).minusDays(1).dayOfMonth
        val endDate = if(lastDayOfMonth > cutoffDate){
            LocalDateTime.of(boughtDate.year, boughtDate.month, cutoffDate, 11, 59, 59)
        }else {
            LocalDateTime.of(boughtDate.year, boughtDate.month, lastDayOfMonth, 11, 59, 59)
        }
        val startDate = endDate.minusMonths(1).plusDays(1).withHour(0).withMinute(0).withSecond(0)
        val interestPercent =  cursor.getDouble(2).toBigDecimal()
        val valueBought = cursor.getDouble(6).toBigDecimal()
        val month = cursor.getInt(7).toBigDecimal()
        val interest = if(month > BigDecimal.ONE){
            (valueBought.toDouble() * (interestPercent.toDouble() / 100 )).toBigDecimal()
        }else{ BigDecimal.ZERO }
        val capital = if(month > BigDecimal.ONE){
            (valueBought.toDouble() / month.toDouble()).toBigDecimal()
        }else{
            valueBought
        }
        return PeriodDTO(
            cursor.getInt(5),
            startDate,
            endDate,
            interest,
                    capital,
                    capital.plus(interest)
        )
    }
}