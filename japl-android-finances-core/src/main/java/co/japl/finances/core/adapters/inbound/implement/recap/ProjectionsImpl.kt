package co.japl.finances.core.adapters.inbound.implement.recap

import co.japl.finances.core.adapters.inbound.interfaces.recap.IProjectionsPort
import co.japl.finances.core.usercases.interfaces.recap.IProjections
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(private val projection: IProjections):IProjectionsPort {
    override fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal> {
        return projection.getTotalSavedAndQuote()
    }
}