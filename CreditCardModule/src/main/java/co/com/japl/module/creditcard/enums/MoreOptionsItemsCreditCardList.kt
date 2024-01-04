package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsCreditCardList(@StringRes val title: Int) : IMoreOptions {

    EDIT(R.string.edit),
    DELETE(R.string.delete),
    SETTINGS(R.string.setting_redirect);

    override fun getName()=title
}