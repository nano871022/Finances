package co.japl.android.myapplication.finanzas.holders.validations

import co.com.japl.utils.NumbersUtil
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.toMoneyFormat() = setCOPtoField()
fun TextInputEditText.toBigDecimal() = NumbersUtil.toBigDecimal(text())