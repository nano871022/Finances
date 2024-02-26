package co.japl.android.finances.services.enums

import androidx.annotation.StringRes
import co.japl.android.finances.services.R

enum class KindOfPayListEnum (@StringRes private val value: Int) {
    Monthly(R.string.kind_of_pay_monthly),
    Bi_Monthly(R.string.kind_of_pay_bi_monthly),
    Quarterly(R.string.kind_of_pay_quarterly),
    Four_Monthly(R.string.kind_of_pay_four_monthly),
    Semi_Annual(R.string.kind_of_pay_semi_annual),
    Annual(R.string.kind_of_pay_annual);

    fun getValue() = value

}