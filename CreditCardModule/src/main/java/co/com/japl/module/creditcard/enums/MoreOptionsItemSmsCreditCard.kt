package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemSmsCreditCard (@StringRes val title: Int) : IMoreOptions{
    DELETE(R.string.delete)
    , EDIT(R.string.edit),
    ENABLE(R.string.enabled)
    , DISABLE(R.string.disabled);

    override fun getName()=title
}