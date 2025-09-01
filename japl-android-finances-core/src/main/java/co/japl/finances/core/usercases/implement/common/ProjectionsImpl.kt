package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.outbounds.IProjectionsRecapPort
import co.japl.finances.core.usercases.interfaces.common.IProjections
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(private val projection:IProjectionsRecapPort, private val projectionSvc: IProjection) :
    IProjections {
    override fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal> {
        val values = projection.getAllActive()
        val quote = values.sumOf { it.quote }
        val saved = values.sumOf {
            val monthLeft = Period.between(LocalDate.now(),it.end).toTotalMonths()
            val months = Period.between(it.create,it.end).toTotalMonths()
            projectionSvc.savedAmount(it.quote.toDouble(),months.toInt(),monthLeft.toInt())
        }
        return Pair(saved.toBigDecimal(),quote)
    }
}