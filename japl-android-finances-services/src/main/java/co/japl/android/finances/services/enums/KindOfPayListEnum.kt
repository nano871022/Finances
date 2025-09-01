package co.japl.android.finances.services.enums

import androidx.annotation.StringRes
import co.japl.android.finances.services.R

enum class KindOfPayListEnum (private val months:Int,private val monthStr:String,@StringRes private val value: Int) {
    Monthly(1,"ML",R.string.kind_of_pay_monthly),
    Bi_Monthly(2,"BM",R.string.kind_of_pay_bi_monthly),
    Quarterly(3,"QM",R.string.kind_of_pay_quarterly),
    Four_Monthly(4,"FM",R.string.kind_of_pay_four_monthly),
    Semi_Annual(6,"SM",R.string.kind_of_pay_semi_annual),
    Annual(12,"AM",R.string.kind_of_pay_annual);

    fun getValue() = value
    fun getMonthStr() = monthStr
    fun getMonths() = months

    object actions {
        fun findByMonthStr(value:String): KindOfPayListEnum{
            return KindOfPayListEnum.entries.firstOrNull { it.monthStr == value } ?: KindOfPayListEnum.Annual
        }

        fun findByMonths(value:Int): KindOfPayListEnum{
            return KindOfPayListEnum.entries.firstOrNull { it.months == value } ?: KindOfPayListEnum.Annual
        }
    }
}