package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemPaidPeriodEnum (@StringRes val i: Int) : IMoreOptions {
    SEE(R.string.look_at);

    override fun getName(): Int = i
}