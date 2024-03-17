package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemSmsCreditCard (@StringRes val title: Int) : IMoreOptions{
    DELETE(R.string.delete)
    , EDIT(R.string.edit);

    override fun getName()=title
}