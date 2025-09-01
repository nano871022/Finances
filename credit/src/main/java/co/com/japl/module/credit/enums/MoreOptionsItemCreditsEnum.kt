package co.com.japl.module.credit.enums

import androidx.annotation.StringRes
import co.com.japl.module.credit.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemCreditsEnum (@StringRes val i:Int):IMoreOptions{
    ADDITIONAL(R.string.additional),
    AMOTIZATION(R.string.amortization),
    PERIOD_GRACE(R.string.period_grace),
    DELETE(R.string.delete),
    DELETE_PERIOD_GRACE(R.string.delete_period_grace);

    override fun getName(): Int = i
}