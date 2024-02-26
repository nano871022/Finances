package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsTypeSettings constructor(@StringRes private val value:Int): IMoreOptions {

    SPECIAL_TAX(R.string.item_select_setting_special_tax),
    REDIFER(R.string.item_select_setting_redifer);

    override fun getName() = value

}