package co.japl.finances.core.usercases.implement.recap

import co.japl.finances.core.enums.KindOfTaxEnum
import kotlin.math.pow

object KindOfTaxImpl  {
    private val PERIODS_YEAR = 12
    fun getNM(value: Double, kindOf: KindOfTaxEnum): Double {
        return when(kindOf){
            KindOfTaxEnum.ANUAL_EFFECTIVE->{
                toNominal(value,PERIODS_YEAR)/PERIODS_YEAR
            }
            KindOfTaxEnum.MONTHLY_EFFECTIVE->{
                toNominal(fromEffectiveMonthlyToEffectiveYearly(value , PERIODS_YEAR),PERIODS_YEAR) / PERIODS_YEAR
            }
            KindOfTaxEnum.ANUAL_NOMINAL->{
                value/PERIODS_YEAR
            }
            KindOfTaxEnum.MONTLY_NOMINAL->{
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