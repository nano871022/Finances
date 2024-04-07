package co.com.japl.module.paid.enums

import androidx.annotation.StringRes
import co.com.japl.module.paid.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsInput(val i: Int,@StringRes val title: Int) : IMoreOptions {

    DELETE(0, R.string.delete_input),
    UPDATE_VALUE(1, R.string.update_input_value );

    override fun getName(): Int = title

}