package co.japl.android.myapplication.finanzas.holders.validations

import android.widget.TextView
import androidx.annotation.StringRes
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText

fun TextView.COPToBigDecimal() = NumbersUtil.stringCOPToBigDecimal(text.toString())
