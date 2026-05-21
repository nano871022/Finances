package co.com.japl.module.paid.enums

import androidx.annotation.StringRes
import co.com.japl.module.paid.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsEmail(val i: Int, @StringRes val title: Int) : IMoreOptions{

    EDIT(1,R.string.edit),
    CLONE(2,R.string.clone),
    ENABLED(3, R.string.enabled),
    DISABLED(4, R.string.disabled),
    DELETE(5, R.string.delete);

    override fun getName(): Int = title

}