package co.com.japl.module.creditcard.enums

import androidx.annotation.StringRes
import co.com.japl.module.creditcard.R
import co.com.japl.ui.enums.IMoreOptions

enum class MoreOptionsItemsSettingsCreditCard(@StringRes  val value: Int): IMoreOptions {

    EDIT(R.string.edit),DELETE(R.string.delete);

    override fun getName()=value


}