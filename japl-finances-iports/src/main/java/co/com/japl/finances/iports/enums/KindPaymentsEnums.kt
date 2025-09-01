package co.com.japl.finances.iports.enums

import androidx.annotation.StringRes
import co.com.japl.finances.iports.R

enum class KindPaymentsEnums constructor(@StringRes val title:Int, val month:Int){
    MONTHLY(R.string.monthly,1),
    QUARTERLY(R.string.quarterly,3),
    SEMIANNUAL(R.string.semiannual,6),
    ANNUAL(R.string.annual,12),
    BIMONTHLY(R.string.bimonthly,2),
    FOUR_MOTHS_PERIOD(R.string.four_months_period,4);

    companion object {
        fun findByValue(value: String):KindPaymentsEnums {
            return KindPaymentsEnums.entries.firstOrNull { it.name == value } ?: MONTHLY
        }
        fun findByIndex(index:Int?):KindPaymentsEnums{
            return KindPaymentsEnums.entries.firstOrNull { it.month == index } ?: MONTHLY
        }

        fun existIndex(index:Int?):Boolean{
            return KindPaymentsEnums.entries.any { it.month == index }
        }
        fun find(kind:String):KindPaymentsEnums{
            return KindPaymentsEnums.entries.firstOrNull { it.toString() == kind } ?: MONTHLY
        }
    }
}