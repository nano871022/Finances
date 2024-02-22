package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.com.japl.ui.enums.IMoreOptions
import co.japl.android.myapplication.R

enum class MoreOptionsItemsAccount(val i: Int, @StringRes val title: Int) : IMoreOptions{

    EDIT(2,R.string.edit),DELETE(1, R.string.delete);

    override fun getName(): Int = title

}