package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.japl.android.myapplication.R

enum class MoreOptionsItemsCreditCardList(@StringRes val title: Int) : IMoreOptions{

    EDIT(R.string.ccio_edit),
    DELETE(R.string.ccio_delete);

    override fun getName()=title
}