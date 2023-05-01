package co.japl.android.myapplication.finanzas.bussiness.impl

import android.util.Log
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import kotlin.math.pow

class KindOfTaxImpl:IKindOfTaxSvc {
    private val PERIODS_YEAR = 12
    override fun getNM(value: Double, kindOf: KindOfTaxEnum): Double {
        return when(kindOf){
            KindOfTaxEnum.EA->{
                toNominal(value,PERIODS_YEAR)/PERIODS_YEAR
            }
            KindOfTaxEnum.EM->{
                toNominal(value * PERIODS_YEAR,PERIODS_YEAR) / PERIODS_YEAR
            }
            KindOfTaxEnum.NA->{
                value/PERIODS_YEAR
            }
            KindOfTaxEnum.NM->{
                value
            }
        }
    }

    private fun toNominal(taxEffective:Double,periods:Int):Double{
        val pow = (1 / periods.toDouble())
        val part1 = 1 + taxEffective
        val tax =  part1.pow(pow) * periods
        Log.d(javaClass.name,"Tax NA $tax = [$part1](1 + $taxEffective) ^ (1 / $periods)[$pow] X $periods")
        return tax
    }

}