package co.japl.android.finances.services.implement

import android.util.Log
import co.japl.android.finances.services.interfaces.IKindOfTaxSvc
import co.japl.android.finances.services.enums.KindOfTaxEnum
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.pow

class KindOfTaxImpl @Inject constructor():IKindOfTaxSvc {
    private val PERIODS_YEAR = 12
    override fun getNM(value: Double, kindOf: KindOfTaxEnum): Double {
        return when(kindOf){
            KindOfTaxEnum.EA->{
                toNominal(value,PERIODS_YEAR)/PERIODS_YEAR
            }
            KindOfTaxEnum.EM->{
                toNominal(fromEffectiveMonthlyToEffectiveYearly(value , PERIODS_YEAR),PERIODS_YEAR) / PERIODS_YEAR
            }
            KindOfTaxEnum.NA->{
                value/PERIODS_YEAR
            }
            KindOfTaxEnum.NM->{
                value/100
            }
        }
    }

    private fun toNominal(taxEffective:Double,periods:Int):Double{
        val pow = (1 / periods.toDouble())
        val part1 = 1 + (taxEffective/100)
        return  ((part1.pow(pow)-1) * periods)
    }

    private fun fromEffectiveMonthlyToEffectiveYearly(taxEffective:Double,periods:Int):Double{
        return (((1 + (taxEffective/100)).pow(periods)) -1) * 100
    }

}