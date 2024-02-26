package co.japl.finances.core.usercases.implement.common

import android.os.Build
import androidx.annotation.RequiresApi
import co.com.japl.finances.iports.outbounds.IAdditionalRecapPort
import co.com.japl.finances.iports.outbounds.ICreditFixRecapPort
import co.com.japl.finances.iports.dtos.*
import co.japl.finances.core.usercases.interfaces.common.ICreditFix
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class CreditFixImpl @Inject constructor(private val creditFixPort: ICreditFixRecapPort
                                        , private val additionalSvc: IAdditionalRecapPort) :
    ICreditFix {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTotalQuote(date: LocalDate): BigDecimal {
        val list = creditFixPort.getAll().filter { date < it.date.plusMonths(it.periods.toLong()) }
        val additionals = list.map {
            additionalSvc.getListByIdCredit(it.id.toLong())
        }?.flatMap { it.toList() }
            ?.sumOf { it.value }

        val quote =  list.sumOf { it.quoteValue }
        return quote + (additionals ?: BigDecimal.ZERO)
    }
}