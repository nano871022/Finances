package co.japl.android.myapplication.finanzas.enums

import androidx.annotation.StringRes
import co.com.japl.ui.enums.IMoreOptions
import co.japl.android.myapplication.R

enum class MoreOptionsItemsSettingsCreditCard(@StringRes  val value: Int): IMoreOptions {

    EDIT(R.string.ccio_edit),DELETE(R.string.delete);

    override fun getName()=value


}