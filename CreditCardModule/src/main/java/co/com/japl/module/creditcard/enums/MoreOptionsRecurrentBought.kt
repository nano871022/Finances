package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.interfaces.IMoreOptions

enum class MoreOptionsRecurrentBought(@StringRes override val title: Int) : IMoreOptions {
    ACTIVATE(R.string.activate),
    DEACTIVATE(R.string.deactivate),
    COPY(R.string.copy),
    DELETE(R.string.delete),
    EDIT(R.string.edit),
    ALTER(R.string.alter);
}
