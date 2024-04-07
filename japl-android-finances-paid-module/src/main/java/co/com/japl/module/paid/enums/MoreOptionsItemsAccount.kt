package co.com.japl.module.paid.enums

import androidx.annotation.StringRes
import co.com.japl.module.paid.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsAccount(val i: Int, @StringRes val title: Int) : IMoreOptions{

    EDIT(2,R.string.edit),DELETE(1, R.string.delete);

    override fun getName(): Int = title

}