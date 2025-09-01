package co.japl.finances.core.usercases.implement.credit

import android.util.Log
import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import co.com.japl.finances.iports.outbounds.IAdditionalRecapPort
import co.com.japl.finances.iports.outbounds.ICreditFixRecapPort
import co.japl.finances.core.usercases.interfaces.credit.IPeriodCredit
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import javax.inject.Inject

class PeriodCredit @Inject constructor(private val periodSvc: ICreditFixRecapPort,private val additionalSvc:IAdditionalRecapPort) : IPeriodCredit{

    override fun getRecords(): List<PeriodCreditDTO> {
        val list = mutableListOf<PeriodCreditDTO>()
        val currentDate = LocalDate.now()
        periodSvc.getAll().takeIf { it.isNotEmpty() }?.let{
            it.groupBy { it.date.year }.forEach {
                    it.value.groupBy { it.date.monthValue }.forEach{
                    val difference = Period.between(it.value.first().date,currentDate).toTotalMonths()
                     val additionalCost = it.value.map { additionalSvc.getListByIdCredit(it.id.toLong()) }.flatMap { it }.sumOf { it.value }

                    repeat ( difference.toInt() ){ values->
                        Log.d(this.javaClass.name,"getRecors: $difference $values $additionalCost $it")
                        val record = PeriodCreditDTO(
                            value = it.value.sumOf { it.quoteValue } + additionalCost,
                            count = it.value.size,
                            date = YearMonth.of(it.value.first().date.year,
                                                it.value.first().date.monthValue)
                                            .plusMonths(values.toLong())
                        )
                        list.add(record)
                    }
                }
            }
        }
        return list.groupBy { it.date }.map {
            PeriodCreditDTO(
                date = it.value.first().date,
                count = it.value.sumOf { it.count },
                value = it.value.sumOf { it.value }
            )
        }.sortedByDescending { it.date  }
    }

}