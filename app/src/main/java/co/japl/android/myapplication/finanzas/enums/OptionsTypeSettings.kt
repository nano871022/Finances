package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.com.japl.ui.enums.IMoreOptions
import co.japl.android.myapplication.R

enum class OptionsTypeSettings constructor(@StringRes private val value:Int): IMoreOptions {

    SPECIAL_TAX(R.string.item_select_setting_special_tax),
    REDIFER(R.string.item_select_setting_redifer);

    override fun getName() = value

}