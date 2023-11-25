package co.japl.finances.core.usercases.implement.recap

import co.japl.finances.core.adapters.outbound.interfaces.IProjectionsRecapPort
import co.japl.finances.core.usercases.interfaces.recap.IProjections
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(private val projection:IProjectionsRecapPort) :IProjections {
    override fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal> {
        val values = projection.getAllActive()
        val quote = values.sumOf { it.quote }
        val saved = values.sumOf { it.quote * (getMonths(it.type) - Period.between(LocalDate.now(),it.end).toTotalMonths()).toBigDecimal() }
        return Pair(saved,quote)
    }

    private fun getMonths(type:String):Int{
        return when(type.toLowerCase()){
            "trimestral" -> 3
            "cuatrimestral" ->4
            "semestral" ->6
            "anual" ->12
            else -> 0
        }
    }
}