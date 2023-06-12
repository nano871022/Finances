package co.japl.android.myapplication.finanzas.holders.validations

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal

fun TextInputEditText.text(value: String? = null) = value?.also{ setText(it)} ?: "$text"
fun MaterialAutoCompleteTextView.text(value: String? = null) = value?.also{ setText(it)} ?: "$text"
fun TextInputEditText.isNumber() = text()?.let{ Regex("^[0-9\\.]+]$").containsMatchIn(it) } ?: false
fun TextInputEditText.isNotLocalDate() = text()?.let{!Regex("^[0123][0-9]/[01][0-9]/[129][0-9][0-9][0-9]$").containsMatchIn(it)} ?: false
@RequiresApi(Build.VERSION_CODES.O)

fun TextInputEditText.toLocalDate( ) = text()?.let{ DateUtils.toLocalDate(it) }

fun TextInputEditText.COPtoBigDecimal() = text()?.let{NumbersUtil.stringCOPToBigDecimal( it ) } ?: BigDecimal.ZERO
fun TextInputEditText.setCOPtoField() = text()?.let{text( NumbersUtil.COPtoString(COPtoBigDecimal() ) ) }

fun TextInputEditText.setNumberToField() = text()?.let{text( NumbersUtil.toString(COPtoBigDecimal() ) ) }

fun MaterialTextView.text(value: String? = null) = value?.also{ text = it} ?: "$text"
fun MaterialTextView.COPtoBigDecimal() = text()?.let{NumbersUtil.stringCOPToBigDecimal( it ) } ?: BigDecimal.ZERO

infix fun TextInputEditText.set(@StringRes resource: Int) = this to resource
infix fun MaterialAutoCompleteTextView.set(@StringRes resource: Int) = this to resource

infix fun Pair<TextInputEditText,Int>.`when`(valid: TextInputEditText.() -> Boolean) = Validation(first,second,valid)
infix fun Pair<MaterialAutoCompleteTextView,Int>.`when`(valid:MaterialAutoCompleteTextView.() -> Boolean) = ValidationAutoComplete(first,second,valid)

infix fun <T : Any> T?.isNull(exec: (T) -> Unit): T? = this?.apply{exec(this)}
infix fun Any?.notNull(exec: () -> Unit) = this ?: exec()

class Validation(val editText: TextInputEditText, @StringRes val resource: Int, val validator: (TextInputEditText.() -> Boolean))
class ValidationAutoComplete(val editText: MaterialAutoCompleteTextView, @StringRes val resource: Int, val validator: (MaterialAutoCompleteTextView.() -> Boolean))

fun Array<Validation>.firstInvalid(onFound: (TextInputEditText.() -> Unit)? = null) = firstOrNull{ set ->
    set.run {
        validator(editText).also{ invalid ->
            if(invalid){
                with(editText){
                    error = context.getString(set.resource)
                    onFound.notNull (){
                        
                    }
                }
            }
        }
    }
}

fun Array<ValidationAutoComplete>.firstInvalid(onFound: (MaterialAutoCompleteTextView.() -> Unit)? = null) = firstOrNull{ set ->
    set.run {
        validator(editText).also{ invalid ->
            if(invalid){
                with(editText){
                    error = context.getString(set.resource)
                    onFound.notNull (){

                    }
                }
            }
        }
    }
}
