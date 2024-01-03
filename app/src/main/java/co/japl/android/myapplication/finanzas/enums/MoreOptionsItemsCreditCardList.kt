package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.com.japl.ui.enums.IMoreOptions
import co.japl.android.myapplication.R

enum class MoreOptionsItemsCreditCardList(@StringRes val title: Int) : IMoreOptions {

    EDIT(R.string.ccio_edit),
    DELETE(R.string.ccio_delete),
    SETTINGS(R.string.setting_redirect);

    override fun getName()=title
}