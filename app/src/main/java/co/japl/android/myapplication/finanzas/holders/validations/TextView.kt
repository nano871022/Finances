package co.japl.android.myapplication.finanzas.holders.validations

import android.widget.TextView
import co.japl.android.myapplication.utils.NumbersUtil

fun TextView.COPToBigDecimal() = NumbersUtil.stringCOPToBigDecimal(text.toString())