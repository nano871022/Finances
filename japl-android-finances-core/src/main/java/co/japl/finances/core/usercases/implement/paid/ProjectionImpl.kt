package co.japl.finances.core.usercases.implement.paid

import android.util.Log
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.outbounds.IProjectionsRecapPort
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject
import kotlin.compareTo

class ProjectionImpl @Inject constructor(private val projection:IProjectionsRecapPort): IProjection {

    override fun getProjectionRecap(): Triple<Int, BigDecimal, List<ProjectionRecap>> {
        val list = projection.getAllActive()
        val recap = list.map{
            val period = Period.between(LocalDate.now(),it.end)
            val periods = Period.between(it.create,it.end)
            ProjectionRecap(
                limitDate = it.end,
                monthsLeft = period.toTotalMonths().toShort(),
                savedCash = savedAmount(it.quote.toDouble(),periods.toTotalMonths().toInt(),period.toTotalMonths().toInt()).toBigDecimal()
            )
        }.sortedByDescending { it.limitDate }
        val listEnd = arrayListOf<ProjectionRecap>()
        recap.firstOrNull()?.let(listEnd::add)
        recap.lastOrNull()?.let(listEnd::add)
        return Triple(list.size, list.sumOf { it.quote },listEnd)
    }

    override fun getProjections(): List<ProjectionDTO> {
        val list = projection.getAllActive()
        list.forEach {
            val period = Period.between(LocalDate.now(),it.end)
            val periods = Period.between(it.create,it.end)
            it.monthsLeft = period.toTotalMonths().toInt()
            it.amountSaved = savedAmount(it.quote.toDouble(),periods.toTotalMonths().toInt(),period.toTotalMonths().toInt()).toBigDecimal()
        }
        return list
    }

    override fun delete(id: Int): Boolean {
        return projection.delete(id)
    }

    override fun save(projection: ProjectionDTO): Boolean {
        return this.projection.save(projection)
    }

    override fun update(projection: ProjectionDTO): Boolean {
        return this.projection.update(projection)
    }

    override fun findById(id: Int): ProjectionDTO? {
        return projection.findById(id)
    }

    override fun calculateQuote(
        period: KindPaymentsEnums,
        date: LocalDate,
        value: BigDecimal
    ): BigDecimal {
        try {
            val totalMonths = Period.between(LocalDate.now(), date).toTotalMonths()
            val periods = (totalMonths / period.month).takeIf { it > 0 }?:1L
            return (value.toDouble() / periods).toBigDecimal()
        }catch(e: NumberFormatException){
            Log.e(this.javaClass.name,"Period: $period Date: $date Value: $value ${e.message}",e)
            return BigDecimal.ZERO
        }
    }

    override fun savedAmount(quote:Double, monthsLeft:Int, months:Int): Double{
        return quote *  (months - monthsLeft)
    }

}