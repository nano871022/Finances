package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsRecurrentBought(@StringRes val title: Int) : IMoreOptions {
    ACTIVATE(R.string.recurrent_activate),
    DEACTIVATE(R.string.recurrent_deactivate),
    COPY(R.string.recurrent_copy),
    DELETE(R.string.delete),
    EDIT(R.string.edit),
    ALTER(R.string.recurrent_alter);

    override fun getName(): Int = title
}
