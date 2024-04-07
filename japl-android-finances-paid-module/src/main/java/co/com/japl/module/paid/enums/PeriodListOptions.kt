package co.com.japl.module.paid.enums

import androidx.annotation.StringRes
import co.com.japl.module.paid.R
import co.com.japl.ui.enums.IMoreOptions

enum class PeriodListOptions (@StringRes val id: Int): IMoreOptions {
    SEE(R.string.see);
    override fun getName(): Int {
        return id
    }
}