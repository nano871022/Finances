package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsCreditCard(@StringRes val title: Int) : IMoreOptions {

    EDIT(R.string.ccio_edit),
    AMORTIZATION(R.string.ccio_amortization),
    DELETE(R.string.ccio_delete),
    ENDING(R.string.ccio_ending),
    UPDATE_VALUE(R.string.ccio_update_value),
    DIFFER_INSTALLMENT(R.string.differ_installment),
    CLONE(R.string.ccio_clone)
    ,RESTORE(R.string.ccio_restore);

    override fun getName() = title
}
