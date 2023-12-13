package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.japl.android.myapplication.R

enum class MoreOptionsItemPaidPeriodEnum (@StringRes val i: Int) : IMoreOptions {
    SEE(R.string.look_at);

    override fun getName(): Int = i
}