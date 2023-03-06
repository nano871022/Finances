package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.utils.KindBoughtEnum
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.file.WatchEvent.Kind
import java.security.cert.PKIXRevocationChecker.Option
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

class PeriodsMap (private val taxSvc:ITaxSvc, private val buyCCSettingSvc:BuyCreditCardSettingImpl,private val creditCardSettingSvc:CreditCardSettingImpl){


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSettings(id:Int):Optional<CreditCardSettingDTO> {
        val buyCCSettingDto = buyCCSettingSvc.get(id)
        if (buyCCSettingDto.isPresent) {
            val creditCardSettingDto =
                creditCardSettingSvc.get(buyCCSettingDto.get().codeCreditCardSetting)
            if (creditCardSettingDto.isPresent) {
                return Optional.of(creditCardSettingDto.get())
            }
        }
        return Optional.empty()
    }
    @RequiresApi(Build.VERSION_CODES.O)
        private fun getTax(cursor:Cursor,tax:Optional<TaxDTO>,creditCardSettingDto:Optional<CreditCardSettingDTO>):BigDecimal{
            var interestPercent = if(tax.isPresent){
                tax.get().value.toBigDecimal()
            }else {
                cursor.getDouble(2).toBigDecimal()
            }
            creditCardSettingDto.ifPresent{interestPercent = it.value.toBigDecimal()}
            return interestPercent
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStartDate(endDate:LocalDateTime,cutoffDate: Int):LocalDateTime{
        val startDate = if(endDate.withDayOfMonth(1).minusDays(1).dayOfMonth > cutoffDate){
            endDate.minusMonths(1).withDayOfMonth(cutoffDate).plusDays(1).withHour(0).withMinute(0).withSecond(0)
        }else{
            endDate.withDayOfMonth(1).minusDays(1).plusDays(1).withHour(0).withMinute(0).withSecond(0)
        }
        return startDate
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getEndDate(cursor:Cursor,cutoffDate: Int):LocalDateTime{
        val boughtDate = LocalDate.parse(cursor.getString(4), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val lastDayOfMonth = boughtDate.withDayOfMonth(1).plusMonths(1).minusDays(1).dayOfMonth
        val endDate = if(lastDayOfMonth > cutoffDate){
            LocalDateTime.of(boughtDate.year, boughtDate.month, cutoffDate, 11, 59, 59)
        }else {
            LocalDateTime.of(boughtDate.year, boughtDate.month, lastDayOfMonth, 11, 59, 59)
        }
        return endDate
    }

    private fun getInterestValue(month:BigDecimal,valueBought:BigDecimal,interestPercent:BigDecimal):BigDecimal{
        val interest = if(month > BigDecimal.ONE){
            (valueBought.toDouble() * (interestPercent.toDouble() / 100 )).toBigDecimal()
        }else{ BigDecimal.ZERO }
        return interest
    }

    private fun getCapitalValue(month: BigDecimal,valueBought: BigDecimal):BigDecimal{
        val capital = if(month > BigDecimal.ONE){
            (valueBought.toDouble() / month.toDouble()).toBigDecimal()
        }else{
            valueBought
        }
        return capital
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun maping(cursor:Cursor,cutoffDate:Int):PeriodDTO{
        val id = cursor.getInt(0)
        val creditCardSettingDto = getSettings(id)

        val endDate = getEndDate(cursor,cutoffDate)

        val tax = taxSvc.get(endDate.year,endDate.monthValue,TaxEnum.CREDIT_CARD)

        val startDate = getStartDate(endDate,cutoffDate)
        val interestPercent = getTax(cursor,tax,creditCardSettingDto)

        val valueBought = cursor.getDouble(6).toBigDecimal()
        val month = cursor.getInt(7).toBigDecimal()
        val interest = getInterestValue(month,valueBought,interestPercent)

        val capital = getCapitalValue(month,valueBought)

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