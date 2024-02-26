package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.com.japl.ui.enums.IMoreOptions
import co.japl.android.myapplication.R

enum class MoreOptionsItemsInput(val i: Int,@StringRes val title: Int) : IMoreOptions {

    DELETE(0, R.string.delete_input),
    UPDATE_VALUE(1, R.string.update_input_value );

    override fun getName(): Int = title

}