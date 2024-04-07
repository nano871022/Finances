package co.com.japl.module.paid.enums

import androidx.annotation.StringRes
import co.com.japl.module.paid.R
import co.com.japl.ui.enums.IMoreOptions

enum class PaidListOptions (@StringRes val i:Int) : IMoreOptions {

    DELETE(R.string.delete),
    EDIT(R.string.edit),
    COPY(R.string.copy),
    END(R.string.end);

    override fun getName(): Int = i
}