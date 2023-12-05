package co.japl.android.finances.services.implement

import android.util.Log
import co.japl.android.finances.services.interfaces.Calc
import co.japl.android.finances.services.implement.KindOfTaxImpl
import co.japl.android.finances.services.interfaces.IKindOfTaxSvc
import co.japl.android.finances.services.enums.KindOfTaxEnum
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.pow

class QuoteCredit @Inject constructor() : Calc {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()

    override fun calc(value: BigDecimal, period: Long, tax: Double,kindOf: KindOfTaxEnum): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val taxValue = kindOfTaxSvc.getNM(tax,kindOf)
        val onePlusTaxPowPeriod = onePlusTaxPowPeriod(taxValue,period)
        val taxAndPeriodDiv = (taxValue*onePlusTaxPowPeriod).div(onePlusTaxPowPeriod-1)
        return value.multiply(taxAndPeriodDiv.toBigDecimal()).also { Log.d(this.javaClass.name,"<<<=== FINISH:: Calc: $it = $value X (($tax X $onePlusTaxPowPeriod) / ($onePlusTaxPowPeriod - 1))") }
    }

    private fun onePlusTaxPowPeriod(tax:Double,period:Long):Double{
        val response = (1+tax).pow(period.toDouble())
        return response.also { Log.d(this.javaClass.name,"<<<=== FINISH:: OnePlusTaxPowPeriod: $response = 1 + $tax ^ $period") }
    }
    fun getCreditValue(creditValue:BigDecimal, tax:Double, periodPaid:Int, period:Int, quote:BigDecimal, kindOfTax: KindOfTaxEnum):BigDecimal{
        var sumCapital = BigDecimal.ZERO
        for (p in 1..periodPaid) {
            val capital = quote - getInterest(creditValue,tax,period,quote,p,kindOfTax)
            sumCapital += capital
        }

        return sumCapital
    }

    fun getInterest(creditValue:BigDecimal, tax:Double, period:Int, quote:BigDecimal, periodPaid:Int, kindOfTax: KindOfTaxEnum):BigDecimal{
        val tax = kindOfTaxSvc.getNM(tax, kindOfTax)
        val part3 = creditValue.toDouble() * tax
        if(periodPaid > 1) {
            val pow1 = (period) - 5
            val part5 = (1 + tax).pow(pow1)
            val part1 = creditValue.toDouble() * (tax / part5)
            val part2 = (1 - ((1 + tax).pow(periodPaid )))
            val interest = part3 + (part1 * part2)
            Log.d(
                javaClass.name,
                "$interest = ($creditValue X $tax)[$part3] + ( ($creditValue X $tax /((1 + $tax)^($period - 1)[$pow1])[$part5])[$part1] X ((1-((1 + $tax)^($periodPaid))[$part2])"
            )
            return interest.toBigDecimal()
        }
        return part3.toBigDecimal()
    }
}