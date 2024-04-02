package co.com.japl.module.paid.enums

import androidx.annotation.StringRes
import co.com.japl.module.paid.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsKindPaymentInput constructor(@StringRes val title:Int):IMoreOptions{

    MONTHLY(R.string.monthly),
    BI_MONTHLY(R.string.bi_monthly),
    TRIMESTERLY(R.string.trimesterly),
    QUARTERLY(R.string.quarterly),
    SEMESTERLY(R.string.semesterly),
    YEARLY(R.string.yearly);

    override fun getName():Int = title
}